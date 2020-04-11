package kh.com.omarket.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MyProductFragment extends Fragment {

    private TextView textView;
    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private ProductAdapter adapter;
    private List<Product> products = new ArrayList<>();

    public MyProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_product, container, false);
        textView = (TextView) rootView.findViewById(R.id.mp_txt);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.mp_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    //user is signed in
                    adapter = new ProductAdapter(products);
                    recyclerView.setAdapter(adapter);
                    textView.setVisibility(View.INVISIBLE);
                } else {
                    //user is singed out
                    textView.setVisibility(View.VISIBLE);
                }
            }
        };
        return rootView;
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
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
        textView.setText(R.string.no_data);
    }
}
