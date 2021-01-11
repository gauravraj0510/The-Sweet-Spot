package com.example.thesweetspot;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.thesweetspot.DBqueries.categoryModelList;
import static com.example.thesweetspot.DBqueries.firebaseFirestore;
import static com.example.thesweetspot.DBqueries.lists;
import static com.example.thesweetspot.DBqueries.loadCategories;
import static com.example.thesweetspot.DBqueries.loadFragmentData;
import static com.example.thesweetspot.DBqueries.loadedCategoriesName;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    public static SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView homePageRecyclerView;
    private HomePageAdapter adapter;
    private ImageView noInternetConnection;
    private Button retryButton;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();

    private List<CategoryModel> categoryModelFakeList = new ArrayList<>();

    private ConnectivityManager connectivityManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        noInternetConnection = view.findViewById(R.id.no_internet_connection);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        homePageRecyclerView = view.findViewById(R.id.home_page_recyclerView);
        retryButton = view.findViewById(R.id.retry_button);
        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary), getContext().getResources().getColor(R.color.colorPrimary), getContext().getResources().getColor(R.color.colorPrimary));

        categoryRecyclerView = view.findViewById(R.id.category_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(RecyclerView.VERTICAL);
        homePageRecyclerView.setLayoutManager(testingLayoutManager);


        ///////categories fake list
        categoryModelFakeList.add(new CategoryModel("null",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));

        ///////categories fake list


        ///////home page fake list
        List<SliderModel> sliderModelFakeList=new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));

        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList=new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));

        homePageModelFakeList.add(new HomePageModel(0,sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1,"","#dfdfdf"));
        homePageModelFakeList.add(new HomePageModel(2,"","#dfdfdf",horizontalProductScrollModelFakeList,new ArrayList<WishListModel>()));
        homePageModelFakeList.add(new HomePageModel(3,"","#dfdfdf",horizontalProductScrollModelFakeList));
        ///////home page fake list

        categoryAdapter = new CategoryAdapter(categoryModelFakeList);

        adapter = new HomePageAdapter(homePageModelFakeList);

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            noInternetConnection.setVisibility(View.GONE);
            retryButton.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);

            if(categoryModelList.size() == 0){
                loadCategories(categoryRecyclerView, getContext());
            }
            else {
                categoryAdapter = new CategoryAdapter(categoryModelList);
                categoryAdapter.notifyDataSetChanged();
            }
            categoryRecyclerView.setAdapter(categoryAdapter);

            if(lists.size() == 0){
                loadedCategoriesName.add("HOME");
                lists.add(new ArrayList<HomePageModel>());
                loadFragmentData(homePageRecyclerView, getContext(),0, "HOME");
            }
            else {
                adapter = new HomePageAdapter(lists.get(0));
                adapter.notifyDataSetChanged();
            }
            homePageRecyclerView.setAdapter(adapter);
        }else{
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.no_internet_gif).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryButton.setVisibility(View.VISIBLE);
        }

        ////////refresh layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                reloadPage();
            }
        });
        ////////refresh layout

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadPage();
            }
        });

        return view;
    }

    private void reloadPage(){
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        categoryModelList.clear();
        lists.clear();
        loadedCategoriesName.clear();

        if(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            noInternetConnection.setVisibility(View.GONE);
            retryButton.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);

            categoryAdapter = new CategoryAdapter(categoryModelFakeList);
            categoryRecyclerView.setAdapter(categoryAdapter);

            adapter = new HomePageAdapter(homePageModelFakeList);
            homePageRecyclerView.setAdapter(adapter);

            loadCategories(categoryRecyclerView, getContext());

            loadedCategoriesName.add("HOME");
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(homePageRecyclerView, getContext(),0, "HOME");
        }
        else{
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            Toast.makeText(getContext(), "No Internet Connection! Please try again!", Toast.LENGTH_SHORT).show();
            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            Glide.with(getContext()).load(R.drawable.no_internet_gif).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryButton.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
