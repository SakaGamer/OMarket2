package kh.com.omarket;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnNext;
    private TextInputEditText edtEmail,edtPassword,edtConfirmPassword;
    private TextView txtAlreadyHaveAcc;
    private TextInputEditText edtName;
    private TextInputEditText edtPhone;
    private LinearLayout layout;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private String name;
    private int userLastId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnNext = (Button) findViewById(R.id.su_btn_next);
        edtEmail = (TextInputEditText) findViewById(R.id.su_edt_mail);
        edtPassword = (TextInputEditText) findViewById(R.id.su_edt_password);
        edtConfirmPassword = (TextInputEditText) findViewById(R.id.su_edt_confirm_password);
        txtAlreadyHaveAcc = (TextView) findViewById(R.id.su_txt_already_have_acc);
        edtName = (TextInputEditText) findViewById(R.id.su2_edt_username);
        edtPhone = (TextInputEditText) findViewById(R.id.su2_edt_phone);
        layout = (LinearLayout) findViewById(R.id.su_layout);

        btnNext.setOnClickListener(this);
        txtAlreadyHaveAcc.setOnClickListener(this);
        layout.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    //user is signed in
                    int newId = checkUserInsertedId()+1;
                    String i = Integer.toString(newId);
                    User user2 = new User(i, edtName.getText().toString(), edtPhone.getText().toString(),
                            edtEmail.getText().toString(), edtPassword.getText().toString(), "null");
                    databaseReference.child("users").push().setValue(user2);
                    Toast.makeText(getApplicationContext(), "insert", Toast.LENGTH_SHORT).show();
                } else {
                    //user is singed out
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.su_btn_next) {
            if (!validateForm()){
                return;
            } else {
                createAccount(edtEmail.getText().toString(), edtPassword.getText().toString());

                //createAccount(edtEmail.getText().toString(), edtPassword.getText().toString());
                //signIn(edtEmail.getText().toString(), edtPassword.getText().toString());
                //databaseReference.child("users").push().setValue(user);

                // pass data to MainActivity
//                String[] dataUser = {edtName.getText().toString(), edtPhone.getText().toString(),
//                        edtEmail.getText().toString(), edtPassword.getText().toString()};
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.putExtra("user", dataUser);
//                startActivity(intent);
//                finish();

            }
        } else if(id == R.id.su_txt_already_have_acc) {
            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.start_slide_to_left, R.anim.exit_slide_to_left);
            finish();
        } else if(id == R.id.su_layout) {
            hideKeyboard();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);

    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
                .getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.start_slide_to_down,R.anim.exit_slide_to_down);
    }

    private int checkUserInsertedId(){
        Query query = databaseReference.child("users").orderByChild("userId").limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = (HashMap<String, String>) dataSnapshot.getValue();
                String data = map.values().toString();
                data = data.substring(2, data.length()-2);
                Map<String, String> mapUser = stringToMap(data);
        //        userLastId = Integer.parseInt(mapUser.get("userId"));
                Toast.makeText(getApplicationContext(), "---"+data, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing
            }
        });
        return userLastId;
    }

    private void createAccount(String email, String password){
        if (!validateForm()){ return; }
        Toast.makeText(getApplicationContext(), "start create account ", Toast.LENGTH_SHORT).show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Create Account Failed- ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
        name = edtName.getText().toString();
    }

    private Map<String, String> stringToMap(String str){
        String[] tokens = str.split(",");
        Map<String, String> newMap = new HashMap<>();
        for (String pair : tokens ){
            String[] entry = pair.split("=");
            newMap.put(entry[0].trim().toString(), entry[1].trim().toString());
        }
        return newMap;
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
        if (TextUtils.isEmpty(confirmPassword)){
            edtConfirmPassword.setError("Required");
            valid = false;
        } else if (!TextUtils.equals(password, confirmPassword)){
            edtConfirmPassword.setError("Password not matched");
            valid = false;
        } else {
            edtConfirmPassword.setError(null);
        }
        return valid;
    }

    public void AddPhoto(){
        final String[] choose = {"Take Photo", "Choose from Library", "Cancel"};
        TextView textViewTitle = new TextView(getApplicationContext());
        textViewTitle.setText("Add Photo");
        textViewTitle.setBackgroundColor(Color.rgb(255, 64, 129));
        textViewTitle.setPadding(10,15,15,10);
        textViewTitle.setGravity(Gravity.CENTER);
        textViewTitle.setTextColor(Color.WHITE);
        textViewTitle.setTextSize(24);

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setCustomTitle(textViewTitle);
        builder.setItems(choose, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (choose[which]){
                    case "Take Photo":
                        takePhoto();
                        break;
                    case "Choose from Library":
                        pickPhoto();
                        break;
                    case "Cancel":
                        dialog.dismiss();
                        break;
                }
            }
        });
        builder.show();
    }

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_LIBRARY = 2;

    public void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CAMERA);
    }

    public void pickPhoto(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_LIBRARY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 2) {
                try {
                    Uri uri = data.getData();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }   //end catch
            }   //end if requestCode ==2
            else if (requestCode == 1) {
                try {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }   //end catch
            }   //end else if

            Snackbar snackbar = Snackbar.make(layout, "Limited Photos Add", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }


}//end main class
