package com.example.database;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.database.adapter.ItemAdapter;
import com.example.database.model.ItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;


public class ItemViewFragment extends Fragment {


    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    List<ItemModel> listItem;
    FirebaseFirestore db;
    GridLayoutManager gridLayoutManager;
    public ItemViewFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_view, container, false);
        recyclerView = view.findViewById(R.id.item_reclerview);
        listItem = new ArrayList<>();
        itemAdapter = new ItemAdapter(listItem, new ItemAdapter.OnItemClick() {
            @Override
            public void itemClicked(ItemModel model) {
                changeFragmentDetail(model);
            }
        });
        recyclerView.setAdapter(itemAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(1));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3,GridLayoutManager.VERTICAL,false));

        db = FirebaseFirestore.getInstance();
       //fetchData();
        db.collection("itemcollections").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<DocumentChange> docchange = value.getDocumentChanges();
                docchange.forEach(doc->{
                    ItemModel model = doc.getDocument().toObject(ItemModel.class);
                    listItem.add(model);
                    itemAdapter.setItemList(listItem);
                });
                Log.d("our data is in quirysnopshot event listner: ",listItem.toString());
            }
        });
        return view;
    }

    private void changeFragmentDetail(ItemModel model) {
        ItemDetail itemDetail = new ItemDetail();
        Bundle bundle = new Bundle();
        bundle.putString("itemName",model.getItemName());
        itemDetail.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,itemDetail).addToBackStack("detail").commit();
    }

    private void fetchData() {


        db.collection("itemcollections").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        ItemModel model = doc.toObject(ItemModel.class);
                        listItem.add(model);
                    }
                    Toast.makeText(getActivity(), "Completed", Toast.LENGTH_SHORT).show();
                    Log.d("Data base result is ",listItem.toString());
                    itemAdapter.setItemList(listItem);

                }
                else{
                    Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
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