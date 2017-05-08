package kh.com.omarket;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;

/**
 * Created by daly on 3/20/17.
 */

 public class EditTextValidation {
    private TextInputEditText edtPassword,edtEmail;
    private Button btnShowPassword;
    public EditTextValidation(TextInputEditText edtPassword,Button btnShowPassword,TextInputEditText edtEmail)
    {
        this.btnShowPassword  = btnShowPassword;
        this.edtPassword = edtPassword;
        this.edtEmail = edtEmail;}

    public void setTextWatcher()
    {
        edtPassword.addTextChangedListener(new myTextWatcher(edtPassword));
        edtEmail.addTextChangedListener(new myTextWatcher(edtEmail));
    }
    public boolean submitForm()
    {
        if(!validateEmail()) return false;
        if(!validatePassword())return false;
        return true;
    }
    private boolean validateEmail()
    {
        String email = edtEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            return false;
        }
        return true;
    }
    private static boolean isValidEmail(String Email)
    {
        return !TextUtils.isEmpty(Email) && android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches();

    }
    private boolean validatePassword(){

        if (edtPassword.getText().toString().trim().isEmpty()){
            btnShowPassword.setVisibility(View.INVISIBLE);
            return false;
        }
        else{
            btnShowPassword.setVisibility(View.VISIBLE);
        }
        return true;
    }
    private class myTextWatcher implements TextWatcher
    {
        View view;
        private myTextWatcher(View v)
        {
            this.view = v;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
        @Override
        public void afterTextChanged(Editable s) {
            int id = view.getId();
            if(id == R.id.su_edt_password)
            {
                validatePassword();
            }
            else if(id == R.id.su_edt_mail)
            {
                validateEmail();
            }
        }
    }
}
