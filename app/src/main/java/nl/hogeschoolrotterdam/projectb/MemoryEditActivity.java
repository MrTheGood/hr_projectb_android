package nl.hogeschoolrotterdam.projectb;

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
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputLayout;
import nl.hogeschoolrotterdam.projectb.data.Database;
import nl.hogeschoolrotterdam.projectb.data.Memory;
import nl.hogeschoolrotterdam.projectb.data.media.Media;
import nl.hogeschoolrotterdam.projectb.util.LocationManager;
import nl.hogeschoolrotterdam.projectb.util.SimpleTextWatcher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MemoryEditActivity extends AppCompatActivity {
    public static final int GALLERY_REQUEST = 20;
    public static final int IMAGE_CAMERA_REQUEST = 21;
    public static final int VIDEO_CAMERA_REQUEST=22;
    private TextInputLayout titleInput;
    private TextInputLayout dateInput;
    private TextInputLayout descriptionInput;
    private Button saveButton;
    private ImageButton cameraButton1,cameraButton2,cameraButton3;
    private ImageButton lastClick;

    private Boolean isTitleValid = false;
    private Boolean isDescriptionValid = false;

    private Memory memory = null;

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
        cameraButton1 = findViewById(R.id.memory_camera_button1);
        cameraButton2 = findViewById(R.id.memory_camera_button2);
        cameraButton3 = findViewById(R.id.memory_camera_button3);


        View.OnClickListener cameraClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 lastClick= (ImageButton) v;
                new AlertDialog.Builder(MemoryEditActivity.this)
                        .setTitle(R.string.dialog_add_memory_media_title1)
                        .setMessage(R.string.dialog_add_memory_media_description1)
                        .setPositiveButton(R.string.dialog_add_memory_media_positive1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent mediaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                                String pictureDirectoryPath = pictureDirectory.getPath();
                                Uri data = Uri.parse(pictureDirectoryPath);
                                mediaIntent.setDataAndType(data,"image/* video/*");
                                startActivityForResult(mediaIntent,GALLERY_REQUEST);
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
                                                startActivityForResult(intent,VIDEO_CAMERA_REQUEST);
                                            }
                                        })
                                        .setNegativeButton(R.string.dialog_add_memory_media_negative2, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                startActivityForResult(intent2,IMAGE_CAMERA_REQUEST);
                                            }
                                        }).show();
                            }
                        })
                        .show();
            }
        };

        cameraButton1.setOnClickListener(cameraClick);
        cameraButton2.setOnClickListener(cameraClick);
        cameraButton3.setOnClickListener(cameraClick);



        // create a memory with calendar to today
        final Calendar calendar = Calendar.getInstance();
        ArrayList<Media> media = null;
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
        if(resultCode == RESULT_OK){
            if (requestCode==GALLERY_REQUEST){
                Uri imageUri = data.getData();

                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                    lastClick.setImageBitmap(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this,"Unable to open image",Toast.LENGTH_LONG).show();
                }
            }
            else if (requestCode==VIDEO_CAMERA_REQUEST){
                Uri imageUri = data.getData();

                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                    lastClick.setImageBitmap(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this,"Unable to open image",Toast.LENGTH_LONG).show();
                }
            }
            else if (requestCode==IMAGE_CAMERA_REQUEST){
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                lastClick.setImageBitmap(bitmap);
            }
        }
        else{
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
}
