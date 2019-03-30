package nl.hogeschoolrotterdam.projectb;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputLayout;
import nl.hogeschoolrotterdam.projectb.data.Database;
import nl.hogeschoolrotterdam.projectb.data.Memory;
import nl.hogeschoolrotterdam.projectb.data.media.Media;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);

        ArrayList<Media> media = null;
        memory = new Memory(
                Database.getInstance().newId(),
                new LatLng(0, 0),
                new Date(),
                "",
                "",
                media
        );

        titleInput = findViewById(R.id.memory_add_title);
        dateInput = findViewById(R.id.memory_add_date);
        descriptionInput = findViewById(R.id.memory_add_description);
        saveButton = findViewById(R.id.memory_save_button);

        setButtonEnabled();
        dateInput.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {
                    final Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    new DatePickerDialog(MemoryEditActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                    calendar.set(year, month - 1, day, 0, 0);
                                    memory.setDate(new Date(calendar.getTimeInMillis()));
                                    dateInput.getEditText().setText(memory.getDateText());
                                    findViewById(v.getNextFocusDownId()).requestFocus();
                                }
                            },
                            year, month, day)
                            .show();
                }
            }
        });
        titleInput.getEditText().addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                isTitleValid = s.toString().length() > 1;
                titleInput.setError(isTitleValid ? null : getString(R.string.error_title_too_short));
                memory.setTitle(s.toString());
                setButtonEnabled();
            }
        });
        descriptionInput.getEditText().addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                isDescriptionValid = s.toString().length() > 1;
                descriptionInput.setError(isDescriptionValid ? null : getString(R.string.error_description_too_short));
                memory.setDescription(s.toString());
                setButtonEnabled();
            }
        });


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
