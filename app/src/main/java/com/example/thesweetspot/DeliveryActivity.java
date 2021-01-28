package com.example.thesweetspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DeliveryActivity extends AppCompatActivity {

    public static List<CartItemModel> cartItemModelList;
    private RecyclerView deliveryRecyclerView;
    private Button changeAddAddressButton;
    public static final int SELECT_ADDRESS = 0;
    private TextView totalAmount;
    private TextView fullName;
    private TextView fullAddress;
    private TextView pinCode;
    private Button continueBtn;
    private Dialog loadingDialog;
    private Dialog paymentMethodDialog;
    private ImageButton paytm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");

        deliveryRecyclerView = findViewById(R.id.delivery_recyclerView);
        changeAddAddressButton = findViewById(R.id.change_or_add_address_btn);
        totalAmount = findViewById(R.id.total_cart_amount);
        fullName = findViewById(R.id.full_name);
        fullAddress = findViewById(R.id.address);
        pinCode = findViewById(R.id.pincode);
        continueBtn = findViewById(R.id.cart_continue_btn);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(linearLayoutManager);

        CartAdapter cartAdapter = new CartAdapter(cartItemModelList, totalAmount, false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        changeAddAddressButton.setVisibility(View.VISIBLE);

        changeAddAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myAddressesIntent = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
                myAddressesIntent.putExtra("MODE", SELECT_ADDRESS);
                startActivity(myAddressesIntent);
            }
        });

        ///////loading dialog
        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ///////loading dialog

        ///////payment method dialog
        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.payment_method);
        loadingDialog.setCancelable(true);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ///////payment method dialog

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethodDialog.show();
                paytm = paymentMethodDialog.findViewById(R.id.paytm);
            }
        });

        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethodDialog.dismiss();
                loadingDialog.show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        fullName.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFullName());
        fullAddress.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAddress());
        pinCode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPinCode());

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}