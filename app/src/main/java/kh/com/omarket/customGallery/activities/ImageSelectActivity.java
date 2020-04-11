package kh.com.omarket.customGallery.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import kh.com.omarket.customGallery.ConstantsCustomGallery;
import kh.com.omarket.customGallery.adapters.CustomImageSelectAdapter;
import kh.com.omarket.customGallery.models.Image;
import kh.com.omarket.R;

import static android.support.v7.appcompat.R.anim.abc_fade_in;
import static android.support.v7.appcompat.R.anim.abc_fade_out;

public class ImageSelectActivity extends HelperActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private ArrayList<Image> images;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        setView(findViewById(R.id.layout_image_select));

        tvProfile = findViewById(R.id.toolbar_txt_title);
        tvAdd = findViewById(R.id.toolbar_txt_add);
        tvSelectCount = findViewById(R.id.toolbar_txt_count);
        liFinish = findViewById(R.id.toolbar_back_arrow_layout);
        errorDisplay = findViewById(R.id.image_select_txt_error);
        loader = findViewById(R.id.image_select_progress_bar);
        gridView = findViewById(R.id.image_select_grid_view);
        tvProfile.setText(R.string.image_view);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }
        album = intent.getStringExtra(ConstantsCustomGallery.INTENT_EXTRA_ALBUM);
        errorDisplay.setVisibility(View.INVISIBLE);
        gridView.setOnItemClickListener(this);
        liFinish.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ConstantsCustomGallery.PERMISSION_GRANTED: {
                        loadImages();
                        break;
                    }
                    case ConstantsCustomGallery.FETCH_STARTED: {
                        loader.setVisibility(View.VISIBLE);
                        gridView.setVisibility(View.INVISIBLE);
                        break;
                    }
                    case ConstantsCustomGallery.FETCH_COMPLETE: {
                        /*
                        If adapter is null, this implies that the loaded images will be shown
                        for the first time, hence send FETCH_COMPLETED message.
                        However, if adapter has been initialised, this thread was run either
                        due to the activity being restarted or content being changed.
                         */
                        if (adapter == null) {
                            adapter = new CustomImageSelectAdapter(images, getApplicationContext(),
                                    ImageSelectActivity.this);
                            gridView.setAdapter(adapter);
                            loader.setVisibility(View.GONE);
                            gridView.setVisibility(View.VISIBLE);
                            orientationBasedUI(getResources().getConfiguration().orientation);
                        } else {
                            adapter.notifyDataSetChanged();
                            // Some selected images may have been deleted hence update action mode title
                            countSelected = msg.arg1;
                            tvSelectCount.setText(countSelected + " " + getString(R.string.selected));
                            tvSelectCount.setVisibility(View.VISIBLE);
                            tvAdd.setVisibility(View.VISIBLE);
                            tvProfile.setVisibility(View.GONE);
                        } break;
                    }
                    case ConstantsCustomGallery.ERROR: {
                        loader.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.VISIBLE);
                        break;
                    }
                    default: {
                        super.handleMessage(msg);
                    }
                }
            }
        };
        observer = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange) {
                loadImages();
            }
        };
        getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                false, observer);
        checkPermission();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopThread();
        getContentResolver().unregisterContentObserver(observer);
        observer = null;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        images = null;
        if (adapter != null) {
            adapter.releaseResource();
        }
        gridView.setOnItemClickListener(null);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientationBasedUI(newConfig.orientation);
    }

    private void orientationBasedUI(int orientation) {
        final WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        if (adapter != null) {
            int size = orientation == Configuration.ORIENTATION_PORTRAIT ? metrics.widthPixels / 3 : metrics.widthPixels / 5;
            adapter.setLayoutParams(size);
        }
        gridView.setNumColumns(orientation == Configuration.ORIENTATION_PORTRAIT ? 3 : 5);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            default: {
                return false;
            }
        }
    }

    private void toggleSelection(int position) {
        if (!images.get(position).getSelected() && countSelected >= ConstantsCustomGallery.limit) {
            Toast.makeText(getApplicationContext(),
                    String.format(getString(R.string.limit_exceeded), ConstantsCustomGallery.limit),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        images.get(position).setSelected(!images.get(position).getSelected());
        if (images.get(position).getSelected()) {
            countSelected++;
        } else {
            countSelected--;
        }
        adapter.notifyDataSetChanged();
    }

    private void deselectAll() {
        tvProfile.setVisibility(View.VISIBLE);
        tvAdd.setVisibility(View.GONE);
        tvSelectCount.setVisibility(View.GONE);
        for (int i = 0, l = images.size(); i < l; i++) {
            images.get(i).setSelected(false);
        }
        countSelected = 0;
        adapter.notifyDataSetChanged();
    }

    private ArrayList<Image> getSelected() {
        ArrayList<Image> selectedImages = new ArrayList<>();
        if (images.size() > 0){
            for (int i = 0; i < images.size(); i++) {
                if (images.get(i).getSelected()) {
                    selectedImages.add(images.get(i));
                }
            }
        }
        return selectedImages;
    }

    private ArrayList<String> getSelectedPath(){
        ArrayList<String> selectedImages = new ArrayList<>();
        if (images.size() > 0){
            for (int i = 0; i < images.size(); i++) {
                if (images.get(i).getSelected()) {
                    selectedImages.add(images.get(i).getPath());
                }
            }
        }
        return selectedImages;
    }

    private void sendIntent() {
        Intent intent = new Intent();
        //intent.putParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES, getSelected());
        intent.putStringArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES, getSelectedPath());
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(abc_fade_in, abc_fade_out);
    }

    private void loadImages() {
        startThread(new ImageLoaderRunnable());
    }

    private class ImageLoaderRunnable implements Runnable {
        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            /*
            If the adapter is null, this is first time this activity's view is
            being shown, hence send FETCH_STARTED message to show progress bar
            while images are loaded from phone
             */
            if (adapter == null) {
                sendMessage(ConstantsCustomGallery.FETCH_STARTED);
            }
            File file;
            HashSet<Long> selectedImages = new HashSet<>();
            if (images != null) {
                Image image;
                for (int i = 0, l = images.size(); i < l; i++) {
                    image = images.get(i);
                    file = new File(image.getPath());
                    if (file.exists() && image.getSelected()) {
                        selectedImages.add(image.getId());
                    }
                }
            }
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =?", new String[]{album}, MediaStore.Images.Media.DATE_ADDED);
            if (cursor == null) {
                sendMessage(ConstantsCustomGallery.ERROR);
                return;
            }
            /*
            In case this runnable is executed to onChange calling loadImages,
            using countSelected variable can result in a race condition. To avoid that,
            tempCountSelected keeps track of number of selected images. On handling
            FETCH_COMPLETED message, countSelected is assigned value of tempCountSelected.
             */
            int tempCountSelected = 0;
            ArrayList<Image> temp = new ArrayList<>(cursor.getCount());
            if (cursor.moveToLast()) {
                do {
                    if (Thread.interrupted()) {
                        return;
                    }
                    long id = cursor.getLong(cursor.getColumnIndex(projection[0]));
                    String name = cursor.getString(cursor.getColumnIndex(projection[1]));
                    String path = cursor.getString(cursor.getColumnIndex(projection[2]));
                    boolean isSelected = selectedImages.contains(id);
                    if (isSelected) {
                        tempCountSelected++;
                    }
                    file = null;
                    try {
                        file = new File(path);
                    } catch (Exception e) {
                        Log.d("Exception : ", e.toString());
                    }
                    if (file.exists()) {
                        temp.add(new Image(id, name, path, isSelected));
                    }
                } while (cursor.moveToPrevious());
            }
            cursor.close();
            if (images == null) {
                images = new ArrayList<>();
            }
            images.clear();
            images.addAll(temp);
            sendMessage(ConstantsCustomGallery.FETCH_COMPLETE, tempCountSelected);
        }
    }

    private void startThread(Runnable runnable) {
        stopThread();
        thread = new Thread(runnable);
        thread.start();
    }

    private void stopThread() {
        if (thread == null || !thread.isAlive()) {
            return;
        }
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(int what) {
        sendMessage(what, 0);
    }

    private void sendMessage(int what, int arg1) {
        if (handler == null) {
            return;
        }
        Message message = handler.obtainMessage();
        message.what = what;
        message.arg1 = arg1;
        message.sendToTarget();
    }

    @Override
    protected void permissionGranted() {
        sendMessage(ConstantsCustomGallery.PERMISSION_GRANTED);
    }

    @Override
    protected void hideViews() {
        loader.setVisibility(View.GONE);
        gridView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.toolbar_back_arrow_layout){
            if (tvSelectCount.getVisibility() == View.VISIBLE) {
                deselectAll();
            } else {
                finish();
                overridePendingTransition(abc_fade_in, abc_fade_out);
            }
        } else if (id == R.id.toolbar_txt_add){
            sendIntent();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toggleSelection(position);
        tvSelectCount.setText(countSelected + " " + getString(R.string.selected));
        tvSelectCount.setVisibility(View.VISIBLE);
        tvAdd.setVisibility(View.VISIBLE);
        tvProfile.setVisibility(View.GONE);
        if (countSelected == 0) {
            tvSelectCount.setVisibility(View.GONE);
            tvAdd.setVisibility(View.GONE);
            tvProfile.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (tvSelectCount.getVisibility() == View.VISIBLE) {
            deselectAll();
        } else {
            super.onBackPressed();
            overridePendingTransition(abc_fade_in, abc_fade_out);
            finish();
        }

    }
}
