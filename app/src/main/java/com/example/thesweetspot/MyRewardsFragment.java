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


public class MyRewardsFragment extends Fragment {


    public MyRewardsFragment() {
        // Required empty public constructor
    }

    private RecyclerView rewardsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_rewards, container, false);

        rewardsRecyclerView = view.findViewById(R.id.my_rewards_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rewardsRecyclerView.setLayoutManager(layoutManager);

        List<RewardModel> rewardModelList = new ArrayList<>();
        rewardModelList.add(new RewardModel("Cashback","Valid till 5th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Discount","Valid till 10th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Buy 1 Get 1 Free","Valid till 15th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Cashback","Valid till 5th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Discount","Valid till 10th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Buy 1 Get 1 Free","Valid till 15th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Cashback","Valid till 5th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Discount","Valid till 10th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Buy 1 Get 1 Free","Valid till 15th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));

        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(rewardModelList);
        rewardsRecyclerView.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();

        return view;
    }
}