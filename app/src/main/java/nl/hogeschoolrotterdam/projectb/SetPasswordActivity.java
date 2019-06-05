package nl.hogeschoolrotterdam.projectb;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class SetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(WhibApp.getInstance().getThemeId());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        final EditText oldPassword = findViewById(R.id.old_password_input);
        final EditText password = findViewById(R.id.set_password_input);
        final EditText repeatPassword = findViewById(R.id.set_password_repeat_input);
        final CheckBox useFingerprint = findViewById(R.id.use_fingerprint);
        useFingerprint.setVisibility(Build.VERSION.SDK_INT >= 28 ? View.VISIBLE : View.GONE);
        oldPassword.setVisibility(WhibApp.getInstance().hasPassword() ? View.VISIBLE : View.GONE);

        findViewById(R.id.set_password_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setError(null);
                repeatPassword.setError(null);
                oldPassword.setError(null);

                if (WhibApp.getInstance().hasPassword() && !WhibApp.getInstance().checkPassword(oldPassword.getText().toString())) {
                    oldPassword.setError(getString(R.string.error_invalid_password));
                    return;
                }

                String p = password.getText().toString();
                String rP = repeatPassword.getText().toString();
                if (p.isEmpty()) {
                    repeatPassword.setError(getString(R.string.error_password_empty));
                } else if (p.equals(rP)) {
                    WhibApp.getInstance().setPassword(p);
                    WhibApp.getInstance().setUseFingerprint(useFingerprint.isChecked());
                    finish();
                } else {
                    password.setError(getString(R.string.error_passwords_dont_match));
                    repeatPassword.setError(getString(R.string.error_passwords_dont_match));
                }
            }
        });
    }
}
