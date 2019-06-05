package nl.hogeschoolrotterdam.projectb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(WhibApp.getInstance().getThemeId());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText input = findViewById(R.id.login_password_input);

        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WhibApp.getInstance().checkPassword(input.getText().toString())) {
                    WhibApp.getInstance().logIn();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    input.setError(getString(R.string.error_invalid_password));
                }
            }
        });
    }
}
