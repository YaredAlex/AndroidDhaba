package com.example.myapplication;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.adapter.ItemRecyclerAdapter;
import com.example.myapplication.model.ItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class AllItemFragment extends Fragment {

   FirebaseFirestore db;
   ItemRecyclerAdapter itemAdapter;
   List<ItemModel> list = new ArrayList<>();
   Bundle bundle;
   String category;
    public AllItemFragment(String category) {
        // Required empty public constructor
        this.category = category;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        View view = inflater.inflate(R.layout.fragment_all_item, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.item_reclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        recyclerView.addItemDecoration(new ItemDecorator(10));
        itemAdapter = new ItemRecyclerAdapter(list, new ItemRecyclerAdapter.OnItemClick() {
            @Override
            public void itemClicked(ItemModel model) {
                Intent intent = new Intent(getActivity(),ItemDetailActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("model",model);
                intent.putExtra("model",bundle1);
                startActivity(intent);
            }
        },getActivity());
        recyclerView.setAdapter(itemAdapter);
        if(category.equals("all"))
            fetchAllData();
        else
            fetchBasedOnCategoryData();
        return view;
    }
//When document is changed
    private void listenToDocumentChange(){
        db.collection("itemcollections").whereEqualTo("category",category).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                }
                else{
                    List<DocumentChange> changes =  value.getDocumentChanges();
                    changes.forEach(item->{
                        ItemModel model = item.getDocument().toObject(ItemModel.class);
                        model.setId(item.getDocument().getId());
                        list.add(model);
                    });
                    Collections.shuffle(list);
                    itemAdapter.setItemList(list);
                }
            }
        });
    }
    private void fetchBasedOnCategoryData() {
       db.collection("itemcollections").whereEqualTo("category",category).addSnapshotListener(new EventListener<QuerySnapshot>() {
           @RequiresApi(api = Build.VERSION_CODES.N)
           @Override
           public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
               if(error!=null){
                   Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
               }
               else {
                   List<DocumentChange> changes = value.getDocumentChanges();
                   changes.forEach(change->{
                       ItemModel model = change.getDocument().toObject(ItemModel.class);
                       model.setId(change.getDocument().getId());
                       list.add(model);
                   });
                   Collections.shuffle(list);
                   itemAdapter.setItemList(list);
               }
           }
       });
    }
    private void fetchAllData(){
        db = FirebaseFirestore.getInstance();
        db.collection("itemcollections").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                }
                else{
                    List<DocumentChange> changes = value.getDocumentChanges();
                    changes.forEach(change->{
                        ItemModel model = change.getDocument().toObject(ItemModel.class);
                        model.setId(change.getDocument().getId());
                        list.add(model);
                    });
                    Collections.shuffle(list);
                    itemAdapter.setItemList(list);
                }
            }
        });
    }


    class ItemDecorator extends RecyclerView.ItemDecoration{
        int margin;
        ItemDecorator(int m){
            this.margin = m;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.right = margin;
            outRect.bottom = margin;
            outRect.top = margin;
        }
    }
}