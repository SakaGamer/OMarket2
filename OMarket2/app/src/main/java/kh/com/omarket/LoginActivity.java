package kh.com.omarket;

import android.content.Intent;
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

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn ;
    private TextInputEditText edtEmail,edtPassword;
    private TextView txtTitle, txtDontHaveAcc;
    private LinearLayout layout;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn = (Button) findViewById(R.id.li_btn_login);
        edtEmail = (TextInputEditText) findViewById(R.id.li_edt_mail);
        edtPassword = (TextInputEditText)findViewById(R.id.li_edt_password);
        txtDontHaveAcc = (TextView)findViewById(R.id.li_txt_dont_have_acc);
        txtTitle = (TextView) findViewById(R.id.li_txt_title);
        layout = (LinearLayout)findViewById(R.id.li_layout);

        btn.setOnClickListener(this);
        txtDontHaveAcc.setOnClickListener(this);
        layout.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    //user is signed in
                    Log.d("StateListener", "onAuthStateChanged:signed_in"+user.getUid());
                } else {
                    //user is singed out
                    Log.d("StateListener", "onAuthStateChanged:sign_out");
                }
                //updateUI(user);
            }
        };

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.li_btn_login) {
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();
            if (!validateForm()){
                return;
            }
            signIn(email, password);
            finish();
        } else if(id == R.id.li_layout) {
            hideKeyBoard();
        } else if (id == R.id.li_txt_dont_have_acc) {
            startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            overridePendingTransition(R.anim.start_slide_to_right,R.anim.exit_slide_to_right);
            finish();
        }
    }//end OnClick

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.start_slide_to_right,R.anim.exit_slide_to_right);
    }

    public void hideKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager)LoginActivity.this.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

    private void signIn(String email, String password){
        if (!validateForm()){ return; }
        Toast.makeText(getApplicationContext(), "start sign in", Toast.LENGTH_SHORT).show();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication Successful", Toast.LENGTH_SHORT).show();
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
