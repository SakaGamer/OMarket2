package kh.com.omarket.adapters;

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

import kh.com.omarket.R;
import kh.com.omarket.models.Product;

public class AdapterProductCard extends FirebaseRecyclerAdapter<Product, AdapterProductCard.ProductViewHolder> {

        private RecyclerViewItemClickListener itemClickListener;

        public AdapterProductCard (Class<Product> modelClass, int modelLayout,
                                   Class<AdapterProductCard.ProductViewHolder> viewHolderClass, Query ref) {
            super (modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder (final AdapterProductCard.ProductViewHolder viewHolder,
                                           Product model, final int position) {
            viewHolder.pName.setText (model.getName());
            viewHolder.pPrice.setText (model.getPrice());
            viewHolder.pLocation.setText (model.getLocation());
            //decode image from string to byte---
            if (model.getImage() != null) {
                byte[] decode = Base64.decode (model.getImage(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray (decode, 0, decode.length);
                viewHolder.imageView.setImageBitmap (bitmap);
            }
        }

        @Override
        public ProductViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
            View view = LayoutInflater.from (parent.getContext()).inflate (mModelLayout, parent, false);
            return new ProductViewHolder (view);
        }

        public void setRecyclerViewItemClickListener (RecyclerViewItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public interface RecyclerViewItemClickListener
        {
            void onRecyclerItemClick (View v, int position);
        }

        public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                TextView pName, pPrice, pLocation;
                ImageView imageView;

                private ProductViewHolder (View itemView) {
                    super (itemView);
                    pName = (TextView) itemView.findViewById (R.id.card_title);
                    pPrice = (TextView) itemView.findViewById (R.id.card_price);
                    pLocation = (TextView) itemView.findViewById (R.id.card_sub_title);
                    imageView = (ImageView) itemView.findViewById (R.id.card_img);
                    itemView.setOnClickListener (this);
                }

                @Override
                public void onClick (View v) {
                    if (itemClickListener != null)
                    { itemClickListener.onRecyclerItemClick (v, getPosition()); }
                }
        }

}//end main class
