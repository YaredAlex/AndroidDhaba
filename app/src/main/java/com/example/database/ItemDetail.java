package com.example.database;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ItemDetail extends Fragment {

    TextView itemName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        View view = inflater.inflate(R.layout.fragment_item_detail, container, false);
        itemName = view.findViewById(R.id.item_name);
        itemName.setText(bundle.getString("itemName"));
        return view;
    }

    public void onBackPressed(){

    }
}