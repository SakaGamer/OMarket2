package kh.com.omarket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import kh.com.omarket.models.Product;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView txtTitle;
    private TextView txtDescr;
    private TextView txtLocation;
    private TextView txtCategory;
    private TextView txtPrice;
    private TextView txtContact;
    private TextView txtContact2;
    private NetworkImageView thumbnail;
    private Product product;

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
        thumbnail = (NetworkImageView) findViewById(R.id.dt_img_thumbnail);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            txtContact.setText(user.getEmail());
        }
        Gson gson = new Gson();
        product = gson.fromJson("data", Product.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        txtCategory.setText(product.getCategory());
        txtDescr.setText(product.getDescription());
        txtTitle.setText(product.getName());
        txtPrice.setText(product.getPrice());
        txtLocation.setText(product.getLocation());
    }
}
