package kh.com.omarket;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.LruCache;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import kh.com.omarket.model.Product;

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
        txtContact3 = (TextView) findViewById(R.id.dt_txt_contact3);
        txtContact4 = (TextView) findViewById(R.id.dt_txt_contact4);
        thumbnail = (NetworkImageView) findViewById(R.id.dt_img_thumbnail);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            txtContact.setText(user.getEmail());
        }
        Gson gson = new Gson();
        String data = getIntent().getStringExtra("data");
        product = gson.fromJson(data, Product.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //txtCategory.setText(product.getCategory());

        txtDescr.setText(getIntent().getStringExtra("subCategory"));
        txtTitle.setText(getIntent().getStringExtra("name"));
        txtPrice.setText(getIntent().getStringExtra("price"));
        txtLocation.setText(getIntent().getStringExtra("location"));
        txtDescr.setText(getIntent().getStringExtra("descr"));
        txtContact2.setText(getIntent().getStringExtra("phone"));
    }

    private void loadImageFromServer(String imageUrl) {
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ImageLoader imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
        thumbnail.setImageUrl(imageUrl, imageLoader);
        AppSingleTon.getInstance(getApplicationContext()).getImageLoader();
    }
}
