package kh.com.omarket.CustomGallery.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import kh.com.omarket.CustomGallery.models.AlbumImage;
import kh.com.omarket.R;

/**
 * Created by Saka on 07-May-17.
 */

public class CustomAlbumSelectAdapter extends CustomGenericAdapter<AlbumImage> {

    public CustomAlbumSelectAdapter(ArrayList<AlbumImage> arrayList, Context context,
                                    Activity activity) {
        super(arrayList, context, activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_album_select_grid, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.album_select_img);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.album_select_txt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.getLayoutParams().width = size;
        viewHolder.imageView.getLayoutParams().height = size;
        viewHolder.textView.setText(arrayList.get(position).getName());
        if (arrayList.get(position).getName().equals("Take Photo")){
            Glide.with(context).load(arrayList.get(position).getCover())
                    .placeholder(R.color.colorPrimary)
                    .override(200, 200)
                    .crossFade()
                    .centerCrop()
                    .into(viewHolder.imageView);
        } else {
            final Uri uri = Uri.fromFile(new File(arrayList.get(position).getCover()));
            Glide.with(context).load(arrayList.get(position).getCover())
                    .placeholder(R.color.colorPrimary)
                    .override(200, 200)
                    .crossFade()
                    .centerCrop()
                    .into(viewHolder.imageView);
        }
        return convertView;
    }

    private static class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
