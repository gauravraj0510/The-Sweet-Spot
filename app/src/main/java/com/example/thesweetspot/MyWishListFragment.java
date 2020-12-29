package com.example.thesweetspot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MyWishListFragment extends Fragment {

    public MyWishListFragment() {
        // Required empty public constructor
    }

    private RecyclerView wishListRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_wish_list, container, false);
        wishListRecyclerView = view.findViewById(R.id.my_wish_list_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        wishListRecyclerView.setLayoutManager(linearLayoutManager);

        List<WishListModel> wishListModelList = new ArrayList<>();
        wishListModelList.add(new WishListModel(R.drawable.mobile_image,"Pixel 2", 1, "3", 45, "Rs.4999/", "Rs.5999/-","Cash on Delivery available"));
        wishListModelList.add(new WishListModel(R.drawable.mobile_image,"Pixel 2", 0, "4", 45, "Rs.4999/", "Rs.5999/-","Cash on Delivery available"));
        wishListModelList.add(new WishListModel(R.drawable.mobile_image,"Pixel 2", 2, "2.5", 45, "Rs.4999/", "Rs.5999/-","Cash on Delivery available"));
        wishListModelList.add(new WishListModel(R.drawable.mobile_image,"Pixel 2", 4, "4.5", 45, "Rs.4999/", "Rs.5999/-","Cash on Delivery available"));
        wishListModelList.add(new WishListModel(R.drawable.mobile_image,"Pixel 2", 0, "5", 45, "Rs.4999/", "Rs.5999/-","Cash on Delivery available"));

        WishListAdapter wishListAdapter = new WishListAdapter(wishListModelList, true);
        wishListRecyclerView.setAdapter(wishListAdapter);
        wishListAdapter.notifyDataSetChanged();

        return view;
    }
}