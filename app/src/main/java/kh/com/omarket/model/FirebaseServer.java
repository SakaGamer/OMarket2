package kh.com.omarket.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Saka on 12-May-17.
 */

public class FirebaseServer {

    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public static boolean isLoggedIn() {
        if (auth.getCurrentUser() != null) {
            return true;
        } else {
            return false;
        }
    }

    public static String getCurrentUid() {
        if (auth.getCurrentUser() != null) {
            return auth.getCurrentUser().getUid();
        } else {
            return null;
        }
    }

    public static String getNewProductPushKey() {
        return databaseReference.child("products").push().getKey();
    }

    public static void writeNewProduct(Product product, String key) {
        try {
            databaseReference.child("products").child(key).setValue(product)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("PostNewProduct:", "post new product successful");
                            } else {
                                Log.d("PostNewProduct:", "post new product fail");
                            }
                        }
                    });
        } catch (Exception e) {
            Log.d("onWriteNewProduct", "Fail to write new product...");
        }
    }

    public static boolean loginUser(String email, String password) {
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("LogIn:", "Log in successful");
                    } else {
                        Log.d("LogIn:", "Log in failed");
                    }
                }
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean createNewUser(String email, String password) {
        try {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("SignUp:", "Create new user success");
                            } else {
                                Log.d("SignUp:", "Failed to create new user...");
                            }
                        }
                    });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Query getRecentProduct() {
        try {
            Log.d("onGetRecentPost", "Query to get recent posts...");
            return databaseReference.child("products").limitToLast(50);
        } catch (Exception e) {
            Log.d("onGetRecentPost", "Fail to query recent posts...");
            return null;
        }
    }

    public static Query getMyProduct() {
        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();
            return databaseReference.child("user-products").orderByChild("userId").equalTo(userId);
        } else {
            return null;
        }
    }
}
