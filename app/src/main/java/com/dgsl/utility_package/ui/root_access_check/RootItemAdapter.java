package com.dgsl.utility_package.ui.root_access_check;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgsl.utility_package.data.model.SettingListModel;

import java.util.ArrayList;

import utility_package.R;

public class RootItemAdapter extends ArrayAdapter<RootItemResult> {
    public RootItemAdapter(Context context, ArrayList<RootItemResult> rootItems) {
        super(context, 0, rootItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RootItemResult rootItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_root_access_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.rootAccessTextView);
        ImageView imgIcon = convertView.findViewById(R.id.rootCheckIconImageView);
        ImageView crossIcon = convertView.findViewById(R.id.rootCheckIconCrossImageView);

        // Populate the data into the template view using the data object
        tvName.setText(rootItem.checkContent);

        if(rootItem.status) {
            imgIcon.setVisibility(View.GONE);
            crossIcon.setVisibility(View.VISIBLE);
        }else{
            imgIcon.setVisibility(View.VISIBLE);
            crossIcon.setVisibility(View.GONE);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
