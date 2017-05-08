package kh.com.omarket.CustomGallery.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Saka on 07-May-17.
 */

public abstract class CustomGenericAdapter<T> extends BaseAdapter {

    protected ArrayList<T> arrayList;
    protected Context context;
    protected Activity activity;
    protected LayoutInflater layoutInflater;
    protected int size;

    public CustomGenericAdapter(ArrayList<T> arrayList, Context context, Activity activity) {
        this.arrayList = arrayList;
        this.context = context;
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public T getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setLayoutParams(int size){
        this.size = size;
    }

    public void releaseResource(){
        arrayList = null;
        context = null;
        activity = null;
    }
}
