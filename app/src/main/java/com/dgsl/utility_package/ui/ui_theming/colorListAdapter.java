package com.dgsl.utility_package.ui.ui_theming;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dgsl.utility_package.data.model.ColorListModel;

import java.util.List;

import utility_package.R;

public class colorListAdapter extends RecyclerView.Adapter<colorListAdapter.MyViewHolder> {
    private List<ColorListModel> colorListModelList;
    public int selectedItemPosition = -1;
    public boolean gradientToggle = false;
    private ClickListener<ColorListModel> clickListener;
    class MyViewHolder extends RecyclerView.ViewHolder {
        View colorView;
        CardView cardview;
        ImageView checkIcon;
        RelativeLayout checkView;
        MyViewHolder(View view) {
            super(view);
            colorView = view.findViewById(R.id.colorTextView);
            checkIcon = view.findViewById(R.id.colorImageView);
            cardview = view.findViewById(R.id.carView);
            checkView = view.findViewById(R.id.checkView);

        }
    }
    public colorListAdapter(List<ColorListModel> colorListModelList) {
        this.colorListModelList = colorListModelList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.color_list_item, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ColorListModel color = colorListModelList.get(position);

        holder.colorView.setBackgroundColor(Color.parseColor(color.getColorCode()));

        if(selectedItemPosition == position){
            holder.checkView.setVisibility(View.VISIBLE);
        }else{
            holder.checkView.setVisibility(View.GONE);
        }
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItemPosition = position;
                clickListener.onItemClick(color);
            }
        });

    }
    @Override
    public int getItemCount() {
        return colorListModelList.size();
    }
    public void setOnItemClickListener(ClickListener<ColorListModel> movieClickListener) {
        this.clickListener = movieClickListener;
    }
}
interface ClickListener<T> {
    void onItemClick(T data);
}