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

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnNext;
    private TextInputEditText edtName, edtPhone, edtEmail, edtPassword, edtConfirmPassword;
    private TextView txtAlreadyHaveAcc;
    private LinearLayout layout;
    private FirebaseAuth auth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnNext = (Button) findViewById(R.id.su_btn_next);
        edtName = (TextInputEditText) findViewById(R.id.su_edt_username);
        edtPhone = (TextInputEditText) findViewById(R.id.su_edt_phone);
        edtEmail = (TextInputEditText) findViewById(R.id.su_edt_mail);
        edtPassword = (TextInputEditText)findViewById(R.id.su_edt_password);
        edtConfirmPassword = (TextInputEditText) findViewById(R.id.su_edt_confirm_password);
        txtAlreadyHaveAcc = (TextView) findViewById(R.id.su_txt_already_have_acc);
        layout = (LinearLayout) findViewById(R.id.su_layout);

        btnNext.setOnClickListener(this);
        txtAlreadyHaveAcc.setOnClickListener(this);
        layout.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.su_btn_next) {
            if (!validateForm()){
                return;
            } else {
                try {
                    createAccount(edtEmail.getText().toString(), edtPassword.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), Welcome.class);
                    intent.putExtra("name", edtName.getText().toString());
                    intent.putExtra("phone", edtPhone.getText().toString());
                    intent.putExtra("email", edtEmail.getText().toString());
                    intent.putExtra("password", edtPassword.getText().toString());
                    startActivity(intent);
                    finish();
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } else if(id == R.id.su_txt_already_have_acc) {
            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.start_slide_to_left, R.anim.exit_slide_to_left);
            finish();
        } else if(id == R.id.su_layout) {
            hideKeyBoard();
        }
    }

    public void hideKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
                .getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.start_slide_to_up,R.anim.exit_slide_to_up );
    }

    private void createAccount(String email, String password){
        if (!validateForm()){
            return;
        }
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Create Account Failed- ", Toast.LENGTH_SHORT).show();
                        } else {

                        }
                    }
                });
    }

    private boolean validateForm(){
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
        //Validate Email
        String email = edtEmail.getText().toString();
        if (TextUtils.isEmpty(email)){
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
        if (TextUtils.isEmpty(password)){
            edtPassword.setError("Required");
            valid = false;
        } else if (password.length()<6){
            edtPassword.setError("Password at least 6 Characters");
            valid = false;
        } else {
            edtPassword.setError(null);
        }
        //Validate Confirm Password
        String confirmPassword = edtConfirmPassword.getText().toString();
        if (!TextUtils.equals(password, confirmPassword)){
            edtConfirmPassword.setError("Password not matched");
            valid = false;
        } else if (password.length()<6){
            edtConfirmPassword.setError("Password at least 6 Characters");
            valid = false;
        } else {
            edtConfirmPassword.setError(null);
        }
        return valid;
    }



//    public void AddPhoto(){
//        final String[] choose = {"Take Photo", "Choose from Library", "Cancel"};
//        TextView textViewTitle = new TextView(getApplicationContext());
//        textViewTitle.setText("Add Photo");
//        textViewTitle.setBackgroundColor(Color.rgb(255, 64, 129));
//        textViewTitle.setPadding(10,15,15,10);
//        textViewTitle.setGravity(Gravity.CENTER);
//        textViewTitle.setTextColor(Color.WHITE);
//        textViewTitle.setTextSize(24);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//        builder.setCustomTitle(textViewTitle);
//        builder.setItems(choose, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (choose[which]){
//                    case "Take Photo":
//                        takePhoto();
//                        break;
//                    case "Choose from Library":
//                        pickPhoto();
//                        break;
//                    case "Cancel":
//                        dialog.dismiss();
//                        break;
//                }
//            }
//        });
//        builder.show();
//    }
//
//    private static final int REQUEST_CAMERA = 1;
//    private static final int REQUEST_LIBRARY = 2;
//
//    public void takePhoto(){
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent,REQUEST_CAMERA);
//    }
//
//    public void pickPhoto(){
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setType("image/*");
//        startActivityForResult(intent, REQUEST_LIBRARY);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK && data != null) {
//            if (requestCode == 2) {
//                try {
//                    Uri uri = data.getData();
//                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                }   //end catch
//            }   //end if requestCode ==2
//            else if (requestCode == 1) {
//                try {
//                    Bundle bundle = data.getExtras();
//                    Bitmap bitmap = (Bitmap) bundle.get("data");
//                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                }   //end catch
//            }   //end else if
//
//            Snackbar snackbar = Snackbar.make(layout, "Limited Photos Add", Snackbar.LENGTH_LONG);
//            snackbar.show();
//        }
//    }
//
//    private void updateUserProfile(String displayName, String photoUrl){
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
//                .setDisplayName(displayName).setPhotoUri(Uri.parse(photoUrl)).build();
//        user.updateProfile(profileUpdate)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(getApplicationContext(), "User profile updated", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//    private void updateUserName(String displayName){
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
//                .setDisplayName(displayName).build();
//        user.updateProfile(profileUpdate)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(getApplicationContext(), "User name updated", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

}//end main class
