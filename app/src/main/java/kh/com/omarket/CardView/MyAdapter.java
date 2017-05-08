package kh.com.omarket.CardView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kh.com.omarket.ProductDetailActivity;
import kh.com.omarket.R;

/**
 * Created by Saka on 29-Apr-17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private onItemClickListener itemClickListener;
    private Context context;
    private List<Product> products = new ArrayList<>();

    public MyAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title;
        private TextView descr;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.card_title);
            descr = (TextView) itemView.findViewById(R.id.card_sub_title);
            itemView.setOnClickListener(this);
        }

        public void setOnItemClickListener(onItemClickListener itemClickListener2){
            itemClickListener = itemClickListener2;
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null)
                itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Product product = products.get(position);
        holder.title.setText(product.getName());
        holder.descr.setText(product.getDescription());

        context = holder.itemView.getContext();
        holder.setOnItemClickListener(new onItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //  Open DetailActivity
                Product p = products.get(position);
                Intent i = new Intent(context, ProductDetailActivity.class);
                i.putExtra("name", p.getName());
                i.putExtra("descr", p.getDescription());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setItemClickListener(onItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface onItemClickListener{
        void onItemClick(View v, int position);
    }

}
