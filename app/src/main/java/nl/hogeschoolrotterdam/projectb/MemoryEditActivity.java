package nl.hogeschoolrotterdam.projectb;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputLayout;
import nl.hogeschoolrotterdam.projectb.data.Database;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Image;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Media;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Memory;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Video;
import nl.hogeschoolrotterdam.projectb.util.LocationManager;
import nl.hogeschoolrotterdam.projectb.util.SimpleTextWatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MemoryEditActivity extends AppCompatActivity {
    public static final int GALLERY_REQUEST = 20;
    public static final int IMAGE_CAMERA_REQUEST = 21;
    public static final int VIDEO_CAMERA_REQUEST = 22;
    public static final int LOCATION_EDIT = 23;
    private boolean isEditMode = false;

    private TextInputLayout titleInput;
    private TextInputLayout dateInput;
    private TextInputLayout descriptionInput;
    private Button saveButton;
    private ImageButton cameraButton1, cameraButton2, cameraButton3;
    private ImageButton lastClick;
    private Button locationInput;

    private Boolean isTitleValid = false;
    private Boolean isDescriptionValid = false;

    private Memory memory = null;
    private File mediaFile;
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
        cameraButton1 = findViewById(R.id.memory_camera_button1);
        cameraButton2 = findViewById(R.id.memory_camera_button2);
        cameraButton3 = findViewById(R.id.memory_camera_button3);


        View.OnClickListener cameraClick = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                lastClick = (ImageButton) v;
                if (lastClick.getTag() != null) {
                    new AlertDialog.Builder(MemoryEditActivity.this)
                            .setTitle(R.string.dialog_remove_image)
                            .setMessage(R.string.dialog_remove_image_description)
                            .setPositiveButton(R.string.action_remove, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    lastClick.setImageResource(R.drawable.add_media_icon);
                                    lastClick.setTag(null);
                                }
                            })
                            .setNegativeButton(R.string.action_cancel, null)
                            .show();
                } else {
                    new AlertDialog.Builder(MemoryEditActivity.this)
                            .setTitle(R.string.dialog_add_media)
                            .setMessage(R.string.dialog_add_media_description)
                            .setPositiveButton(R.string.action_gallery, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mediaFile = createMediaFile(false);
                                    Uri mediaUri = FileProvider.getUriForFile(v.getContext(), getApplicationContext().getPackageName() + ".fileProvider", mediaFile);

                                    Intent mediaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    mediaIntent.setDataAndType(mediaUri, "image/* video/*");
                                    mediaIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    startActivityForResult(mediaIntent, GALLERY_REQUEST);
                                }

                            })
                            .setNegativeButton(R.string.action_camera, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new AlertDialog.Builder(MemoryEditActivity.this)
                                            .setTitle(R.string.action_camera)
                                            .setMessage(R.string.dialog_add_media_camera_description)
                                            .setPositiveButton(R.string.action_video, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    mediaFile = createMediaFile(true);
                                                    Uri mediaUri = FileProvider.getUriForFile(v.getContext(), getApplicationContext().getPackageName() + ".fileProvider", mediaFile);
                                                    captureMedia(true, mediaUri);
                                                }
                                            })
                                            .setNegativeButton(R.string.action_image, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    mediaFile = createMediaFile(false);
                                                    Uri mediaUri = FileProvider.getUriForFile(v.getContext(), getApplicationContext().getPackageName() + ".fileProvider", mediaFile);
                                                    captureMedia(false, mediaUri);
                                                }
                                            }).show();
                                }
                            })
                            .show();
                }
            }
        };

        cameraButton1.setOnClickListener(cameraClick);
        cameraButton2.setOnClickListener(cameraClick);
        cameraButton3.setOnClickListener(cameraClick);

        final Calendar calendar = Calendar.getInstance();
        if (getIntent().getStringExtra("ID") != null) {
            isEditMode = true;
            Database database = Database.getInstance();
            String sessionId = getIntent().getStringExtra("ID");
            memory = database.findMemory(sessionId);
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
                    media
            );

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
        // Open location picker if location input was selected
        locationInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MemoryEditActivity.this, LocationEditActivity.class);
                i.putExtra("location", memory.getLocation());
                startActivityForResult(i, LOCATION_EDIT);
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
                if (cameraButton1.getTag() != null) {
                    File mediaFile = (File) cameraButton1.getTag();
                    String path = mediaFile.getAbsolutePath();
                    if (path.endsWith(".mp4")) {
                        memory.addMedia(new Video(0, memory.getId(), path));
                    } else {
                        memory.addMedia(new Image(0, memory.getId(), path));
                    }
                }

                if (isEditMode) {
                    Database.getInstance().updateMemory(memory);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == LOCATION_EDIT) {
                memory.setLocation((LatLng) data.getExtras().get("result"));
            } else if (requestCode == GALLERY_REQUEST || requestCode == VIDEO_CAMERA_REQUEST || requestCode == IMAGE_CAMERA_REQUEST) {
                InputStream inputStream;

                try {
                    inputStream = new FileInputStream(mediaFile);
                    Bitmap image = requestCode == VIDEO_CAMERA_REQUEST
                            ? ThumbnailUtils.createVideoThumbnail(mediaFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND)
                            : BitmapFactory.decodeStream(inputStream);

                    lastClick.setImageBitmap(image);
                    lastClick.setTag(mediaFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void setButtonEnabled() {
        saveButton.setEnabled(isDescriptionValid && isTitleValid);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void captureMedia(boolean isVideo, Uri mediaUri) {
        Intent intent = new Intent(
                isVideo ? MediaStore.ACTION_VIDEO_CAPTURE : MediaStore.ACTION_IMAGE_CAPTURE
        );
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, isVideo ? VIDEO_CAMERA_REQUEST : IMAGE_CAMERA_REQUEST);
    }

    private File createMediaFile(boolean isVideo) {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "MEDIA_" + timeStamp + (isVideo ? ".mp4" : ".jpg");
        File image = new File(Environment.getExternalStorageDirectory(), imageFileName);
        return image;
    }
}
