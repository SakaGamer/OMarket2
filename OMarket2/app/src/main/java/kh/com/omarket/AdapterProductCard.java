package kh.com.omarket;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by saka on 3/28/17.
 */

public class AdapterProductCard extends FirebaseRecyclerAdapter<Product, AdapterProductCard.ProductViewHolder> {


    public AdapterProductCard(Class<Product> modelClass, int modelLayout, Class<AdapterProductCard.ProductViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(AdapterProductCard.ProductViewHolder viewHolder, Product model, int position) {
        viewHolder.pName.setText(model.getName());
        viewHolder.pDescr.setText(model.getDescription());
        //decode image from string to byte---
        //byte[] decode = Base64.decode(model.getImage(), Base64.DEFAULT);
        //Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        //viewHolder.imageView.setImageBitmap(bitmap);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view, parent, false);
        ProductViewHolder pViewHolder = new ProductViewHolder(view);
        return pViewHolder;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView pName, pDescr;
        //ImageView imageView;
        CardView cardView;
        public ProductViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            pName = (TextView) itemView.findViewById(R.id.card_title);
            pDescr = (TextView) itemView.findViewById(R.id.card_sub_title);
            //imageView = (ImageView) itemView.findViewById(R.id.card_img);
        }
    }

    @Override
    public void onBindViewHolder(ProductViewHolder viewHolder, final int position) {
        super.onBindViewHolder(viewHolder, position);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked"+position);

            }
        });
    }


}//end main class
