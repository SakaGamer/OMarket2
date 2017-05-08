package kh.com.omarket.CardView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import kh.com.omarket.ProductDetailActivity;
import kh.com.omarket.R;

public class AdapterProductCard extends FirebaseRecyclerAdapter<Product, AdapterProductCard.ProductViewHolder> {

    private ItemClickListener itemClickListener;

    public AdapterProductCard(Class<Product> modelClass, int modelLayout,
                              Class<AdapterProductCard.ProductViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView pName,pPrice, pLocation;
        ImageView imageView;
        private ProductViewHolder(View itemView) {
            super(itemView);
            pName = (TextView) itemView.findViewById(R.id.card_title);
            pPrice = (TextView) itemView.findViewById(R.id.card_price);
            pLocation = (TextView) itemView.findViewById(R.id.card_sub_title);
            imageView = (ImageView) itemView.findViewById(R.id.card_img);
            itemView.setOnClickListener(this);
        }

        private void setOnItemClickListener(ItemClickListener itemClickListener2){
            itemClickListener = itemClickListener2;
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null)
                itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    @Override
    protected void populateViewHolder(final AdapterProductCard.ProductViewHolder viewHolder, Product model, final int position) {
        viewHolder.pName.setText(model.getName());
        viewHolder.pPrice.setText(model.getPrice());
        viewHolder.pLocation.setText(model.getLocation());

        //decode image from string to byte---
        if (model.getImage() != null){
            byte[] decode = Base64.decode(model.getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
            viewHolder.imageView.setImageBitmap(bitmap);
        }
        final Context context = viewHolder.itemView.getContext();
        viewHolder.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //  Open DetailActivity
                Product p = getItem(position);
                Intent i = new Intent(context, ProductDetailActivity.class);
                i.putExtra("key", p.getKey());
                i.putExtra("image", p.getImage());
                i.putExtra("category", p.getCategory());
                i.putExtra("subCategory", p.getSubCategory());
                i.putExtra("name", p.getName());
                i.putExtra("location", p.getLocation());
                i.putExtra("price", p.getPrice());
                i.putExtra("descr", p.getDescription());
                context.startActivity(i);
            }
        });
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mModelLayout, parent, false);
        return new ProductViewHolder(view);
    }

//    @Override
//    public void onBindViewHolder(final ProductViewHolder viewHolder, final int position) {
//        super.onBindViewHolder(viewHolder, position);
//        Product product = products.get(position);
//        viewHolder.pName.setText(product.getName());
//        viewHolder.pDescr.setText(product.getDescription());
//    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View v, int position);
    }

}//end main class
