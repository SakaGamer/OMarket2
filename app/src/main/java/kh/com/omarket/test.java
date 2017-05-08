package kh.com.omarket;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import kh.com.omarket.CardView.Product;
import kh.com.omarket.CustomGallery.ConstantsCustomGallery;
import kh.com.omarket.CustomGallery.Helper;
import kh.com.omarket.CustomGallery.activities.AlbumSelectActivity;

public class test extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgA;
    private ImageView imgB;
    private ImageView imgC;
    private ImageView imgD;
    private ImageView imgE;
    private ImageView imgF;
    private ImageView[] img = {imgA, imgB, imgC, imgD, imgE, imgF};

    private static final int READ_STORAGE_PERMISSION = 4000;
    private static final int LIMIT = 6;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_LIBRARY = 2;

    private LinearLayout backArrow;
    //private ImageView imageView;
    //private TextView txImageSelects;
    private TextView txtAdd;
    private int selectedImage = 0;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        imgA = (ImageView) findViewById(R.id.s4_img_a);
        imgB = (ImageView) findViewById(R.id.s4_img_b);
        imgC = (ImageView) findViewById(R.id.s4_img_c);
        imgD = (ImageView) findViewById(R.id.s4_img_d);
        imgE = (ImageView) findViewById(R.id.s4_img_e);
        imgF = (ImageView) findViewById(R.id.s4_img_f);
        txtAdd = (TextView) findViewById(R.id.toolbar_txt_add);
        backArrow = (LinearLayout) findViewById(R.id.toolbar_back_arrow_layout);

        img[0] = imgA;
        img[1] = imgB;
        img[2] = imgC;
        img[3] = imgD;
        img[4] = imgE;
        img[5] = imgF;

        txtAdd.setVisibility(View.GONE);
        txtAdd.setOnClickListener(this);
        backArrow.setOnClickListener(this);

        for (int i = 1; i < img.length; i++) {
            img[i].setVisibility(View.GONE);
            img[i-1].setOnClickListener(this);
        }

        //txImageSelects = (TextView) findViewById(R.id.s4_txt_add_image);
        //imageView = (ImageView) findViewById(R.id.s4_image_default);
        //imageView.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void addPhoto() {
        final String[] choose = getResources().getStringArray(R.array.choose_image_options);
        TextView title = new TextView(getApplicationContext());
        title.setText(R.string.add_photo);
        title.setBackgroundColor(Color.rgb(255, 64, 129));
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(18);

        AlertDialog.Builder builder = new AlertDialog.Builder(test.this);
        builder.setCustomTitle(title);
        builder.setItems(choose, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (choose[which]) {
                    case "Take Photo":
                        takePhoto();
                        break;
                    case "Choose from Library":
                        pickPhoto();
                        break;
                }
            }
        });
        //builder.show();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void pickPhoto() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setType("image/*");
//        startActivityForResult(intent, REQUEST_LIBRARY);
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Helper.checkPermissionForExternalStorage(test.this)) {
                Helper.requestStoragePermission(test.this, READ_STORAGE_PERMISSION);
            }
            // opining custom gallery
            Intent intent = new Intent(test.this, AlbumSelectActivity.class);
            intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, LIMIT);
            startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == ConstantsCustomGallery.REQUEST_CODE) {
                //The array list has the image paths of the selected images
                try {
                    ArrayList<String> images = data.getStringArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
                    Toast.makeText(getApplicationContext(), "returned image size " + images.size(), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < images.size(); i++) {
                        Uri uri = Uri.fromFile(new File(images.get(i)));
                        Glide.with(this).load(uri.toString())
                                .placeholder(R.color.colorPrimary)
                                .override(400, 400)
                                .crossFade()
                                .centerCrop()
                                .into(img[i]);
                        img[i].setVisibility(View.VISIBLE);
                        img[i].setDrawingCacheEnabled(true);
                        img[i+1].setVisibility(View.VISIBLE);
                    }
                    //imageView.setVisibility(View.GONE);
                    //txImageSelects.setVisibility(View.GONE);
                    txtAdd.setVisibility(View.VISIBLE);
                    selectedImage = images.size();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } //end catch
            } else if (requestCode == REQUEST_CAMERA) {
                try {
                    Toast.makeText(getApplicationContext(), "get", Toast.LENGTH_SHORT).show();
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
                    imgA.setImageBitmap(bitmap);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } // end catch
            }
        } // end else if
    } // end onActivityResult

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.toolbar_back_arrow_layout) {
            onBackPressed();
        } else if (id == R.id.toolbar_txt_add) {
            if (selectedImage > 0) {
                String encodedStringImage = null;
                for (int i = 0; i < selectedImage; i++) {
                    img[i].setDrawingCacheEnabled(true);
                    img[i].buildDrawingCache();
                    Bitmap bitmap = img[i].getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    encodedStringImage = Base64.encodeToString(data, Base64.DEFAULT);
                    //Toast.makeText(getApplicationContext(), dd[0], Toast.LENGTH_LONG).show();
                }
                String userId = auth.getCurrentUser().getUid();
                String key = databaseReference.child("products").push().getKey();
                Product product = new Product(userId,
                        getIntent().getStringExtra("category"), "null",
                        getIntent().getStringExtra("name"),
                        getIntent().getStringExtra("descr"),
                        getIntent().getStringExtra("location"),
                        getIntent().getStringExtra("price"),
                        encodedStringImage, key);

                databaseReference.child("products").child(key).setValue(product);
                startActivity(new Intent(getApplicationContext(), MainActivity2.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Select at lease 1 photo !!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.github:
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/myinnos")));
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

