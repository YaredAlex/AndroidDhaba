package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.adapter.RelatedItemAdapter;
import com.example.myapplication.model.ItemModel;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ItemDetailActivity extends AppCompatActivity {
   ImageView itemImg;
   TextView itemPrice;
   TextView itemDescription,itemProvider,itemName;

   RecyclerView relatedRecycler;
   ItemModel model;
   RelatedItemAdapter relatedAdapter;
   List<ItemModel> list = new ArrayList<>();
   FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Bundle bundle = getIntent().getBundleExtra("model");
        itemImg = findViewById(R.id.img_item_detail);
        itemPrice = findViewById(R.id.item_price);
        itemDescription = findViewById(R.id.item_description);
        itemProvider = findViewById(R.id.item_provider);
        itemName = findViewById(R.id.item_name);
        model = (ItemModel) bundle.getSerializable("model");
        Glide.with(this).load(model.getImgUri()).centerCrop().into(itemImg);
        itemPrice.setText(String.valueOf(model.getItemPrice()));
        itemDescription.setText(model.getDescription()+model.getId());
        itemProvider.setText(model.getProvider());
        itemName.setText(model.getItemName());

        //recyclerView
       relatedRecycler = findViewById(R.id.related_items);
         relatedAdapter = new RelatedItemAdapter(list, new RelatedItemAdapter.OnItemClick() {
            @Override
            public void itemClicked(ItemModel model) {
                Intent intent = new Intent(ItemDetailActivity.this,ItemDetailActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("model",model);
                intent.putExtra("model",bundle1);
                startActivity(intent);
            }
        },this);
        relatedRecycler.setAdapter(relatedAdapter);
        relatedRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        db = FirebaseFirestore.getInstance();
        fetchBasedOnCategoryData();
    }
    private void fetchBasedOnCategoryData() {
        db.collection("itemcollections").whereEqualTo("category",model.getCategory()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(ItemDetailActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
                else {
                    List<DocumentChange> changes = value.getDocumentChanges();
                    changes.forEach(change->{
                        ItemModel model = change.getDocument().toObject(ItemModel.class);
                        model.setId(change.getDocument().getId());
                        list.add(model);
                    });
                    Collections.shuffle(list);
                    relatedAdapter.setItemList(list);
                }
            }
        });
    }
}