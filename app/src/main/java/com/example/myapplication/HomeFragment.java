package com.example.myapplication;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.adapter.CarouselAdapter;
import com.example.myapplication.adapter.ItemRecyclerAdapter;
import com.example.myapplication.adapter.VPAdapter;
import com.example.myapplication.model.CarouselModel;
import com.example.myapplication.model.ItemModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    RecyclerView carousleRecycler;
    List<CarouselModel> list = new ArrayList<>();
    ItemRecyclerAdapter itemAdapter;
    List<ItemModel> listItem;
    FirebaseFirestore db;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        carousleRecycler = view.findViewById(R.id.recycler_carousel);
        list.add(new CarouselModel("@drawable/c4","This is what you have waiting for"));
        list.add(new CarouselModel("@drawable/c6","What are you waiting for"));
        CarouselAdapter adapter = new CarouselAdapter(list,getContext());
        carousleRecycler.setAdapter(adapter);
        carousleRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
       //  RecyclerView  recyclerView = view.findViewById(R.id.item_reclerview);
        listItem = new ArrayList<>();
        itemAdapter = new ItemRecyclerAdapter(listItem, new ItemRecyclerAdapter.OnItemClick() {
            @Override
            public void itemClicked(ItemModel model) {

            }
        },getActivity());
        //recyclerView.setAdapter(itemAdapter);
      //  recyclerView.addItemDecoration(new SpacesItemDecoration(1));
       // recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3,GridLayoutManager.VERTICAL,false));
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        VPAdapter vpAdapter = new VPAdapter(getActivity());
        vpAdapter.setFragmentAndTitle(new AllItemFragment("all"),"allItem");
        vpAdapter.setFragmentAndTitle(new AllItemFragment("Vegetarian"),"Veg");
        vpAdapter.setFragmentAndTitle(new AllItemFragment("Non Vegetarian"),"non-veg");
        viewPager.setAdapter(vpAdapter);
        String names[] = {"All","Veg","Non-Veg"};
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(names[position]);
            }
        }).attach();

        db = FirebaseFirestore.getInstance();
        //fetchData();


        return view;
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;


            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = space;
            } else {
                outRect.top = 0;
            }
        }
    }
}