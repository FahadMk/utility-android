package com.dgsl.utility_package.ui.ui_theming;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dgsl.utility_package.data.model.ColorListModel;

import java.util.List;

import utility_package.R;

public class colorGradientListAdapter extends RecyclerView.Adapter<colorGradientListAdapter.MyViewHolder> {
    private List<ColorListModel> colorListModelList;
    public int selectedItemPosition = -1;
    public boolean gradientToggle = false;
    private GradientClickListener<ColorListModel> clickListener;
    class MyViewHolder extends RecyclerView.ViewHolder {
        View colorView;
        CardView cardview;
        ImageView checkIcon;
        RelativeLayout checkView;
        MyViewHolder(View view) {
            super(view);
            colorView = view.findViewById(R.id.colorGradientTextView);
            checkView = view.findViewById(R.id.GradientCheckView);
            cardview = view.findViewById(R.id.GradientcarView);
        }
    }
    public colorGradientListAdapter(List<ColorListModel> colorListModelList) {
        this.colorListModelList = colorListModelList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_list_item, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ColorListModel color = colorListModelList.get(position);
        GradientDrawable drawable = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT, new int[] { Color.parseColor(color.getColorCode()), Color.parseColor(color.getColorVariantCode())
        });
        drawable.setCornerRadius(0f);
        holder.colorView.setBackground(drawable);


        if(selectedItemPosition == position && selectedItemPosition != -1){
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
    public void setOnItemClickListener(GradientClickListener<ColorListModel> movieClickListener) {
        this.clickListener = movieClickListener;
    }
}

interface GradientClickListener<T> {
    void onItemClick(T data);
}