package com.example.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class RelatedItemAdapter extends RecyclerView.Adapter<RelatedItemAdapter.ViewHolder> {

    List<ItemModel> itemModelList = new ArrayList<>();
    FragmentManager fragmentManager;
    Context context;
    Activity activity;
    OnItemClick onItemClick;
    public RelatedItemAdapter(List<ItemModel> list, OnItemClick onItemClick, Activity activity){
        this.itemModelList = list;
        this.onItemClick = onItemClick;
        this.activity = activity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.related_item_layout,parent,false);
           ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = position;
        holder.cardHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.itemClicked(itemModelList.get(pos));
            }
        });
        holder.itemName.setText(itemModelList.get(position).getItemName());
        holder.itemPrice.setText(String.valueOf(itemModelList.get(position).getItemPrice()));
        Glide.with(activity).load(itemModelList.get(position).getImgUri()).centerCrop().into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }
    public void setItemList(List<ItemModel> list){
        this.itemModelList = list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView itemName,itemPrice,itemDescription,itemProvider;
        ImageView itemImage;
        RelativeLayout cardHolder;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemImage = itemView.findViewById(R.id.item_img);
            cardHolder = itemView.findViewById(R.id.card_holder);
        }
    }
    public interface OnItemClick{
        public void itemClicked(ItemModel model);
    }
}
