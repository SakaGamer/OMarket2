//package kh.com.omarket;
//
//import android.app.Activity;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Base64;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.util.ArrayList;
//
//import kh.com.omarket.model.FirebaseServer;
//import kh.com.omarket.model.Product;
//
//public class test extends AppCompatActivity implements View.OnClickListener {
//
//    private static final int READ_STORAGE_PERMISSION = 4000;
//    private static final int LIMIT = 1;
//    private static final int REQUEST_CAMERA = 1;
//    //private static final int REQUEST_LIBRARY = 2;
//
//    private LinearLayout backArrow;
//    private ImageView imageDefault;
//    private TextView txImageSelects;
//    private TextView txtPost;
//    private int selectedImage = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//
//        txtPost = (TextView) findViewById(R.id.toolbar_txt_add);
//        backArrow = (LinearLayout) findViewById(R.id.toolbar_back_arrow_layout);
//        txtPost.setVisibility(View.GONE);
//        txtPost.setOnClickListener(this);
//        backArrow.setOnClickListener(this);
//
//        txImageSelects = (TextView) findViewById(R.id.s4_txt_add_image);
//        imageDefault = (ImageView) findViewById(R.id.s4_image_default);
//        imageDefault.setOnClickListener(this);
//    }
//
//    public void addPhoto() {
//        final String[] choose = getResources().getStringArray(R.array.choose_image_options);
//        TextView title = new TextView(getApplicationContext());
//        title.setText(R.string.add_photo);
//        title.setBackgroundColor(Color.rgb(255, 64, 129));
//        title.setPadding(10, 15, 15, 10);
//        title.setGravity(Gravity.CENTER);
//        title.setTextColor(Color.WHITE);
//        title.setTextSize(18);
//
//        // build dialog
//        AlertDialog.Builder builder = new AlertDialog.Builder(test.this);
//        builder.setCustomTitle(title);
//        builder.setItems(choose, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (choose[which]) {
//                    case "Take Photo":
//                        takePhoto();
//                        break;
//                    case "Choose from Library":
//                        pickPhoto();
//                        break;
//                }
//            }
//        });
//        // show dialog
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
//    public void takePhoto() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, REQUEST_CAMERA);
//    }
//
//    public void pickPhoto() {
//        // check version sdk
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (!Helper.checkPermissionForExternalStorage(test.this)) {
//                Helper.requestStoragePermission(test.this, READ_STORAGE_PERMISSION);
//            }
//            // opining custom gallery
//            Intent intent = new Intent(test.this, AlbumSelectActivity.class);
//            intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, LIMIT);
//            startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK && data != null) {
//            if (requestCode == ConstantsCustomGallery.REQUEST_CODE) {
//                //The array list has the image paths of the selected images
//                try {
//                    ArrayList<String> imagesPath = data.getStringArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
//                    Toast.makeText(getApplicationContext(), "returned image size " + imagesPath.size(),
//                            Toast.LENGTH_SHORT).show();
//                    for (int i = 0; i < imagesPath.size(); i++) {
//                        Uri uri = Uri.fromFile(new File(imagesPath.get(i)));
//                        Glide.with(this).load(uri.toString())
//                                .placeholder(R.color.colorPrimary)
//                                .override(400, 400)
//                                .crossFade()
//                                .centerCrop()
//                                .into(imageDefault);
//                    }
//                    // update ui
//                    imageDefault.setVisibility(View.VISIBLE);
//                    imageDefault.setDrawingCacheEnabled(true);
//                    txImageSelects.setVisibility(View.GONE);
//                    txtPost.setVisibility(View.VISIBLE);
//                    txtPost.setText(R.string.sell);
//                    selectedImage = imagesPath.size();
//                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    Log.d("onActivityResult:", "Error loading image from library");
//                } //end catch
//            } else if (requestCode == REQUEST_CAMERA) {
//                try {
//                    Bundle bundle = data.getExtras();
//                    Bitmap bitmap = (Bitmap) bundle.get("data");
//                    bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
//                    imageDefault.setImageBitmap(bitmap);
//                    txtPost.setVisibility(View.VISIBLE);
//                    txImageSelects.setVisibility(View.GONE);
//                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    Log.d("onActivityResult:", "Error loading image from camera");
//                } // end catch
//            }// end else if
//        }
//    } // end onActivityResult
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        if (id == R.id.toolbar_back_arrow_layout) {
//            onBackPressed();
//        } else if (id == R.id.s4_image_default) {
//            addPhoto();
//        } else if (id == R.id.toolbar_txt_add) {
//            if (selectedImage > 0) {
//                String userId = FirebaseServer.getCurrentUid();
//                String key = FirebaseServer.getNewPushKey();
//                // create new product to post
//                Product product = new Product(userId,
//                        getIntent().getStringExtra("category"), "null",
//                        getIntent().getStringExtra("name"),
//                        getIntent().getStringExtra("descr"),
//                        getIntent().getStringExtra("location"),
//                        getIntent().getStringExtra("price"),
//                        imageToString(), key);
//                FirebaseServer.writeNewProduct(product, key);
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                finish();
//            }
//        }
//    }
//
//    public String imageToString() {
//        imageDefault.setDrawingCacheEnabled(true);
//        imageDefault.buildDrawingCache();
//        Bitmap bitmap = imageDefault.getDrawingCache();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//        return Base64.encodeToString(data, Base64.DEFAULT);
//    }
//}