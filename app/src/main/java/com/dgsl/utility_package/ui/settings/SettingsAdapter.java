package com.dgsl.utility_package.ui.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgsl.utility_package.data.model.SettingListModel;

import java.util.ArrayList;

import utility_package.R;

public class SettingsAdapter extends ArrayAdapter<SettingListModel> {
    public SettingsAdapter(Context context, ArrayList<SettingListModel> settings) {
        super(context, 0, settings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SettingListModel setting = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.setting_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        ImageView imgIcon = convertView.findViewById(R.id.imgIcon);
        // Populate the data into the template view using the data object
        tvName.setText(setting.title);
        imgIcon.setImageResource(setting.imageResourceId);

        // Return the completed view to render on screen
        return convertView;
    }
}