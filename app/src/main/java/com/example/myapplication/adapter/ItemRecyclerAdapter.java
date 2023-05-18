package com.example.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder>{
    List<ItemModel> itemModelList = new ArrayList<>();
    FragmentManager fragmentManager;
    Context context;
    Activity activity;
    OnItemClick onItemClick;
    public ItemRecyclerAdapter(List<ItemModel> list, OnItemClick onItemClick,Activity activity){
        this.itemModelList = list;
        this.onItemClick = onItemClick;
        this.activity = activity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_view,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
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
        holder.itemDescription.setText(itemModelList.get(pos).getDescription());
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
            itemDescription = itemView.findViewById(R.id.item_description);
        }
    }
    public interface OnItemClick{
        public void itemClicked(ItemModel model);
    }

}
