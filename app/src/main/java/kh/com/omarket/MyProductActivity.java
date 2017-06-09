package kh.com.omarket;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import kh.com.omarket.adapter.AdapterProductCard;
import kh.com.omarket.model.Product;

public class MyProductActivity extends AppCompatActivity{

    private TextView textView;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product);

        textView = (TextView) findViewById(R.id.mp_txt);
        recyclerView = (RecyclerView) findViewById(R.id.mp_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    //user is signed in
                    AdapterProductCard adapterForCardView = new AdapterProductCard(
                            Product.class,
                            R.layout.item_card_view,
                            AdapterProductCard.ProductViewHolder.class,
                            databaseReference.child("products").orderByChild("userId").equalTo(auth.getCurrentUser().getUid()));
                    recyclerView.setAdapter(adapterForCardView);
                    textView.setVisibility(View.INVISIBLE);
                } else {
                    //user is singed out
                    textView.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
        textView.setText(R.string.no_data);
    }

    private Map<String, String> stringToMap(String str){
        String[] tokens = str.split(",");
        Map<String, String> map = new HashMap<>();
        for (String pair : tokens ){
            String[] entry = pair.split("=");
            map.put(entry[0].trim(), entry[1].trim());
        }
        return map;
    }

}   //end main class
