package kh.com.omarket;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.app.Activity.RESULT_OK;

public class FragmentRegistration extends Fragment implements View.OnClickListener {

    private Button btnNext;
    private ImageView imgPf;
    private TextInputEditText edtUsername,edtEmail,edtPassword,edtPhone;
    private TextView txtAlreadyHaveAcc,txtTitle;
    private LinearLayout layout;
    private Button btnShowPassword;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_registration, container, false);

        btnNext = (Button) view.findViewById(R.id.su_btn_next);
        //edtUsername = (TextInputEditText) view.findViewById(R.id.su_edt_username);
        edtEmail = (TextInputEditText) view.findViewById(R.id.su_edt_mail);
        edtPassword = (TextInputEditText) view.findViewById(R.id.su_edt_password);
       // edtPhone = (TextInputEditText) view.findViewById(R.id.su_edt_phone);
        txtAlreadyHaveAcc = (TextView) view.findViewById(R.id.su_txt_already_have_acc);
        //txtTitle = (TextView) view.findViewById(R.id.su_txt_title);
        layout = (LinearLayout) view.findViewById(R.id.su_layout);

        btnNext.setOnClickListener(this);
        txtAlreadyHaveAcc.setOnClickListener(this);
        txtTitle.setOnClickListener(this);
        layout.setOnClickListener(this);
        btnShowPassword.setOnClickListener(this);
        imgPf.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    //user is signed in
                    Log.d("StateListener", "onAuthStateChanged:signed_in"+user.getUid());
                    txtTitle.setText("Signed In.. "+user.getUid());
                    Toast.makeText(getContext(), user.getUid().toString(), Toast.LENGTH_LONG).show();
                } else {
                    //user is singed out
                    Log.d("StateListener", "onAuthStateChanged:sign_out");
                }
                //updateUI(user);

            }
        };

        //Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        return view;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if(id==R.id.su_btn_next) {
            if (!validateForm()){
                //Snackbar.make(v, "Fill all above !!!", Snackbar.LENGTH_LONG).show();
                return;
            } else {
                Toast.makeText(getContext(),"Created"+edtEmail.getText().toString()+"/"
                        +edtPassword.getText().toString(),Toast.LENGTH_SHORT).show();
                User user = new User("12", edtUsername.getText().toString(),
                        edtPhone.getText().toString(), edtEmail.getText().toString(),
                        edtPassword.getText().toString(),"pf");
                createAccount(edtEmail.getText().toString(), edtPassword.getText().toString());
                databaseReference.child("users").child("34").setValue(user);
                //overridePendingTransition(R.animator.start_slide_to_left, R.animator.exit_slide_to_left);
                onLowMemory();
            }
        } else if(id == R.id.su_txt_already_have_acc) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            //overridePendingTransition(R.animator.start_slide_to_left, R.animator.exit_slide_to_left);
        } else if(id == R.id.su_layout) {
            //hideKeyBoard();
        }

    }


    private void createAccount(String email, String password){
        if (!validateForm()){
            return;
        }
        Toast.makeText(getContext(), "start create acc ", Toast.LENGTH_SHORT).show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(getContext(), "Authentication Failed-- ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Authentication Success-- ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm(){
        boolean valid = true;

        //Validate Username
        String name = edtUsername.getText().toString();
        if (TextUtils.isEmpty(name)){
            edtUsername.setError("Required");
            valid = false;
        } else {
            edtUsername.setError(null);
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

        return valid;
    }

    public void AddPhoto(){
        final String[] choose = {"Take Photo", "Choose from Library", "Cancel"};
        TextView textViewTitle = new TextView(getContext());
        textViewTitle.setText("Add Photo");
        textViewTitle.setBackgroundColor(Color.rgb(255, 64, 129));
        textViewTitle.setPadding(10,15,15,10);
        textViewTitle.setGravity(Gravity.CENTER);
        textViewTitle.setTextColor(Color.WHITE);
        textViewTitle.setTextSize(24);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                    imgPf.setImageURI(uri);
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }   //end catch
            }   //end if requestCode ==2
            else if (requestCode == 1) {
                try {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    imgPf.setImageBitmap(bitmap);
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }   //end catch
            }   //end else if
            Snackbar snackbar = Snackbar.make(layout, "Limited Photos Add", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }


}//end main class
