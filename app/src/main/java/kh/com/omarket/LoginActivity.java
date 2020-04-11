package kh.com.omarket;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnLogin ;
    private TextInputEditText edtEmail,edtPassword;
    private TextView txtDontHaveAcc;
    private LinearLayout layout;
    private FirebaseAuth auth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin =  findViewById(R.id.login_btn_login);
        edtEmail = findViewById(R.id.login_edt_mail);
        edtPassword = findViewById(R.id.login_edt_password);
        txtDontHaveAcc = findViewById(R.id.login_txt_dont_have_acc);
        layout = findViewById(R.id.login_layout);

        btnLogin.setOnClickListener(this);
        txtDontHaveAcc.setOnClickListener(this);
        layout.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == btnLogin.getId()) {
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();
            signIn(email, password);
            finish();
        } else if(id == layout.getId()) {
            hideKeyBoard();
        } else if (id == txtDontHaveAcc.getId()) {
            startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            overridePendingTransition(R.anim.start_slide_to_right,R.anim.exit_slide_to_right);
            finish();
        }
    }//end OnClick

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.start_slide_to_down,R.anim.exit_slide_to_down);
    }

    public void hideKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager)LoginActivity.this
                .getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

    private void signIn(String email, String password){
        if (!validateForm()){
            return;
        }
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }//end Sign In

    private boolean validateForm(){
        boolean valid = true;
        //Validate Password
        String password = edtPassword.getText().toString();
        if (TextUtils.isEmpty(password)){
            edtPassword.setError("Enter your password to log in");
            valid = false;
        } else {
            edtPassword.setError(null);
        }
        //Validate Email
        String email = edtEmail.getText().toString();
        if (TextUtils.isEmpty(email)){
            edtEmail.setError("Enter your Email to log in");
            valid = false;
        } else {
            edtEmail.setError(null);
        }
        return valid;
    }

}//end main class
