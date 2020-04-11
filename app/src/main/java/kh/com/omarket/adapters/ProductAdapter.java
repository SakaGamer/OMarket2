package kh.com.omarket.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kh.com.omarket.R;
import kh.com.omarket.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private ItemClickListener itemClickListener;
    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    public void setRecyclerViewItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.pName.setText(product.getName());
        holder.pPrice.setText(product.getPrice());
        holder.pLocation.setText(product.getLocation());
        //decode image from string to byte---
        if (product.getImage() != null) {
            byte[] decode = Base64.decode(product.getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
            holder.imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public interface ItemClickListener {
        void onRecyclerItemClick(View v, int position);
    }

    public void setProducts(List<Product> products){
        this.productList = products;
        notifyDataSetChanged();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView pName, pPrice, pLocation;
        ImageView imageView;

        private ProductViewHolder(View itemView) {
            super(itemView);
            pName = itemView.findViewById(R.id.card_title);
            pPrice = itemView.findViewById(R.id.card_price);
            pLocation = itemView.findViewById(R.id.card_sub_title);
            imageView = itemView.findViewById(R.id.card_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null)
                itemClickListener.onRecyclerItemClick(v, getAdapterPosition());
        }
    }

}//end main class
