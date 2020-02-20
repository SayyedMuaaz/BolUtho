package com.example.bolutho.Fragments.ModelClasses.RecyclerView_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bolutho.R;

import java.util.List;

public class UnsafeAddressAdapter extends RecyclerView.Adapter<UnsafeAddressAdapter.UnsafeViewHolder> {
    List<String> UnsafeAddress;
    Context context;

    public UnsafeAddressAdapter(List<String> unsafeAddress, Context context) {
        UnsafeAddress = unsafeAddress;
        this.context = context;
    }

    @NonNull
    @Override
    public UnsafeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unsafeaddressadapter, parent, false);
        return new UnsafeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnsafeViewHolder holder, int position) {
        holder.unsafeAdress.setText(UnsafeAddress.get(position));

    }

    @Override
    public int getItemCount() {
        return UnsafeAddress.size();
    }

    public class UnsafeViewHolder extends RecyclerView.ViewHolder {
        TextView unsafeAdress;
        public UnsafeViewHolder(@NonNull View itemView) {
            super(itemView);
            unsafeAdress=itemView.findViewById(R.id.addressUnsafe);


        }
    }
}
