package kh.com.omarket.models;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * Created by Saka on 12-May-17.
 */

public class FirebaseServer {

    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private static final String USERS = "USERS";
    private static final String PRODUCTS = "PRODUCTS";
    private static final String USER_PRODUCTS = "USER-PRODUCTS";
    private static final String UID = "uid";
    private static User user;

    public static String getCurrentUid() {
        if (auth.getCurrentUser() != null) {
            return auth.getCurrentUser().getUid();
        } else {
            return null;
        }
    }

    public static String getNewPushKey() {
        return databaseReference.child(PRODUCTS).push().getKey();
    }

    public static User getUser(final String uid){
        databaseReference.child(USERS).child(uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user2 = dataSnapshot.getValue(User.class);
                        if (user2 != null){
                            user = user2;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
        return user;
    }

    public static boolean loggedIn(String inputEmail, String inputPassword) {
        return auth.signInWithEmailAndPassword(inputEmail, inputPassword).isSuccessful();
    }

    public static boolean createNewUser(String email, String password) {
        try {
            auth.createUserWithEmailAndPassword(email, password);
            Log.d("omarket:", "Create new user success");
            return true;
        } catch (Exception e) {
            Log.d("omarket:", "Failed to create new user...");
            return false;
        }
    }

    public static void writeNewUser(String uid, User user) {
        databaseReference.child(USERS).child(uid).setValue(user);
    }

    public static void writeNewProduct(Product product, String key){

    }

    public static void updateMyChildren(Map<String, Object> childUpdate) {
        databaseReference.updateChildren(childUpdate);
    }

    public static Query getRecentProduct() {
        try {
            return databaseReference.child(PRODUCTS).limitToFirst(50);
        } catch (Exception e) {
            return null;
        }
    }

    public static Query getMyProduct() {
        if (auth.getCurrentUser() != null) {
            String uid = auth.getCurrentUser().getUid();
            return databaseReference.child(USER_PRODUCTS).child(uid);
        } else {
            return null;
        }
    }
}
