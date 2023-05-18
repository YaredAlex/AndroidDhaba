package com.example.database.adapter;

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

import com.example.database.R;
import com.example.database.model.ItemModel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    List<ItemModel> itemModelList = new ArrayList<>();
    FragmentManager fragmentManager;
    Context context;
    Activity activity;
    OnItemClick onItemClick;
    public ItemAdapter(List<ItemModel> list,OnItemClick onItemClick){
        this.itemModelList = list;
        this.onItemClick = onItemClick;
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
             holder.itemName.setText(itemModelList.get(position).getItemName());
             holder.itemDescription.setText(itemModelList.get(position).getDescription());
             holder.itemPrice.setText(String.valueOf(itemModelList.get(position).getItemPrice()));
             holder.itemProvider.setText(itemModelList.get(position).getProvider());
             new DownloadImage(holder.itemImage).execute(itemModelList.get(position).getImgUri());
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
            itemDescription = itemView.findViewById(R.id.item_description);
            itemImage = itemView.findViewById(R.id.item_img);
            itemProvider = itemView.findViewById(R.id.provider_name);
            cardHolder = itemView.findViewById(R.id.card_holder);
        }
    }
    public interface OnItemClick{
        public void itemClicked(ItemModel model);
    }
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView image;
        DownloadImage(ImageView image){
            this.image = image;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
// Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
// Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            image.setImageBitmap(result);
        }
    }
}
