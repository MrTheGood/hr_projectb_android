package nl.hogeschoolrotterdam.projectb;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputLayout;
import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.Video.VideoPicker;
import nl.hogeschoolrotterdam.projectb.data.Database;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Image;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Media;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Memory;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Video;
import nl.hogeschoolrotterdam.projectb.util.AnalyticsUtil;
import nl.hogeschoolrotterdam.projectb.util.LocationManager;
import nl.hogeschoolrotterdam.projectb.util.SimpleOnItemSelectedListener;
import nl.hogeschoolrotterdam.projectb.util.SimpleTextWatcher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MemoryEditActivity extends AppCompatActivity {
    public static final int LOCATION_EDIT = 23;
    private boolean isEditMode = false;

    private TextInputLayout titleInput;
    private TextInputLayout dateInput;
    private TextInputLayout descriptionInput;
    private Button saveButton;
    private ViewGroup mediaList;
    private ImageButton addMediaButton;
    private Button locationInput;

    private Boolean isTitleValid = false;
    private Boolean isDescriptionValid = false;
    private Spinner spinner;
    private Memory memory = null;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(WhibApp.getInstance().getThemeId());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_edit);


        // initialise views
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Drawable homeAsUpIndicator = ContextCompat.getDrawable(this, R.drawable.ic_action_close); // Workaround for a bug in MaterialComponents
        getSupportActionBar().setHomeAsUpIndicator(WhibApp.getInstance().tintDrawable(homeAsUpIndicator));

        titleInput = findViewById(R.id.memory_add_title);
        dateInput = findViewById(R.id.memory_add_date);
        descriptionInput = findViewById(R.id.memory_add_description);
        saveButton = findViewById(R.id.memory_save_button);
        locationInput = findViewById(R.id.memory_change_location_input);
        addMediaButton = findViewById(R.id.add_media);
        mediaList = findViewById(R.id.images);
        spinner = findViewById(R.id.Marker_selector);

        dateInput.getEditText().setKeyListener(null);

        final Calendar calendar = Calendar.getInstance();
        if (getIntent().getStringExtra("ID") != null) {
            isEditMode = true;
            Database database = Database.getInstance();
            String sessionId = getIntent().getStringExtra("ID");
            memory = database.findMemory(sessionId);

            for (Media m : memory.getMedia()) {
                if (m instanceof Image) {
                    Bitmap image = ((Image) m).getImage();
                    addMediaImageView(image, m);
                } else if (m instanceof Video) {
                    Bitmap image = ((Video) m).getThumbnail();
                    addMediaImageView(image, m);
                }
            }

            titleInput.getEditText().setText(memory.getTitle());
            descriptionInput.getEditText().setText(memory.getDescription());
            dateInput.getEditText().setText(memory.getDateText());
            isTitleValid = true;
            isDescriptionValid = true;

        } else {
            // create a memory with calendar to today
            ArrayList<Media> media = new ArrayList<>();
            memory = new Memory(
                    Database.getInstance().newId(),
                    new LatLng(0, 0),
                    new Date(calendar.getTimeInMillis()),
                    "",
                    "",
                    media,
                    R.drawable.ic_map_adefault
            );

            dateInput.getEditText().setText(memory.getDateText());
            if (getIntent().getExtras() != null && getIntent().getExtras().get("location") != null) {
                latLng = (LatLng) getIntent().getExtras().get("location");
                memory.setLocation(latLng);
            } else {
                // Get current location from the LocationManager
                LocationManager.getInstance()
                        .initialize(this)
                        .updateLocation(this, new LocationManager.OnLocationResultListener() {
                            @Override
                            public void onLocationResult(@Nullable Location location) {
                                if (location == null) {
                                    Toast.makeText(MemoryEditActivity.this, R.string.error_could_not_set_current_location, Toast.LENGTH_LONG).show();
                                    return;
                                }
                                memory.setLocation(new LatLng(location.getLatitude(), location.getLongitude()));
                            }
                        });
            }
        }

        setButtonEnabled(); // save button should be disabled when input not yet valid

        // check if the title is valid when the text has changed
        titleInput.getEditText().addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                isTitleValid = s.toString().length() > 1;
                titleInput.setError(isTitleValid ? null : getString(R.string.error_title_too_short)); // show an error if title is too short
                memory.setTitle(s.toString());
                setButtonEnabled();
            }
        });

        // Open date picker if date input was selected
        dateInput.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialog dialog = new DatePickerDialog(
                            MemoryEditActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                    calendar.set(year, month, day, 0, 0); // set calendar to the selected time
                                    memory.setDate(new Date(calendar.getTimeInMillis())); // update memory date
                                    dateInput.getEditText().setText(memory.getDateText()); // set input text
                                    findViewById(v.getNextFocusDownId()).requestFocus(); // request focus for the next input
                                }
                            },
                            calendar.get(Calendar.YEAR), // picker dialog selected year
                            calendar.get(Calendar.MONTH), // picker dialog selected month
                            calendar.get(Calendar.DAY_OF_MONTH) // picker dialog selected day
                    );
                    dialog.getDatePicker().setMaxDate(System.currentTimeMillis()); // limit date picker to picking only past dates
                    dialog.show();
                }
            }
        });

        addMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(MemoryEditActivity.this)
                        .setTitle(R.string.action_camera)
                        .setMessage(R.string.dialog_add_media_camera_description)
                        .setPositiveButton(R.string.action_video, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new VideoPicker.Builder(MemoryEditActivity.this)
                                        .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
                                        .directory(VideoPicker.Directory.DEFAULT)
                                        .extension(VideoPicker.Extension.MP4)
                                        .enableDebuggingMode(true)
                                        .build();
                            }
                        })
                        .setNegativeButton(R.string.action_image, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new ImagePicker.Builder(MemoryEditActivity.this)
                                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                                        .directory(ImagePicker.Directory.DEFAULT)
                                        .extension(ImagePicker.Extension.PNG)
                                        .scale(600, 600)
                                        .allowMultipleImages(false)
                                        .enableDebuggingMode(true)
                                        .build();
                            }
                        }).show();

            }
        });

        // Open location picker if location input was selected
        locationInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MemoryEditActivity.this, LocationEditActivity.class);
                i.putExtra("location", memory.getLocation());
                startActivityForResult(i, LOCATION_EDIT);
            }
        });

        spinner.setOnItemSelectedListener(new SimpleOnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iconSelected(position);
            }
        });

        // check if the description is valid when the text has changed
        descriptionInput.getEditText().addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                isDescriptionValid = s.toString().length() > 1;
                descriptionInput.setError(isDescriptionValid ? null : getString(R.string.error_description_too_short)); // show an error if description is too short
                memory.setDescription(s.toString());
                setButtonEnabled();
            }
        });

        // save and open new memory on save clicked
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditMode) {
                    Database.getInstance().updateMemory(memory);
                    AnalyticsUtil.editContent(MemoryEditActivity.this);
                } else {
                    Database.getInstance().addMemory(memory);
                    Intent intent = new Intent(MemoryEditActivity.this, MemoryDetailActivity.class);
                    intent.putExtra("EXTRA_SESSION_ID", memory.getId());
                    startActivity(intent);
                }
                finish();
            }
        });
    }

    private void iconSelected(int selectedIcon) {
        int foundIcon;
        switch (selectedIcon) {
            case 0:
                foundIcon = R.drawable.ic_map_adefault;
                break;
            case 1:
                foundIcon = R.drawable.ic_map_architecture;
                break;
            case 2:
                foundIcon = R.drawable.ic_map_baby;
                break;
            case 3:
                foundIcon = R.drawable.ic_map_bar;
                break;
            case 4:
                foundIcon = R.drawable.ic_map_beach;
                break;
            case 5:
                foundIcon = R.drawable.ic_map_birthday;
                break;
            case 6:
                foundIcon = R.drawable.ic_map_cafe;
                break;
            case 7:
                foundIcon = R.drawable.ic_map_camera;
                break;
            case 8:
                foundIcon = R.drawable.ic_map_cart;
                break;
            case 9:
                foundIcon = R.drawable.ic_map_city;
                break;
            case 10:
                foundIcon = R.drawable.ic_map_flagged;
                break;
            case 11:
                foundIcon = R.drawable.ic_map_games;
                break;
            case 12:
                foundIcon = R.drawable.ic_map_golf;
                break;
            case 13:
                foundIcon = R.drawable.ic_map_home;
                break;
            case 14:
                foundIcon = R.drawable.ic_map_images;
                break;
            case 15:
                foundIcon = R.drawable.ic_map_iron_throne;
                break;
            case 16:
                foundIcon = R.drawable.ic_map_job;
                break;
            case 17:
                foundIcon = R.drawable.ic_map_landscape;
                break;
            case 18:
                foundIcon = R.drawable.ic_map_money;
                break;
            case 19:
                foundIcon = R.drawable.ic_map_mood_extremely_happy;
                break;
            case 20:
                foundIcon = R.drawable.ic_map_mood_extremely_sad;
                break;
            case 21:
                foundIcon = R.drawable.ic_map_mood_happy;
                break;
            case 22:
                foundIcon = R.drawable.ic_map_mood_sad;
                break;
            case 23:
                foundIcon = R.drawable.ic_map_mood_very_happy;
                break;
            case 24:
                foundIcon = R.drawable.ic_map_mood_very_sad;
                break;
            case 25:
                foundIcon = R.drawable.ic_map_music;
                break;
            case 26:
                foundIcon = R.drawable.ic_map_painting;
                break;
            case 27:
                foundIcon = R.drawable.ic_map_party;
                break;
            case 28:
                foundIcon = R.drawable.ic_map_pets;
                break;
            case 29:
                foundIcon = R.drawable.ic_map_pool;
                break;
            case 30:
                foundIcon = R.drawable.ic_map_restaurant;
                break;
            case 31:
                foundIcon = R.drawable.ic_map_school;
                break;
            case 32:
                foundIcon = R.drawable.ic_map_sexy;
                break;
            case 33:
                foundIcon = R.drawable.ic_map_shield;
                break;
            case 34:
                foundIcon = R.drawable.ic_map_spa;
                break;
            case 35:
                foundIcon = R.drawable.ic_map_speakers;
                break;
            case 36:
                foundIcon = R.drawable.ic_map_starred;
                break;
            case 37:
                foundIcon = R.drawable.ic_map_store;
                break;
            case 38:
                foundIcon = R.drawable.ic_map_styles;
                break;
            case 39:
                foundIcon = R.drawable.ic_map_teathers;
                break;
            case 40:
                foundIcon = R.drawable.ic_map_tourist;
                break;
            case 41:
                foundIcon = R.drawable.ic_map_want_to_go;
                break;
            case 42:
                foundIcon = R.drawable.ic_map_world;
                break;
            default:
                foundIcon = R.drawable.ic_map_adefault;
        }
        memory.setMemoryTypeIconId(foundIcon);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == LOCATION_EDIT) {
                memory.setLocation((LatLng) data.getExtras().get("result"));
            } else if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE) {
                List<String> paths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
                for (String path : paths) {
                    Image media = new Image(memory.getId(), path);
                    Bitmap image = media.getImage();

                    addMediaImageView(image, media);
                    memory.addMedia(media);
                }
            } else if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE) {
                List<String> paths = data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);

                for (String path : paths) {
                    Video media = new Video(memory.getId(), path);
                    Bitmap image = media.getThumbnail();

                    addMediaImageView(image, media);
                    memory.addMedia(media);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void addMediaImageView(Bitmap bitmap, final Media media) {
        final ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.item_memory_image_edit, mediaList, false);
        mediaList.addView(imageView);
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(MemoryEditActivity.this)
                        .setTitle(R.string.dialog_remove_image)
                        .setMessage(R.string.dialog_remove_image_description)
                        .setPositiveButton(R.string.action_remove, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                memory.removeMedia(media);
                                mediaList.removeView(v);
                            }
                        })
                        .setNegativeButton(R.string.action_cancel, null)
                        .show();
            }
        });
    }


    private void setButtonEnabled() {
        saveButton.setEnabled(isDescriptionValid && isTitleValid);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isEditMode)
            AnalyticsUtil.cancelEditContent(this);
        else
            AnalyticsUtil.cancelAddContent(this);
    }
}
