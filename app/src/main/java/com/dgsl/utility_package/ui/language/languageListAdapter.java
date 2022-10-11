package com.dgsl.utility_package.ui.language;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import utility_package.R;

public class languageListAdapter  extends ArrayAdapter<String> {
    public int selectedItemPosition = -1;
    public languageListAdapter(Context context, String[] languages) {
        super(context, 0, languages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String language = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.language_list_item, parent, false);
            TextView tvlanguageName = (TextView) convertView.findViewById(R.id.languageTextView);
            ImageView tvcheckImage = (ImageView) convertView.findViewById(R.id.languageImageView);
            if(selectedItemPosition == position){
                tvcheckImage.setVisibility(View.VISIBLE);
                convertView.setBackgroundResource(R.color.light_blue);
            }else{
                convertView.setBackgroundResource(R.color.white);
                tvcheckImage.setVisibility(View.GONE);
            }
            // Populate the data into the template view using the data object
            tvlanguageName.setText(language);
            // Return the completed view to render on screen

        }
        // Lookup view for data population
        return convertView;
    }
}

