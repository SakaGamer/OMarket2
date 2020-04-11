package kh.com.omarket.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import kh.com.omarket.R;

public class ProductCategoryAdapter extends BaseAdapter {

    private Context context;
    private String[] category;
    private int[] img;

    public ProductCategoryAdapter(Context context, String[] category) {
        this.context = context;
        this.category = category;
    }

    @Override
    public int getCount() {
        return category.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        TextView textView;
        //ImageView image;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            grid = new View(context);
            grid = inflater.inflate(R.layout.viewholder_img, null);
            textView = (TextView) grid.findViewById(R.id.grid_text);
            //image = (ImageView) grid.findViewById(R.id.grid_image);

            textView.setText(category[position]);
            //image.setImageResource(img[position]);
        } else{
            grid = (View) convertView;
        }

        return grid;
    }

}//end main class
