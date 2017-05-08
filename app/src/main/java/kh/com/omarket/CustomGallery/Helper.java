package kh.com.omarket.CustomGallery;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * Created by Saka on 07-May-17.
 */

public class Helper {

    public static Boolean checkPermissionForExternalStorage(Activity activity){

        int result = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public static Boolean requestStoragePermission(Activity activity, int READ_STORAGE_PERMISSION){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_STORAGE_PERMISSION);
            }
        }
        return false;
    }
}
