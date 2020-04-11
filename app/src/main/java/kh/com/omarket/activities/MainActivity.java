package kh.com.omarket.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import kh.com.omarket.R;
import kh.com.omarket.fragments.FavoriteFragment;
import kh.com.omarket.fragments.HomeFragment;
import kh.com.omarket.fragments.MyProductFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private TextView txtAccName, txtLogOut, txtEmail;
    private ImageView imgProfile;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private NavigationView navigationView;

    private static final String PREFERENCE_NAME = "omarket-app";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeader = navigationView.getHeaderView(0);
        imgProfile = navHeader.findViewById(R.id.nav_header_img_profile);
        txtAccName = navHeader.findViewById(R.id.nav_header_txt_acc_name);
        txtLogOut = navHeader.findViewById(R.id.nav_header_txt_log_out);
        txtEmail = navHeader.findViewById(R.id.nav_header_txt_email);

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user is signed in
                    if (txtEmail.getVisibility() == View.GONE) {
                        txtEmail.setVisibility(View.VISIBLE);
                    }
                    SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
                    String username = preferences.getString(KEY_USERNAME, null);
                    txtEmail.setText(user.getEmail());
                    txtAccName.setText(username);
                    txtLogOut.setText(R.string.sign_out);
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
                    txtLogOut.setAllCaps(true);
                    txtEmail.setVisibility(View.GONE);
                    txtLogOut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                            overridePendingTransition(R.anim.start_slide_to_down, R.anim.exit_slide_to_down);
                        }
                    });
                    txtAccName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            overridePendingTransition(R.anim.start_slide_to_up, R.anim.exit_slide_to_up);
                        }
                    });
                }
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        };

        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new HomeFragment()).commit();
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (getClass() == MainActivity.class) {
            navigationView.setCheckedItem(R.id.nav_home);
        }
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }

    private void signOut() {
        auth.signOut();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_sell) {
//            startActivity(new Intent(getApplicationContext(), SellActivity1.class));
//            overridePendingTransition(R.anim.start_slide_to_left, R.anim.exit_slide_to_left);
        } else if (id == R.id.nav_favorite) {
//            startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
            setNavigationView(id, new FavoriteFragment(), "Favorite");
        } else if (id == R.id.nav_my_product) {
//            startActivity(new Intent(getApplicationContext(), MyProductActivity.class));
            setNavigationView(id, new MyProductFragment(), "My Product");
        } else if (id == R.id.nav_about) {
            //startActivity(new Intent(getApplicationContext(), AboutActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setNavigationView(int selectedNav, Fragment fragment, String title){
        if (!navigationView.getMenu().findItem(selectedNav).isChecked()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
            if(getSupportActionBar() != null){
                getSupportActionBar().setTitle(title);
            }
            drawerLayout.closeDrawers();
        }
    }
}
