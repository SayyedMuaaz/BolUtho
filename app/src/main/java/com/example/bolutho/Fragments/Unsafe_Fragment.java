package com.example.bolutho.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bolutho.Fragments.ModelClasses.RecyclerView_Adapter.UnsafeAddressAdapter;
import com.example.bolutho.Fragments.ModelClasses.RecyclerView_Adapter.UserPostAdapter;
import com.example.bolutho.Fragments.ModelClasses.UserStories;
import com.example.bolutho.R;

import java.util.ArrayList;
import java.util.List;

import kotlin.contracts.Returns;


public class Unsafe_Fragment extends Fragment {

    List<String> unsafeAddress = new ArrayList<>();
    RecyclerView unsafeRv;
    private TextView heading;
    UnsafeAddressAdapter unsafeAddressAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        unsafeAddress = getArguments().getStringArrayList("addressList");
        View view=inflater.inflate(R.layout.fragment_unsafe_, container, false);
        unsafeRv=view.findViewById(R.id.unsafeRv);
        heading=getActivity().findViewById(R.id.title);
        heading.setText("UNSAFE ADDRESS");
        setAdapter(unsafeAddress);
        return view;
    }
    private void setAdapter(List<String> unsafeAddress) {
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getContext());
        unsafeRv.setLayoutManager(linearLayoutManager);
        if (getContext() != null) {
            unsafeAddressAdapter = new UnsafeAddressAdapter(unsafeAddress, getContext());
            unsafeRv.setAdapter(unsafeAddressAdapter);
        }
    }



}
