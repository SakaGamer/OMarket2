package kh.com.omarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import kh.com.omarket.R;

public class SellActivity3 extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText edtName, edtDescr, edtPrice;
    private Spinner spinnerLocation, spinnerCurrency;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell3);

        Button btnNext = (Button) findViewById(R.id.s3_btn_next);
        LinearLayout layout = (LinearLayout) findViewById(R.id.s3_layout);

        edtName = (TextInputEditText) findViewById(R.id.s3_edt_pname);
        edtDescr = (TextInputEditText) findViewById(R.id.s3_edt_description);
        edtPrice = (TextInputEditText) findViewById(R.id.s3_edt_price);
        spinnerLocation = (Spinner) findViewById(R.id.s3_spinner_location);
        spinnerCurrency = (Spinner) findViewById(R.id.s3_spinner_currency);

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

    private boolean validateForm() {
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
        if (TextUtils.isEmpty(descr)) {
            edtDescr.setError("Required");
            valid = false;
        } else {
            edtDescr.setError(null);
        }
        //Validate Price
        String price = edtPrice.getText().toString();
        if (TextUtils.isEmpty(price)) {
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
        if (id == R.id.s3_layout) {
            InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
                    .getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } else if (id == R.id.s3_btn_next) {
            if (validateForm()) {
                String cate = getIntent().getStringExtra("category");
                startActivity(new Intent(getApplicationContext(), test.class)
                        .putExtra("category", cate)
                        .putExtra("name", edtName.getText().toString())
                        .putExtra("descr", edtDescr.getText().toString())
                        .putExtra("location", spinnerLocation.getSelectedItem().toString())
                        .putExtra("price", edtPrice.getText().toString() + spinnerCurrency.getSelectedItem().toString()));
                overridePendingTransition(R.anim.start_slide_to_left, R.anim.exit_slide_to_left);
            }
        }
    }
}//end main class
