package nl.hogeschoolrotterdam.projectb;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class SetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(WhibApp.getInstance().getThemeId());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        final EditText password = findViewById(R.id.set_password_input);
        final EditText repeatPassword = findViewById(R.id.set_password_repeat_input);

        findViewById(R.id.set_password_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p = password.getText().toString();
                String rP = repeatPassword.getText().toString();
                if (p.equals(rP)) {
                    WhibApp.getInstance().setPassword(p);
                    finish();
                } else {
                    password.setError(getString(R.string.error_passwords_dont_match));
                    repeatPassword.setError(getString(R.string.error_passwords_dont_match));
                }
            }
        });
    }
}
