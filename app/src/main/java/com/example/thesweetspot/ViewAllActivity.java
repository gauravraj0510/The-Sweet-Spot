package com.example.thesweetspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Deals of the Day");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        gridView = findViewById(R.id.grid_view);

        int layout_code = getIntent().getIntExtra("layout_code",-1);

        if(layout_code == 0) {
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);

            List<WishListModel> wishListModelList = new ArrayList<>();
            wishListModelList.add(new WishListModel(R.drawable.mobile_image, "Pixel 2", 1, "3", 45, "Rs.4999/", "Rs.5999/-", "Cash on Delivery available"));
            wishListModelList.add(new WishListModel(R.drawable.mobile_image, "Pixel 2", 0, "4", 45, "Rs.4999/", "Rs.5999/-", "Cash on Delivery available"));
            wishListModelList.add(new WishListModel(R.drawable.mobile_image, "Pixel 2", 2, "2.5", 45, "Rs.4999/", "Rs.5999/-", "Cash on Delivery available"));
            wishListModelList.add(new WishListModel(R.drawable.mobile_image, "Pixel 2", 4, "4.5", 45, "Rs.4999/", "Rs.5999/-", "Cash on Delivery available"));
            wishListModelList.add(new WishListModel(R.drawable.mobile_image, "Pixel 2", 0, "5", 45, "Rs.4999/", "Rs.5999/-", "Cash on Delivery available"));

            WishListAdapter adapter = new WishListAdapter(wishListModelList, false);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else if(layout_code == 1) {
            gridView.setVisibility(View.VISIBLE);

            List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.mobile_image, "Redmi 5A", "SD 625 Processor", "Rs.5999/-"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.account_icon, "Redmi 5A", "SD 625 Processor", "Rs.5999/-"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.cart_icon, "Redmi 5A", "SD 625 Processor", "Rs.5999/-"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.mail_green, "Redmi 5A", "SD 625 Processor", "Rs.5999/-"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.mail_red, "Redmi 5A", "SD 625 Processor", "Rs.5999/-"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.gift_icon, "Redmi 5A", "SD 625 Processor", "Rs.5999/-"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.signout_icon, "Redmi 5A", "SD 625 Processor", "Rs.5999/-"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.my_sweet_spot_icon, "Redmi 5A", "SD 625 Processor", "Rs.5999/-"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.mobile_image, "Redmi 5A", "SD 625 Processor", "Rs.5999/-"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.account_icon, "Redmi 5A", "SD 625 Processor", "Rs.5999/-"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.cart_icon, "Redmi 5A", "SD 625 Processor", "Rs.5999/-"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.mail_green, "Redmi 5A", "SD 625 Processor", "Rs.5999/-"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.mail_red, "Redmi 5A", "SD 625 Processor", "Rs.5999/-"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.gift_icon, "Redmi 5A", "SD 625 Processor", "Rs.5999/-"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.signout_icon, "Redmi 5A", "SD 625 Processor", "Rs.5999/-"));
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.my_sweet_spot_icon, "Redmi 5A", "SD 625 Processor", "Rs.5999/-"));

            GridProductLayoutAdapter gridProductLayoutAdapter = new GridProductLayoutAdapter(horizontalProductScrollModelList);
            gridView.setAdapter(gridProductLayoutAdapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
