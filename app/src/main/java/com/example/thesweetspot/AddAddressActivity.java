package com.example.thesweetspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {

    private Button saveButton;

    private EditText city;
    private EditText locality;
    private EditText flatNo;
    private EditText pinCode;
    private EditText landmark;
    private EditText name;
    private EditText mobNo;
    private EditText alternateMobNo;

    private Spinner stateSpinner;
    private Dialog loadingDialog;


    private String [] stateList;
    private String selectedState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        Toolbar toolbar = findViewById(R.id.toolbar);

        city = findViewById(R.id.city);
        locality = findViewById(R.id.locality);
        flatNo = findViewById(R.id.flat_no);
        pinCode = findViewById(R.id.pincode);
        landmark = findViewById(R.id.landmark);
        name = findViewById(R.id.name);
        mobNo = findViewById(R.id.mobile_no);
        alternateMobNo = findViewById(R.id.alternate_mobile_no);

        stateSpinner = findViewById(R.id.state_spinner);

        saveButton = findViewById(R.id.save_btn);
        stateList = getResources().getStringArray(R.array.india_states);
        ///////loading dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ///////loading dialog


        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stateList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(spinnerAdapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = stateList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add new Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(city.getText())){
                    if(!TextUtils.isEmpty(locality.getText())){
                        if(!TextUtils.isEmpty(flatNo.getText())){
                            if(!TextUtils.isEmpty(pinCode.getText()) && pinCode.getText().length() == 6){
                                if(!TextUtils.isEmpty(name.getText())){
                                    if(!TextUtils.isEmpty(mobNo.getText()) && mobNo.getText().length() == 10){

                                        loadingDialog.show();
                                        final String fullAddress = flatNo.getText().toString()+", "+locality.getText().toString()+" - "+city.getText().toString()+", "+landmark.getText().toString();

                                        Map<String, Object> addAddress = new HashMap();
                                        addAddress.put("list_size", (long)DBqueries.addressesModelList.size()+1);
                                        addAddress.put("fullname_"+((long)DBqueries.addressesModelList.size()+1), name.getText().toString()+" - "+mobNo.getText().toString());
                                        addAddress.put("address_"+((long)DBqueries.addressesModelList.size()+1), fullAddress);
                                        addAddress.put("pincode_"+((long)DBqueries.addressesModelList.size()+1), pinCode.getText().toString());
                                        addAddress.put("selected_"+((long)DBqueries.addressesModelList.size()+1), true);
                                        if (DBqueries.addressesModelList.size()>0) {
                                            addAddress.put("selected_" + (long) DBqueries.selectedAddress + 1, false);
                                        }
                                        FirebaseFirestore.getInstance().collection("USERS")
                                                .document(FirebaseAuth.getInstance().getUid())
                                                .collection("USER_DATA")
                                                .document("MY_ADDRESSES")
                                                .update(addAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    if (DBqueries.addressesModelList.size()>0) {
                                                        DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSelected(false);
                                                    }
                                                    DBqueries.addressesModelList.add(new AddressesModel(
                                                            name.getText().toString()+" - "+mobNo.getText().toString(),
                                                            fullAddress,
                                                            pinCode.getText().toString(),
                                                            true
                                                    ));

                                                    Intent deliveryIntent = new Intent(AddAddressActivity.this, DeliveryActivity.class);
                                                    startActivity(deliveryIntent);
                                                    finish();
                                                }
                                                else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(AddAddressActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                                loadingDialog.dismiss();
                                            }
                                        });

                                    } else {
                                        mobNo.requestFocus();
                                        Toast.makeText(AddAddressActivity.this, "Please enter valid Phone No!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    name.requestFocus();
                                    Toast.makeText(AddAddressActivity.this, "Please enter name!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                pinCode.requestFocus();
                                Toast.makeText(AddAddressActivity.this, "Please enter valid PINCODE!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            flatNo.requestFocus();
                            Toast.makeText(AddAddressActivity.this, "Please enter Flat and building details!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        locality.requestFocus();
                        Toast.makeText(AddAddressActivity.this, "Please enter locality!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    city.requestFocus();
                    Toast.makeText(AddAddressActivity.this, "Please enter city!", Toast.LENGTH_SHORT).show();
                }


            }
        });
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