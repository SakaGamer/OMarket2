package kh.com.omarket.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import kh.com.omarket.R;
import kh.com.omarket.adapters.ProductAdapter;
import kh.com.omarket.models.Product;

public class MyProductActivity extends AppCompatActivity {

    private ProductAdapter adapter;
    private List<Product> products = new ArrayList<>();
    private TextView textView;
    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product);

        textView = findViewById(R.id.mp_txt);
        recyclerView = findViewById(R.id.mp_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProductAdapter(products);

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user is signed in
                    recyclerView.setAdapter(adapter);
                    textView.setVisibility(View.INVISIBLE);
                } else {
                    //user is singed out
                    textView.setVisibility(View.VISIBLE);
                }
            }
        };

        getMyProduct();
    }

    private void getMyProduct() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("USER-PRODUCTS").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        products.add(snapshot.getValue(Product.class));
                    }
                    adapter.setProducts(products);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
        textView.setText(R.string.no_data);
    }

//    @Override
//    public void onRecyclerItemClick(View v, int position) {
//        Product product = adapter.getItem(position);
//        AppSingleTon.getInstance().setProduct(product);
//        startActivity(new Intent(getApplicationContext(),
//                ProductDetailActivity.class));
//    }
}
