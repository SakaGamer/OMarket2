package kh.com.omarket.CardView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saka on 29-Apr-17.
 */

public class FirebaseHelper {

    private DatabaseReference databaseReference;
    private Boolean saved = false;
    private List<Product> products = new ArrayList<>();

    //  constructor
    public FirebaseHelper(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    //  write if not null
    public Boolean save (Product product){
        if (product == null){
            saved = false;
        } else {
            try{
                databaseReference.child("products").push().setValue(product);
                saved = true;
            }catch(DatabaseException e){
                e.printStackTrace();
                saved = false;
            }
        }
        return saved;
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return products;
    }

    private int getItemIndex(Product product){
        int index = -1;
        for (int i = 0; i < products.size(); i++){
            if (products.get(i).equals(product.getKey())){
                index = i;
                break;
            }
        }
        return index;
    }
}
