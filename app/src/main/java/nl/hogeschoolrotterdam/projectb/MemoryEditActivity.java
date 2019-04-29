package nl.hogeschoolrotterdam.projectb;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputLayout;
import nl.hogeschoolrotterdam.projectb.data.Database;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Media;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Memory;
import nl.hogeschoolrotterdam.projectb.util.LocationManager;
import nl.hogeschoolrotterdam.projectb.util.SimpleTextWatcher;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.*;

public class MemoryEditActivity extends AppCompatActivity {
    public static final int GALLERY_REQUEST = 20;
    public static final int IMAGE_CAMERA_REQUEST = 21;
    public static final int VIDEO_CAMERA_REQUEST = 22;
    public static final int LOCATION_EDIT = 23;
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
            public void onClick(View v) {
                lastClick = (ImageButton) v;
                if (lastClick.getTag() != null) {
                    new AlertDialog.Builder(MemoryEditActivity.this)
                            .setTitle(R.string.dialog_add_memory_media_change_media)
                            .setMessage(R.string.dialog_add_memory_media_change_media_description)
                            .setPositiveButton(R.string.dialog_add_memory_media_change_media_positive, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    lastClick.setImageResource(R.drawable.add_media_icon);
                                    lastClick.setTag(null);
                                }
                            })
                            .setNegativeButton(R.string.dialog_add_memory_media_change_media_negative, null)
                            .show();
                } else {
                    mediaFile = createImageFile();
                    final Uri mediaUri = FileProvider.getUriForFile(v.getContext(), getApplicationContext().getPackageName() + ".fileProvider", mediaFile);
                    new AlertDialog.Builder(MemoryEditActivity.this)
                            .setTitle(R.string.dialog_add_memory_media_title1)
                            .setMessage(R.string.dialog_add_memory_media_description1)
                            .setPositiveButton(R.string.dialog_add_memory_media_positive1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent mediaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                    mediaIntent.setDataAndType(mediaUri, "image/* video/*");
                                    startActivityForResult(mediaIntent, GALLERY_REQUEST);
                                }

                            })
                            .setNegativeButton(R.string.dialog_add_memory_media_title2, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new AlertDialog.Builder(MemoryEditActivity.this)
                                            .setTitle(R.string.dialog_add_memory_media_title2)
                                            .setMessage(R.string.dialog_add_memory_media_description2)
                                            .setPositiveButton(R.string.dialog_add_memory_media_positive2, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
                                                    startActivityForResult(intent, VIDEO_CAMERA_REQUEST);
                                                }
                                            })
                                            .setNegativeButton(R.string.dialog_add_memory_media_negative2, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                    intent2.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
                                                    intent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                    startActivityForResult(intent2, IMAGE_CAMERA_REQUEST);
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


        // create a memory with calendar to today
        final Calendar calendar = Calendar.getInstance();
        ArrayList<Media> media = new ArrayList<>();
        memory = new Memory(
                Database.getInstance().newId(),
                new LatLng(0, 0),
                new Date(calendar.getTimeInMillis()),
                "",
                "",
                media
        );


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

                Intent i = new Intent( MemoryEditActivity.this,LocationEditActivity.class);
                i.putExtra("location", memory.getLocation());
                startActivityForResult(i,LOCATION_EDIT);
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
                Database.getInstance().addMemory(memory);

                Intent intent = new Intent(MemoryEditActivity.this, MemoryDetailActivity.class);
                intent.putExtra("EXTRA_SESSION_ID", memory.getId());
                startActivity(intent);
                finish();
            }
        });


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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                InputStream inputStream;

                try {
                    inputStream = new FileInputStream(mediaFile);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);


                    lastClick.setImageBitmap(image);
                    lastClick.setTag(mediaFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == VIDEO_CAMERA_REQUEST) {
                InputStream inputStream;

                try {
                    inputStream = new FileInputStream(mediaFile);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);


                    lastClick.setImageBitmap(image);
                    lastClick.setTag(mediaFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == IMAGE_CAMERA_REQUEST) {
                //Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                //File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                //lastClick.setImageBitmap(bitmap);
                //lastClick.setTag(bitmap);
                InputStream inputStream;

                try {
                    inputStream = new FileInputStream(mediaFile);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);


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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_EDIT) {
            if(resultCode == Activity.RESULT_OK){
                memory.setLocation((LatLng) data.getExtras().get("result"));
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File image = new File(Environment.getExternalStorageDirectory(), imageFileName);
        return image;
    }
}
