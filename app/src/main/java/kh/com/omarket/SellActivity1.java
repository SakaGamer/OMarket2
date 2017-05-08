package kh.com.omarket;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SellActivity1 extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView textView;
    private GridView gridView;
    private String[] category;
//    int[] img = {
//            R.drawable.avatar, R.drawable.chat_2, R.drawable.heart,
//            R.drawable.house, R.drawable.like, R.drawable.monitor,
//            R.drawable.settings, R.drawable.shopping_cart
//    };
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s1);

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
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
        AdapterProductGrid adapterProductGrid = new AdapterProductGrid(getApplicationContext(),
                category);
        gridView.setAdapter(adapterProductGrid);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.start_slide_to_right, R.anim.exit_slide_to_right);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getApplicationContext(), SellActivity3.class)
                .putExtra("category", category[position]));
        finish();
    }
}
