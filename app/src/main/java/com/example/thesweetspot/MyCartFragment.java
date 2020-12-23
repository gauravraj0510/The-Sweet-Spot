package com.example.thesweetspot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MyCartFragment extends Fragment {


    public MyCartFragment() {
        // Required empty public constructor
    }

    private RecyclerView cartItemsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        cartItemsRecyclerView = view.findViewById(R.id.cart_items_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(linearLayoutManager);

        List<CartItemModel> cartItemModelList = new ArrayList<>();
        cartItemModelList.add(new CartItemModel(0,R.drawable.mobile_image, "Pixel 2",2,"Rs.4999/-","Rs.5999/-",1 ,0, 0));
        cartItemModelList.add(new CartItemModel(0,R.drawable.mobile_image, "Pixel 2",0,"Rs.4999/-","Rs.5999/-",1 ,1, 0));
        cartItemModelList.add(new CartItemModel(0,R.drawable.mobile_image, "Pixel 2",2,"Rs.4999/-","Rs.5999/-",1 ,2, 0));
        cartItemModelList.add(new CartItemModel(1,"Price (3 items)","Rs.8999/-","Free","Rs.8999/-","Rs.899/-"));

        CartAdapter cartAdapter = new CartAdapter(cartItemModelList);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        return  view;
    }
}