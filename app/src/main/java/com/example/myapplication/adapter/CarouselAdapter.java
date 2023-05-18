package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.CarouselModel;

import java.util.ArrayList;
import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ViewHolder>{

    List<CarouselModel> carouselList = new ArrayList<>();
    Context context;
    public CarouselAdapter(List<CarouselModel> list, Context context){
        this.carouselList = list;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_carousel_recycler,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
           holder.carouselText.setText(carouselList.get(position).getText());
           int source = context.getResources().getIdentifier(carouselList.get(position).getImgUrl(),"drawable",context.getPackageName());
           holder.carouselImg.setImageDrawable(context.getResources().getDrawable(source));
    }

    @Override
    public int getItemCount() {
        return carouselList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView carouselImg;
        TextView carouselText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carouselImg = itemView.findViewById(R.id.carousel_img);
            carouselText = itemView.findViewById(R.id.textview_carousel);
        }
    }
}
