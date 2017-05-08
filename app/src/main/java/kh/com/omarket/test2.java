package kh.com.omarket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import junit.framework.Test;

import java.io.File;
import java.util.ArrayList;

import kh.com.omarket.CustomGallery.ConstantsCustomGallery;
import kh.com.omarket.CustomGallery.Helper;
import kh.com.omarket.CustomGallery.activities.AlbumSelectActivity;
import kh.com.omarket.CustomGallery.activities.HelperActivity;
import kh.com.omarket.CustomGallery.adapters.CustomImageSelectAdapter;
import kh.com.omarket.CustomGallery.models.Image;

import static android.support.v7.appcompat.R.anim.abc_fade_in;
import static android.support.v7.appcompat.R.anim.abc_fade_out;
import static kh.com.omarket.R.id.image_select_grid_view;
import static kh.com.omarket.R.id.image_select_img;

public class test2 extends AppCompatActivity implements
        AdapterView.OnItemClickListener, View.OnClickListener {

    private ArrayList<String> images;
    private String album;
    private TextView errorDisplay, tvProfile, tvAdd, tvSelectCount;
    private LinearLayout liFinish;
    private ProgressBar loader;
    private GridView gridView;
    private CustomImageSelectAdapter adapter;
    private int countSelected;
    private ContentObserver observer;
    private Handler handler;
    private Thread thread;
    private final String[] projection = new String[]{MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
    private static final int READ_STORAGE_PERMISSION = 4000;
    private static final int LIMIT = 8;

    String[] path;
    Boolean[] thumbnailSelect;
    int[] ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        //setView(findViewById(R.id.layout_image_select));

        tvProfile = (TextView) findViewById(R.id.toolbar_txt_title);
        tvAdd = (TextView) findViewById(R.id.toolbar_txt_add);
        tvSelectCount = (TextView) findViewById(R.id.toolbar_txt_count);
        liFinish = (LinearLayout) findViewById(R.id.toolbar_back_arrow_layout);
        errorDisplay = (TextView) findViewById(R.id.image_select_txt_error);
        loader = (ProgressBar) findViewById(R.id.image_select_progress_bar);
        gridView = (GridView) findViewById(R.id.image_select_grid_view);
        tvProfile.setText(R.string.image_view);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }
        //album = intent.getStringExtra(ConstantsCustomGallery.INTENT_EXTRA_ALBUM);
        //errorDisplay.setVisibility(View.INVISIBLE);
        //gridView.setOnItemClickListener(this);
        //gridView.setVisibility(View.GONE);
        liFinish.setOnClickListener(this);
        //errorDisplay.setOnClickListener(this);
        tvAdd.setOnClickListener(this);

        //////////////////////////////////////////////////

        String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        String orderBy = MediaStore.Images.Media._ID;
        Cursor cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns, null, null, orderBy);
        int image_col_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        this.countSelected = cursor.getCount();
        this.path = new String[this.countSelected];
        ids = new int[countSelected];
        this.thumbnailSelect = new Boolean[this.countSelected];

        for (int i=0; i<this.countSelected; i++){
            cursor.moveToPosition(i);
            ids[i] = cursor.getInt(image_col_index);
            int dataColIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            path[i] = cursor.getString(dataColIndex);
        }

        SelectImageAdapter adapter = new SelectImageAdapter();
        gridView.setAdapter(adapter);
        cursor.close();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ConstantsCustomGallery.REQUEST_CODE &&
//                resultCode == Activity.RESULT_OK && data != null) {
//            //The array list has the image paths of the selected images
//            try {
//                //ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
//                //ArrayList<String>
//                images = data.getStringArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
//                gridView.setAdapter(new SelectImageAdapter());
//                gridView.setVisibility(View.VISIBLE);
//                Toast.makeText(getApplicationContext(), "returned image size " + images.size(), Toast.LENGTH_SHORT).show();
//            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.toolbar_back_arrow_layout) {
            if (tvSelectCount.getVisibility() == View.VISIBLE) {
                //deselectAll();
                onBackPressed();
            } else {
                finish();
                overridePendingTransition(abc_fade_in, abc_fade_out);
            }
        } else if (id == R.id.image_select_txt_error) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Helper.checkPermissionForExternalStorage(test2.this)) {
                    Helper.requestStoragePermission(test2.this, READ_STORAGE_PERMISSION);
                }
            }
            // start open custom gallery
            Intent intent = new Intent(test2.this, AlbumSelectActivity.class);
            intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, LIMIT);
            startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
        } else if (id == R.id.toolbar_txt_add) {
            //sendIntent();
//            if (Build.VERSION.SDK_INT >= 23) {
//                if (!Helper.checkPermissionForExternalStorage(test2.this)) {
//                    Helper.requestStoragePermission(test2.this, READ_STORAGE_PERMISSION);
//                } else {
//                    // opining custom gallery
//                    Intent intent = new Intent(test2.this, AlbumSelectActivity.class);
//                    intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, LIMIT);
//                    startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
//                }
//            } else {
//                Intent intent = new Intent(test2.this, AlbumSelectActivity.class);
//                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, LIMIT);
//                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
//            }
            int length = thumbnailSelect.length;
            int cnt = 0;
            String selectImages = "";
            for (int i=0; i<length; i++){
                if (thumbnailSelect[i]){
                    cnt++;
                    selectImages = selectImages + path[i] +"|";
                }
            }
            if (cnt ==0){
                Toast.makeText(getApplicationContext(),"no image select", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getApplicationContext(), Test.class);
                intent.putExtra("data", selectImages);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    private void setBitmap(final ImageView imageView, final int id){
        new AsyncTask<Void, Void, Bitmap>(){

            @Override
            protected Bitmap doInBackground(Void... params) {

                return MediaStore.Images.Thumbnails.getThumbnail(
                        getApplicationContext().getContentResolver(),
                        id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                imageView.setImageBitmap(bitmap);
            }
        }.execute();
    }

    private class SelectImageAdapter extends BaseAdapter {

        LayoutInflater inflater;

        public SelectImageAdapter() {
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return countSelected;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null){
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_image_select_grid, null);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_select_img);
                viewHolder.view = convertView.findViewById(R.id.view_alpha);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.view.setId(position);
            viewHolder.imageView.setId(position);
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    if (thumbnailSelect[id]){
                        viewHolder.view.setAlpha(0.0f);
                        thumbnailSelect[id] = false;
                    } else {
                        viewHolder.view.setAlpha(0.5f);
                        thumbnailSelect[id] = true;
                    }
                }
            });
            try{
                setBitmap(viewHolder.imageView, ids[position]);
            } catch (Throwable e){
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            //viewHolder.view.setAlpha(thumbnailSelect[position]);
            //viewHolder.id = position;
            return convertView;
        }


//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ImageView imageView;
//            if (convertView == null) {
//                imageView = new ImageView(getApplicationContext());
//                imageView.setLayoutParams(new GridView.LayoutParams(400, 400));
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                imageView.setPadding(4, 4, 4, 4);
//            } else {
//                imageView = (ImageView) convertView;
//            }
//            Uri uri = Uri.fromFile(new File(images.get(position)));
//            imageView.setImageURI(uri);
//            return imageView;
//        }
    }

    private class ViewHolder{
        int id;
        String path;
        ImageView imageView;
        View view;
    }
}
