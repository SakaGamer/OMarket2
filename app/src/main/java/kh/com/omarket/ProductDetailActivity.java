package kh.com.omarket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kh.com.omarket.CardView.FirebaseHelper;
import kh.com.omarket.CardView.MyAdapter;
import kh.com.omarket.CustomGallery.models.Image;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView txtTitle;
    private TextView txtDescr;
    private TextView txtLocation;
    private TextView txtCategory;
    private TextView txtPrice;
    private TextView txtContact;
    private TextView txtContact2;
    private TextView txtContact3;
    private TextView txtContact4;
    private ImageView thumbnail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        txtTitle = (TextView) findViewById(R.id.dt_txt_title);
        txtPrice = (TextView) findViewById(R.id.dt_txt_price);
        txtDescr = (TextView) findViewById(R.id.dt_txt_descr);
        txtLocation = (TextView) findViewById(R.id.dt_txt_location);
        txtCategory = (TextView) findViewById(R.id.dt_txt_category);
        txtContact = (TextView) findViewById(R.id.dt_txt_contact);
        txtContact2 = (TextView) findViewById(R.id.dt_txt_contact2);
        txtContact3 = (TextView) findViewById(R.id.dt_txt_contact3);
        txtContact4 = (TextView) findViewById(R.id.dt_txt_contact4);
        thumbnail = (ImageView) findViewById(R.id.dt_img_thumbnail);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            txtContact.setText(user.getEmail());
        }
//
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//        FirebaseHelper firebaseHelper = new FirebaseHelper(databaseReference);
//        MyAdapter myAdapter = new MyAdapter(getApplicationContext(),firebaseHelper.retrieve() );

    }

    @Override
    protected void onStart() {
        super.onStart();
        txtCategory.setText(getIntent().getStringExtra("category"));
        //txtDescr.setText(getIntent().getStringExtra("subCategory"));
        txtTitle.setText(getIntent().getStringExtra("name"));
        txtPrice.setText(getIntent().getStringExtra("price"));
        txtLocation.setText(getIntent().getStringExtra("location"));
        txtDescr.setText(getIntent().getStringExtra("descr"));
        txtContact2.setText(getIntent().getStringExtra("phone"));
        if (getIntent().getStringExtra("image") != null){
            byte[] decode = Base64.decode(getIntent().getStringExtra("image"), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
            thumbnail.setImageBitmap(bitmap);
        } else if (getIntent().getStringArrayExtra("images") != null) {
            String[] images = getIntent().getStringArrayExtra("images");
        }

    }
}
