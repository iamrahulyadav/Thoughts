package com.r3Tech.Thoughts.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.r3Tech.Thoughts.R;

public class MenuListAdapter extends BaseAdapter {

    // Declare Variables
    private Context CONTEXT;
    private String[] ARR_TITLE;
    private String[] ARR_SUBTITLE;
    private int[] ARR_ICON;
    private LayoutInflater INFLATOR;

    public MenuListAdapter(Context context, String[] title, String[] subtitle,
                           int[] icon) {
        this.CONTEXT = context;
        this.ARR_TITLE = title;
        this.ARR_SUBTITLE = subtitle;
        this.ARR_ICON = icon;
    }

    @Override
    public int getCount() {
        return ARR_TITLE.length;
    }

    @Override
    public Object getItem(int position) {
        return ARR_TITLE[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView txtTitle;
        TextView txtSubTitle;
        ImageView imgIcon;

        INFLATOR = (LayoutInflater) CONTEXT
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = INFLATOR.inflate(R.layout.drawer_list_item, parent,
                false);

        txtTitle = (TextView) itemView.findViewById(R.id.title);
        txtSubTitle = (TextView) itemView.findViewById(R.id.subtitle);

        imgIcon = (ImageView) itemView.findViewById(R.id.icon);

        txtTitle.setText(ARR_TITLE[position]);
        txtSubTitle.setText(ARR_SUBTITLE[position]);

        imgIcon.setImageResource(ARR_ICON[position]);

        return itemView;
    }
}
