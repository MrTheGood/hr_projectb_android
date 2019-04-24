package nl.hogeschoolrotterdam.projectb;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputLayout;
import nl.hogeschoolrotterdam.projectb.data.Database;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Media;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Memory;
import nl.hogeschoolrotterdam.projectb.util.LocationManager;
import nl.hogeschoolrotterdam.projectb.util.SimpleTextWatcher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MemoryEditActivity extends AppCompatActivity {
    private TextInputLayout titleInput;
    private TextInputLayout dateInput;
    private TextInputLayout descriptionInput;
    private Button saveButton;

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

    private void setButtonEnabled() {
        saveButton.setEnabled(isDescriptionValid && isTitleValid);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
