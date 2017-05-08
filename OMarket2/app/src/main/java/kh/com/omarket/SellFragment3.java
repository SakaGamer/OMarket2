package kh.com.omarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SellFragment3 extends Fragment implements View.OnClickListener{

    private TextView textView;
    private EditText edtName, edtDescr;
    private Button btnNext;
    private LinearLayout layout;
    private Bundle bundle = new Bundle();
    private DatabaseReference databaseReference;

    public SellFragment3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sell3, container, false);
        textView = (TextView) view.findViewById(R.id.s3_txt_title);
        edtName = (EditText) view.findViewById(R.id.s3_edt_pname);
        edtDescr = (EditText) view.findViewById(R.id.s3_edt_description);
        btnNext = (Button) view.findViewById(R.id.s3_btn_next);
        layout = (LinearLayout) view.findViewById(R.id.s3_layout);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        btnNext.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //String s = bundle.getString("selectedCategory");
        //textView.setText(bundle.getString("selectedCategory"));

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.s3_btn_next){
            Product product = new Product();
            product.name = edtName.getText().toString();
            product.description = edtDescr.getText().toString();
            databaseReference.child("products").push().setValue(product);
            Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
            onLowMemory();
        }
    }
}
