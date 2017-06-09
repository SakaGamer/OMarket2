package kh.com.omarket.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kh.com.omarket.R;
import kh.com.omarket.model.Product;

/**
 * Created by Saka on 29-Apr-17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private RecyclerItemClickListener itemClickListener;
    private List<Product> products = new ArrayList<>();

    public MyAdapter(List<Product> products) {
        this.products = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Product product = products.get(position);
        holder.title.setText(product.getName());
        holder.location.setText(product.getLocation());
        holder.price.setText(product.getPrice());
        //decode image from string to byte---
        if (product.getImage() != null) {
            byte[] decode = Base64.decode(product.getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
            holder.img.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setItemClickListener(RecyclerItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface RecyclerItemClickListener {
        void onRecyclerItemClick(View v, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private TextView location;
        private TextView price;
        private ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.card_title);
            location = (TextView) itemView.findViewById(R.id.card_sub_title);
            price = (TextView) itemView.findViewById(R.id.card_price);
            img = (ImageView) itemView.findViewById(R.id.card_img);
            itemView.setOnClickListener(this);
        }

        public void setOnItemClickListener(RecyclerItemClickListener itemClickListener2) {
            itemClickListener = itemClickListener2;
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null)
                itemClickListener.onRecyclerItemClick(v, getAdapterPosition());
        }
    }

}
