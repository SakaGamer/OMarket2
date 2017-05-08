package kh.com.omarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SellActivity3 extends AppCompatActivity {

    TextView textView;
    EditText edtName, edtDescr;
    Button btnNext;
    LinearLayout layout;
    DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell3);

        textView = (TextView) findViewById(R.id.s3_txt_title);
        edtName = (EditText) findViewById(R.id.s3_edt_pname);
        edtDescr = (EditText) findViewById(R.id.s3_edt_description);
        btnNext = (Button) findViewById(R.id.s3_btn_next);
        layout = (LinearLayout) findViewById(R.id.s3_layout);

        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        textView.setText(getIntent().getStringExtra("selectedCategory"));

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product();
                product.name = edtName.getText().toString();
                product.description = edtDescr.getText().toString();
                databaseReference.child("products").push().setValue(product);
                Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}//end main class
