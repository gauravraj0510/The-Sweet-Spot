package com.example.thesweetspot;

import android.app.Dialog;
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
    private Dialog loadingDialog;
    public static WishListAdapter wishListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_wish_list, container, false);
        wishListRecyclerView = view.findViewById(R.id.my_wish_list_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        wishListRecyclerView.setLayoutManager(linearLayoutManager);

        ///////loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        ///////loading dialog

        if(DBqueries.wishListModelList.size()==0){
            DBqueries.wishList.clear();
            DBqueries.loadWishList(getContext(),loadingDialog, true);
        }
        else {
            loadingDialog.dismiss();
        }

        wishListAdapter = new WishListAdapter(DBqueries.wishListModelList, true);
        wishListRecyclerView.setAdapter(wishListAdapter);
        wishListAdapter.notifyDataSetChanged();

        return view;
    }
}