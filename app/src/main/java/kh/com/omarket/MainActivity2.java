package kh.com.omarket;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

import kh.com.omarket.CardView.AdapterProductCard;
import kh.com.omarket.CardView.Product;

public class MainActivity2 extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private TextView txtAccName, txtLogOut, txtEmail;
    private ImageView imgProfile;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private RecyclerView recyclerView;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
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
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    //user is signed in
                    if (txtEmail.getVisibility() == View.GONE){
                        txtEmail.setVisibility(View.VISIBLE);
                    }
                    txtEmail.setText(user.getEmail());
                    txtAccName.setText(user.getDisplayName());
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
                //updateUI(user);
                drawer.closeDrawer(GravityCompat.START);
            }
        };

        AdapterProductCard adapterForCardView = new AdapterProductCard(
                Product.class,
                R.layout.item_card_view,
                AdapterProductCard.ProductViewHolder.class,
                databaseReference.child("products").limitToFirst(100));
        recyclerView.setAdapter(adapterForCardView);
        adapterForCardView.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getClass() == MainActivity2.class){
            navigationView.setCheckedItem(R.id.nav_home);
        }
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }

    private void signOut(){
        auth.signOut();
        //Signed out
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
        int id = item.getItemId();
        if(id == R.id.nav_sell) {
            startActivity(new Intent(getApplicationContext(), SellActivity1.class));
            overridePendingTransition(R.anim.start_slide_to_left,R.anim.exit_slide_to_left);
        } else if (id == R.id.nav_favorite) {
            startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
        } else if (id == R.id.nav_my_product) {
            startActivity(new Intent(getApplicationContext(), MyProductActivity.class));
        } else if (id == R.id.nav_about) {
            //startActivity(new Intent(getApplicationContext(), AboutActivity.class));
        } else if (id == R.id.nav_settings){
            if (Locale.getDefault() == Locale.ENGLISH ){
                changeToLocale("km");
            } else {
                changeToLocale("en");
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeToLocale(String local){
        Locale locale = new Locale(local);
        Locale.setDefault(Locale.ENGLISH);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getApplicationContext().getResources().updateConfiguration(configuration,
                getApplicationContext().getResources().getDisplayMetrics());
        recreate();
    }
}
