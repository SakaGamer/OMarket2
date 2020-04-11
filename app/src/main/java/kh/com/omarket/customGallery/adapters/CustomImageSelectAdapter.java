package kh.com.omarket.customGallery.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import kh.com.omarket.customGallery.models.Image;
import kh.com.omarket.R;

/**
 * Created by Saka on 07-May-17.
 */

public class CustomImageSelectAdapter extends CustomGenericAdapter<Image> {

    public CustomImageSelectAdapter(ArrayList<Image> arrayList, Context context, Activity activity) {
        super(arrayList, context, activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.viewholder_image_selected, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_select_img);
            viewHolder.view = convertView.findViewById(R.id.view_alpha);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.getLayoutParams().width = size;
        viewHolder.imageView.getLayoutParams().height = size;
        viewHolder.view.getLayoutParams().height = size;
        viewHolder.view.getLayoutParams().height = size;
        if (arrayList.get(position).getSelected()){
            viewHolder.view.setAlpha(0.5f);
            ((FrameLayout) convertView).setForeground(context.getResources()
                    .getDrawable(R.drawable.ic_checked_white));
        } else {
            viewHolder.view.setAlpha(0.0f);
            ((FrameLayout) convertView).setForeground(null);
        }
        Uri uri = Uri.fromFile(new File(arrayList.get(position).getPath()));
        Glide.with(context)
                .load(uri)
                .into(viewHolder.imageView);

        return convertView;
    }

    private static class ViewHolder{
        ImageView imageView;
        View view;
    }
}
