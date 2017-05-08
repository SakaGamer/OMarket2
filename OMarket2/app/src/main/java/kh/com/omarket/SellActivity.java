package kh.com.omarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

public class SellActivity extends AppCompatActivity {

    GridView gridView;
    Bundle bundle = new Bundle();
    String[] category;
    int[] img = {
            R.drawable.avatar, R.drawable.chat_2, R.drawable.heart,
            R.drawable.house, R.drawable.like, R.drawable.monitor,
            R.drawable.settings, R.drawable.shopping_cart
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        gridView = (GridView) findViewById(R.id.sell_grid_category);
        category = getResources().getStringArray(R.array.product_categories);
    }

    @Override
    protected void onStart() {
        super.onStart();

        AdapterProductGrid adapterForGridView = new AdapterProductGrid(
                getApplicationContext(), category, img);
        gridView.setAdapter(adapterForGridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bundle.putString("selectedCategory", category[position]);
//                Intent intent = new Intent(getApplicationContext(), SellActivity3.class);
//                intent.putExtra("selectedCategory", category[position]);
//                startActivity(intent);
                getSupportFragmentManager().beginTransaction().replace(R.id.sell_content_layout,
                        new SellFragment3()).addToBackStack("sell3").commit();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.start_slide_to_right,R.anim.exit_slide_to_right);
    }
}