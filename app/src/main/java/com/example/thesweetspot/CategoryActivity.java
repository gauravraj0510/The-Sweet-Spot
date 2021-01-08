package com.example.thesweetspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static com.example.thesweetspot.DBqueries.lists;
import static com.example.thesweetspot.DBqueries.loadFragmentData;
import static com.example.thesweetspot.DBqueries.loadedCategoriesName;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;
    private HomePageAdapter adapter;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ///////home page fake list
        List<SliderModel> sliderModelFakeList=new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null","#139c69"));
        sliderModelFakeList.add(new SliderModel("null","#139c69"));
        sliderModelFakeList.add(new SliderModel("null","#139c69"));
        sliderModelFakeList.add(new SliderModel("null","#139c69"));
        sliderModelFakeList.add(new SliderModel("null","#139c69"));

        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList=new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));

        homePageModelFakeList.add(new HomePageModel(0,sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1,"","#ffffff"));
        homePageModelFakeList.add(new HomePageModel(2,"","#ffffff",horizontalProductScrollModelFakeList,new ArrayList<WishListModel>()));
        homePageModelFakeList.add(new HomePageModel(3,"","#ffffff",horizontalProductScrollModelFakeList));
        ///////home page fake list


        categoryRecyclerView = findViewById(R.id.category_recyclerView);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(this);
        testingLayoutManager.setOrientation(RecyclerView.VERTICAL);
        categoryRecyclerView.setLayoutManager(testingLayoutManager);

        adapter = new HomePageAdapter(homePageModelFakeList);

        int listPosition = 0;
        for(int x=0; x< loadedCategoriesName.size(); x++){
            if(loadedCategoriesName.get(x).equals(title.toUpperCase())){
                listPosition = x;
            }
        }

        if(listPosition == 0){
            loadedCategoriesName.add(title.toUpperCase());
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(categoryRecyclerView, this,loadedCategoriesName.size()-1, title);
        }
        else{
            adapter = new HomePageAdapter(lists.get(listPosition));
        }

        categoryRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.menu_search_icon){
            return true;
        }else if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
