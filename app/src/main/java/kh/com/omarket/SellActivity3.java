package kh.com.omarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kh.com.omarket.CardView.Product;

public class SellActivity3 extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText edtName, edtDescr, edtPrice;
    private Button btnNext;
    private Spinner spinnerLocation, spinnerCurrency;
    private LinearLayout layout;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell3);

        edtName = (TextInputEditText) findViewById(R.id.s3_edt_pname);
        edtDescr = (TextInputEditText) findViewById(R.id.s3_edt_description);
        edtPrice = (TextInputEditText) findViewById(R.id.s3_edt_price);
        btnNext = (Button) findViewById(R.id.s3_btn_next);
        spinnerLocation = (Spinner) findViewById(R.id.s3_spinner_location);
        spinnerCurrency = (Spinner) findViewById(R.id.s3_spinner_currency);
        layout = (LinearLayout) findViewById(R.id.s3_layout);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        layout.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Spinner Location
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.locations, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(arrayAdapter);

        // Spinner Currency
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(adapter);
    }

    private boolean validateForm(){
        boolean valid = true;
        //Validate Product name
        String name = edtName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            edtName.setError("Required");
            valid = false;
        } else {
            edtName.setError(null);
        }
        //Validate Description
        String descr = edtDescr.getText().toString();
        if (TextUtils.isEmpty(descr)){
            edtDescr.setError("Required");
            valid = false;
        } else {
            edtDescr.setError(null);
        }
        //Validate Price
        String price = edtPrice.getText().toString();
        if (TextUtils.isEmpty(price)){
            edtPrice.setError("Required");
            valid = false;
        } else {
            edtPrice.setError(null);
        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.s3_layout){
            InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
                    .getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } else if (id == R.id.s3_btn_next){
//            if (!validateForm()){
//                return;
//            }
//            Product product = new Product(auth.getCurrentUser().getUid(),
//                    getIntent().getStringExtra("category"), "null",
//                    edtName.getText().toString(), edtDescr.getText().toString(),
//                    spinnerLocation.getSelectedItem().toString(), edtPrice.getText().toString()
//                    +spinnerCurrency.getSelectedItem().toString());
//            databaseReference.child("products").push().setValue(product);
//            startActivity(new Intent(getApplicationContext(), MainActivity2.class));
//            finish();
            String cate = getIntent().getStringExtra("category");
            startActivity(new Intent(getApplicationContext(), test.class)
            .putExtra("category", cate)
            .putExtra("name", edtName.getText().toString())
            .putExtra("descr", edtDescr.getText().toString())
            .putExtra("location", spinnerLocation.getSelectedItem().toString())
            .putExtra("price", edtPrice.getText().toString() + spinnerCurrency.getSelectedItem().toString()));
        }
    }
}//end main class
