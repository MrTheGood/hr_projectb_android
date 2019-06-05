package nl.hogeschoolrotterdam.projectb;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(WhibApp.getInstance().getThemeId());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText input = findViewById(R.id.login_password_input);

        if (WhibApp.getInstance().useFingerprint() && Build.VERSION.SDK_INT >= 28) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
                @Override
                public void onCancel() {
                    finish();
                }
            });
            biometricPrompt().authenticate(cancellationSignal, getMainExecutor(), authenticationCallback());
        }


        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WhibApp.getInstance().checkPassword(input.getText().toString())) {
                    login();
                } else {
                    input.setError(getString(R.string.error_invalid_password));
                }
            }
        });
    }


    @RequiresApi(Build.VERSION_CODES.P)
    private BiometricPrompt biometricPrompt() {
        return new BiometricPrompt.Builder(this)
                .setTitle(getString(R.string.str_log_in))
                .setNegativeButton(getString(R.string.action_cancel), getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build();
    }


    @RequiresApi(Build.VERSION_CODES.P)
    private BiometricPrompt.AuthenticationCallback authenticationCallback() {
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@Nullable BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                login();
            }
        };
    }

    private void login() {
        WhibApp.getInstance().logIn();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}
