package kh.com.omarket;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SellActivity4 extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgA;
    private ImageView imgB;
    private ImageView imgC;
    private ImageView imgD;
    private ImageView imgE;
    private ImageView imgF;
    private LinearLayout layout;
    private ImageView[] images = {imgA, imgB, imgC, imgD, imgE, imgF};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell4);

        imgA = (ImageView) findViewById(R.id.s4_img_a);
        imgB = (ImageView) findViewById(R.id.s4_img_b);
        imgC = (ImageView) findViewById(R.id.s4_img_c);
        imgD = (ImageView) findViewById(R.id.s4_img_d);
        imgE = (ImageView) findViewById(R.id.s4_img_e);
        imgF = (ImageView) findViewById(R.id.s4_img_f);
        layout = (LinearLayout) findViewById(R.id.s4_layout);

//        for (int i = 0; i <= images.length; i++) {
//            //images[i + 1].setVisibility(View.GONE);
//            images[i].setOnClickListener(this);
//        }
        imgA.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.s4_img_a) {
            AddPhoto();
        }
    }

    public void AddPhoto() {
        final String[] choose = getResources().getStringArray(R.array.choose_image_options);
        TextView title = new TextView(getApplicationContext());
        title.setText(R.string.add_photo);
        title.setBackgroundColor(Color.rgb(255, 64, 129));
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(18);

        AlertDialog.Builder builder = new AlertDialog.Builder(SellActivity4.this);
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

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_LIBRARY = 2;

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_LIBRARY) {
                try {
                    Uri uri = data.getData();
                    imgA.setImageURI(uri);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }   //end catch
            }   //end if requestCode ==2
            else if (requestCode == REQUEST_CAMERA) {
                try {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    bitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, true);
                    imgA.setImageBitmap(bitmap);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }   //end catch
            }   //end else if

            Snackbar snackbar = Snackbar.make(layout, "Limited Photos Add", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
