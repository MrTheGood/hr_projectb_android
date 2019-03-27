package nl.hogeschoolrotterdam.projectb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import nl.hogeschoolrotterdam.projectb.data.Database;
import nl.hogeschoolrotterdam.projectb.data.Memory;
import nl.hogeschoolrotterdam.projectb.data.media.Media;

import java.util.ArrayList;
import java.util.Date;

public class MemoryEditActivity extends AppCompatActivity {
    TextInputEditText titleInput;
    TextInputEditText dateInput;
    TextInputEditText descriptionInput;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);

        titleInput = findViewById(R.id.memory_add_title_input);
        dateInput = findViewById(R.id.memory_add_date_input);
        descriptionInput = findViewById(R.id.memory_add_description_input);
        saveButton = findViewById(R.id.memory_save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng location = new LatLng(0, 0);
                Date date = new Date();
                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();
                ArrayList<Media> media = null;

                Memory memory = new Memory(
                        Database.getInstance().newId(),
                        location,
                        date,
                        title,
                        description,
                        media
                );

                Database.getInstance().addMemory(memory);

                Intent intent = new Intent(MemoryEditActivity.this, MemoryDetailActivity.class);
                intent.putExtra("EXTRA_SESSION_ID", memory.getId());
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
