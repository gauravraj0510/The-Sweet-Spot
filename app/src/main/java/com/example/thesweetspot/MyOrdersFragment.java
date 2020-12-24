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


public class MyOrdersFragment extends Fragment {

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    private RecyclerView myOrdersRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        myOrdersRecyclerView = view.findViewById(R.id.my_orders_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        myOrdersRecyclerView.setLayoutManager(layoutManager);

        List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.mobile_image,"Pixel 2XL (BLACK)","Delivered on Mon, 5th Oct 2020",2));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.banner,"Pixel 2XL (BLACK)","Delivered on Mon, 5th Oct 2020",1));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.mobile_image,"Pixel 2XL (BLACK)","Cancelled",4));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.my_sweet_spot_icon,"Pixel 2XL (BLACK)","Delivered on Mon, 5th Oct 2020",3));

        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(myOrderItemModelList);
        myOrdersRecyclerView.setAdapter(myOrderAdapter);
        myOrderAdapter.notifyDataSetChanged();

        return  view;
    }
}