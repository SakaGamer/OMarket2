package kh.com.omarket.adapters;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import kh.com.omarket.models.Product;

/**
 * Created by Saka on 29-Apr-17.
 */

public class FirebaseHelper {

    private DatabaseReference databaseReference;
    private List<Product> products = new ArrayList<>();

    //  constructor
    public FirebaseHelper(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    //  implement fetch data and fill list
    private void fetchData(DataSnapshot dataSnapshot){
        try {
            products.clear();
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                Product product = ds.getValue(Product.class);
                products.add(product);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //  read then return list
    public List<Product> retrieve(){
        databaseReference.child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
                Log.d("onRetrieve", "retrieve data successful");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("onRetrieve", "retrieve data failed");
            }
        });
        return products;
    }

    private int getItemIndex(Product product){
        int index = -1;
        for (int i = 0; i < products.size(); i++){
            if (products.get(i).toString().equals(product.getKey())) {
                index = i;
                break;
            }
        }
        return index;
    }
}
