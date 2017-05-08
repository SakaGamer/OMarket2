package kh.com.omarket;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by saka on 4/21/17.
 */

public class RegistrationActivity2 extends AppCompatActivity {

    private TextInputEditText edtName;
    private TextInputEditText edtPhone;
    private Button btn;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);

        edtName = (TextInputEditText) findViewById(R.id.su2_edt_username);
        edtPhone = (TextInputEditText) findViewById(R.id.su2_edt_phone);
        btn = (Button) findViewById(R.id.su2_btn_create_acc);
        layout = (LinearLayout) findViewById(R.id.su2_layout);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateForm()){
                    return;
                }
                updateUserName(edtName.getText().toString());
                finish();
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
    }

    private void updateUserName(String displayName){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName).build();
        user.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "User name updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
        if (TextUtils.isEmpty(phone)){
            edtPhone.setError("Required");
            valid = false;
        } else {
            edtPhone.setError(null);
        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //AlertDialog
    }
}
