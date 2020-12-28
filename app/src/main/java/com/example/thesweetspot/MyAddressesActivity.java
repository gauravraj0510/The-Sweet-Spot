package com.example.thesweetspot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MyAddressesActivity extends AppCompatActivity {

    private RecyclerView myAddressesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Addresses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myAddressesRecyclerView = findViewById(R.id.addresses_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        myAddressesRecyclerView.setLayoutManager(layoutManager);

        List<AddressesModel> addressesModelList = new ArrayList<>();
        addressesModelList.add(new AddressesModel("Gaurav Raj", "Address 1","400097"));
        addressesModelList.add(new AddressesModel("Vaibhav Raj", "Address 2","400026"));
        addressesModelList.add(new AddressesModel("Ganesh Prasad", "Address 3","400097"));
        addressesModelList.add(new AddressesModel("Vibha Prasad", "Address 4","400026"));
        addressesModelList.add(new AddressesModel("Gaurav Raj", "Address 1","400097"));
        addressesModelList.add(new AddressesModel("Vaibhav Raj", "Address 2","400026"));
        addressesModelList.add(new AddressesModel("Ganesh Prasad", "Address 3","400097"));
        addressesModelList.add(new AddressesModel("Vibha Prasad", "Address 4","400026"));

        AddressesAdapter addressesAdapter = new AddressesAdapter(addressesModelList);
        myAddressesRecyclerView.setAdapter(addressesAdapter);
        addressesAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}