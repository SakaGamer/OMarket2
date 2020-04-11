package kh.com.omarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import kh.com.omarket.R;
import kh.com.omarket.adapters.ProductCategoryAdapter;

public class SellActivity1 extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView textView;
    private GridView gridView;
    private String[] category;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        textView = (TextView) findViewById(R.id.sell_txt_no_user);
        gridView = (GridView) findViewById(R.id.sell_grid_category);
        category = getResources().getStringArray(R.array.product_categories);
        gridView.setOnItemClickListener(this);

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    //user is signed in
                    textView.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                } else {
                    gridView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();

        auth.addAuthStateListener(authStateListener);
        ProductCategoryAdapter adapter = new ProductCategoryAdapter(getApplicationContext(),
                category);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getApplicationContext(), SellActivity3.class)
                .putExtra("category", category[position]));
        overridePendingTransition(R.anim.start_slide_to_left, R.anim.exit_slide_to_left);
        finish();
    }
}
