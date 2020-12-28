package com.example.thesweetspot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.ViewHolder> {

    private List<AddressesModel> addressesModelList;

    public AddressesAdapter(List<AddressesModel> addressesModelList) {
        this.addressesModelList = addressesModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresses_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String name = addressesModelList.get(position).getFullName();
        String address = addressesModelList.get(position).getAddress();
        String pincode = addressesModelList.get(position).getPinCode();
        holder.setData(name, address,pincode);

    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView fullName;
        private TextView address;
        private TextView pinCode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            pinCode = itemView.findViewById(R.id.pincode);
        }

        private void setData(String userName, String userAddress, String userPincode){
            fullName.setText(userName);
            address.setText(userAddress);
            pinCode.setText(userPincode);
        }


    }
}
