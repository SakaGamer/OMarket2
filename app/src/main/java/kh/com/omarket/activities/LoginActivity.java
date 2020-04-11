package kh.com.omarket.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import kh.com.omarket.R;
import kh.com.omarket.models.FirebaseServer;
import kh.com.omarket.models.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String PREFERENCE_NAME = "omarket-app" ;
    private static final String KEY_USERNAME = "username";

    private TextInputEditText edtEmail, edtPassword;
    private LinearLayout layout;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn = (Button) findViewById(R.id.login_btn_login);
        TextView txtDontHaveAcc = (TextView) findViewById(R.id.login_txt_dont_have_acc);
        edtEmail = (TextInputEditText) findViewById(R.id.login_edt_mail);
        edtPassword = (TextInputEditText) findViewById(R.id.login_edt_password);
        layout = (LinearLayout) findViewById(R.id.login_layout);
        progressBar = (ProgressBar) findViewById(R.id.login_progress_bar);

        btn.setOnClickListener(this);
        txtDontHaveAcc.setOnClickListener(this);
        layout.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.login_btn_login) {
            layout.setAlpha((float) 0.1);
            progressBar.setVisibility(View.VISIBLE);
            String inputEmail = edtEmail.getText().toString();
            String inputPassword = edtPassword.getText().toString();
            if (validateForm()) {
                Log.d("omarket", FirebaseServer.loggedIn(inputEmail, inputPassword) + "");
                auth.signInWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User user = FirebaseServer.getUser(FirebaseServer.getCurrentUid());
                                    AppSingleTon.getInstance().setUser(user);
                                    if (user != null) {
                                        // Save username
                                        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString(KEY_USERNAME, user.getName());
                                        editor.apply();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    }
                                } else {
                                    Log.d("omarket", "login fail");
                                    Toast.makeText(getApplicationContext(), "Invalid Email or Password !!!",
                                            Toast.LENGTH_LONG).show();
                                }
                                progressBar.setVisibility(View.GONE);
                                layout.setAlpha(1);
                            }
                        }
                );
            }
        } else if (id == R.id.login_layout) {
            // Hide keyboard
            InputMethodManager inputMethodManager = (InputMethodManager) LoginActivity.this
                    .getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } else if (id == R.id.login_txt_dont_have_acc) {
            startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            overridePendingTransition(R.anim.start_slide_to_right, R.anim.exit_slide_to_right);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.start_slide_to_down, R.anim.exit_slide_to_down);
    }

    private boolean validateForm() {
        boolean valid = true;
        //Validate Password
        String password = edtPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Enter your password to log in");
            valid = false;
        } else {
            edtPassword.setError(null);
        }
        //Validate Email
        String email = edtEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Enter your Email to log in");
            valid = false;
        } else {
            edtEmail.setError(null);
        }
        return valid;
    }
}//end main class
