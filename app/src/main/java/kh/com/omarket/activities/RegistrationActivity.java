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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import kh.com.omarket.R;
import kh.com.omarket.models.FirebaseServer;
import kh.com.omarket.models.User;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String PREFERENCE_NAME = "omarket-app" ;
    private static final String KEY_USERNAME = "username";

    private TextInputEditText edtName, edtPhone, edtEmail, edtPassword, edtConfirmPassword;
    private FirebaseAuth auth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Button btnNext = (Button) findViewById(R.id.su_btn_next);
        TextView txtAlreadyHaveAcc = (TextView) findViewById(R.id.su_txt_already_have_acc);
        LinearLayout layout = (LinearLayout) findViewById(R.id.su_layout);

        edtName = (TextInputEditText) findViewById(R.id.su_edt_username);
        edtPhone = (TextInputEditText) findViewById(R.id.su_edt_phone);
        edtEmail = (TextInputEditText) findViewById(R.id.su_edt_mail);
        edtPassword = (TextInputEditText) findViewById(R.id.su_edt_password);
        edtConfirmPassword = (TextInputEditText) findViewById(R.id.su_edt_confirm_password);

        btnNext.setOnClickListener(this);
        txtAlreadyHaveAcc.setOnClickListener(this);
        layout.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.su_btn_next) {
            String inputEmail = edtEmail.getText().toString();
            String inputPassword = edtPassword.getText().toString();
            if (validateForm()) {
                try {
                    auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        onAuthSuccess(task.getResult().getUser());
                                    } else {
                                        Toast.makeText(getApplicationContext(), "create account fail",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    Log.d("omarket", "create account success");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("omarket", "create account fail");
                }
            }
        } else if (id == R.id.su_txt_already_have_acc) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            overridePendingTransition(R.anim.start_slide_to_left, R.anim.exit_slide_to_left);
            finish();
        } else if (id == R.id.su_layout) {
            hideKeyBoard();
        }
    }

    private void onAuthSuccess(FirebaseUser user) {
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        // write new user
        User newUser = new User(user.getUid(), name, phone, email, password);
        FirebaseServer.writeNewUser(user.getUid(), newUser);
        // save user name to share preferences
        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USERNAME, newUser.getName());
        editor.apply();
        // Go to Main Activity
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void hideKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
                .getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.start_slide_to_up, R.anim.exit_slide_to_up);
    }

    private boolean validateForm() {
        boolean valid = true;
        //Validate Username
        String name = edtName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            edtName.setError("Required");
            valid = false;
        } else {
            edtName.setError(null);
        }
        //Validate Phone
        String phone = edtPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            edtPhone.setError("Required");
            valid = false;
        } else {
            edtPhone.setError(null);
        }
        //Validate Email
        String email = edtEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Required");
            valid = false;
        } else if (!email.contains("@") && !email.contains(".")) {
            edtEmail.setError("Email invalid form");
            valid = false;
        } else {
            edtEmail.setError(null);
        }
        //Validate Password
        String password = edtPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Required");
            valid = false;
        } else if (password.length() < 6) {
            edtPassword.setError("Password at least 6 Characters");
            valid = false;
        } else {
            edtPassword.setError(null);
        }
        //Validate Confirm Password
        String confirmPassword = edtConfirmPassword.getText().toString();
        if (!TextUtils.equals(password, confirmPassword)) {
            edtConfirmPassword.setError("Password not matched");
            valid = false;
        } else if (password.length() < 6) {
            edtConfirmPassword.setError("Password at least 6 Characters");
            valid = false;
        } else {
            edtConfirmPassword.setError(null);
        }
        return valid;
    }
}
