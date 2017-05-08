package kh.com.omarket;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyProductActivity extends AppCompatActivity {

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
                            databaseReference.child("products").orderByChild("email").equalTo(user.getEmail()));
                    recyclerView.setAdapter(adapterForCardView);
                } else {
                    //user is singed out
                    textView.setText(R.string.my_product_error_text);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth.addAuthStateListener(authStateListener);

    }


}   //end main class
