package kh.com.omarket;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Welcome extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private Map<String, String> map = new HashMap<>();
    private ValueEventListener valueEventListener;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        TextView textView = (TextView) findViewById(R.id.wc_txt_welcome);
        String name = getIntent().getStringExtra("name");
        textView.setText("Welcome " + name);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> newMap = (HashMap<String, String>) dataSnapshot.getValue();
                if (dataSnapshot.getValue() == null) {
                    index = 0;
                    return;
                }
                String data = newMap.values().toString();
                data = data.substring(2, data.length() - 2);
                map = stringToMap(data);
                index = Integer.valueOf(map.get("userId"));
                Toast.makeText(getApplicationContext(), map.get("userId") + index, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        final int SPLASH_DISPLAY_LENGTH = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = databaseReference.child("users").orderByChild("userId").limitToLast(1);
        query.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        User user = new User(String.valueOf(index + 1), getIntent().getStringExtra("name"),
                getIntent().getStringExtra("phone"), getIntent().getStringExtra("email"),
                getIntent().getStringExtra("password"), "null");
        updateUserName(user.getName());
        databaseReference.child("users").push().setValue(user);
        Toast.makeText(getApplicationContext(), index + "-inserted", Toast.LENGTH_SHORT).show();
    }

    private void updateUserName(String displayName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName).build();
        if (user != null) {
            user.updateProfile(profileUpdate);
        }
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getApplicationContext(), "User name updated", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }

    private Map<String, String> stringToMap(String str) {
        String[] tokens = str.split(",");
        Map<String, String> map = new HashMap<>();
        for (String pair : tokens) {
            String[] entry = pair.split("=");
            map.put(entry[0].trim(), entry[1].trim());
        }
        return map;
    }
}
