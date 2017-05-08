package kh.com.omarket;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtAccName, txtLogOut, txtEmail;
    private ImageView imgProfile;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private RecyclerView recyclerView;
    private String pf;
    private static Map<String, String> mapUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeader = navigationView.getHeaderView(0);
        imgProfile = (ImageView) navHeader.findViewById(R.id.nav_header_img_profile);
        txtAccName = (TextView) navHeader.findViewById(R.id.nav_header_txt_acc_name);
        txtLogOut = (TextView) navHeader.findViewById(R.id.nav_header_txt_log_out);
        txtEmail = (TextView) navHeader.findViewById(R.id.nav_header_txt_email);

        ///////////////////////////////////////////////////////////////////////
        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
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
//                    databaseReference.child("users").orderByChild("email").equalTo(user.getEmail())
//                            .addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    Map<String, String> map = (HashMap<String, String>) dataSnapshot.getValue();
//                                    String data = map.values().toString();
//                                    data = data.substring(2, data.length()-2);
//                                    mapUser = stringToMap(data);
//                                    Toast.makeText(getApplicationContext(),mapUser.get("profile"), Toast.LENGTH_LONG).show();
//                                    byte[] decode = Base64.decode(mapUser.get("profile"), Base64.DEFAULT);
//                                    Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
//                                    imgProfile.setImageBitmap(bitmap);
//                                    if (txtEmail.getVisibility() == View.GONE){
//                                        txtEmail.setVisibility(View.VISIBLE);
//                                    }
//                                    txtAccName.setText(mapUser.get("username"));
//                                }
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
                    //Uri photoUrl = user.getPhotoUrl();
                   // Toast.makeText(getApplicationContext(), user.getPhotoUrl().toString()+ " signed in "+user.getEmail(), Toast.LENGTH_SHORT).show();
                  //  imgProfile.setImageURI(photoUrl);
                    String[] dataUser = getIntent().getStringArrayExtra("user");
//                    updateUserName(dataUser[0]);
                    if (txtEmail.getVisibility() == View.GONE){
                        txtEmail.setVisibility(View.VISIBLE);
                    }
                    txtEmail.setText(user.getEmail());
                    txtAccName.setText(user.getDisplayName());
                    txtLogOut.setText("Log- Out");
                    txtLogOut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            signOut();
                            Toast.makeText(getApplicationContext(), "Signed Out", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //user is singed out
                    imgProfile.setImageResource(R.drawable.user_profile);
                    txtAccName.setText(R.string.sign_in);
                    txtLogOut.setText(R.string.sign_up);
                    txtEmail.setVisibility(View.GONE);
                    txtLogOut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                        }
                    });
                    txtAccName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    });
                }
                //updateUI(user);
                drawer.closeDrawer(GravityCompat.START);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth.addAuthStateListener(authStateListener);

        AdapterProductCard adapterForCardView = new AdapterProductCard(
                Product.class,
                R.layout.item_card_view,
                AdapterProductCard.ProductViewHolder.class,
                databaseReference.child("products"));
        recyclerView.setAdapter(adapterForCardView);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.navHome) {

        } else if (id == R.id.navSell) {
            startActivity(new Intent(getApplicationContext(),SellActivity.class));
            overridePendingTransition(R.anim.start_slide_to_left,R.anim.exit_slide_to_left);
        } else if (id == R.id.navFavorite) {

        } else if (id == R.id.navMyProduct) {
            startActivity(new Intent(getApplicationContext(), MyProductActivity.class));
        } else if (id == R.id.navFeedback) {

        } else if (id == R.id.navAbout) {
            startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.main_content_layout, new FragmentRegistration()).addToBackStack(null).commit();
        } else if (id == R.id.navSetting){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateUserProfile(String displayName, String photoUrl){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName).setPhotoUri(Uri.parse(photoUrl)).build();
        user.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "User profile updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUserName(String displayName){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName).build();
        user.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "User name updated", Toast.LENGTH_SHORT).show();
                        } else {Toast.makeText(getApplicationContext(), "User name update fail", Toast.LENGTH_SHORT).show();}
                    }
                });
    }

    private void signOut(){
        auth.signOut();
        //Signed out
    }

//    private Map<String, String> stringToMap(String str){
//        String[] tokens = str.split(",");
//        Map<String, String> map = new HashMap<>();
//        for (String pair : tokens ){
//            String[] entry = pair.split("=");
//            map.put(entry[0].trim().toString(), entry[1].trim().toString());
//        }
//        return map;
//    }

    public void pFragment(int idSource , String tagFragment, Fragment newFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.start_slide_to_down, R.anim.exit_slide_to_down);
        fragmentTransaction.replace(idSource,newFragment);
        fragmentTransaction.addToBackStack(tagFragment);
        fragmentTransaction.commit();
    }

    
}//end main class