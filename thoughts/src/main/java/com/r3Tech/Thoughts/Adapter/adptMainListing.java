package com.r3Tech.Thoughts.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.r3Tech.Thoughts.R;
import com.r3Tech.Thoughts.Utils.clsGeneral;
import com.r3Tech.Thoughts.Utils.entItem;

import java.util.ArrayList;
import java.util.List;


public class adptMainListing extends BaseAdapter {

    // ------------------------------------------------------------------------------------------------------------ Global Declaration
    private Context CONTEXT;
    List<entItem> ITEMS;
    LayoutInflater INFLATER;

    // ------------------------------------------------------------------------------------------------------------ Constructor
    public adptMainListing(Context context) {
        CONTEXT = context;
        ITEMS = new ArrayList<entItem>();
        INFLATER = (LayoutInflater) CONTEXT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // ------------------------------------------------------------------------------------------------------------ Add items in list
    public void addItems(List<entItem> items) {
        for (int _i = 0; _i < items.size(); _i++) {
            ITEMS.add(items.get(_i));
        }
    }

    // ------------------------------------------------------------------------------------------------------------ Remove items from list
    public void remoteItems() {
        ITEMS.clear();
    }

    // ------------------------------------------------------------------------------------------------------------ Add items in list
    public void removeItem(int ID) {
        for (int _i = 0; _i < ITEMS.size(); _i++) {
            if (ITEMS.get(_i).ID == ID) {
                ITEMS.remove(_i);
                break;
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------------ Misc adapter methods
    public int getCount() {
        return ITEMS.size();
    }

    public Object getItem(int position) {
        return ITEMS.get(position);
    }

    public long getItemId(int position) {
        return ITEMS.get(position).ID;
    }

    // ------------------------------------------------------------------------------------------------------------ GetView
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder _holder;

        if (convertView == null) {
            convertView = INFLATER.inflate(R.layout.mainlistingfragmentrowlayout, null);

            _holder = new ViewHolder();
            _holder.TV_ID = (TextView) convertView.findViewById(R.id.tvId);
            _holder.TV_TITLE = (TextView) convertView.findViewById(R.id.tvTitle);
            //_holder.TV_DESC =(TextView) convertView.findViewById(R.id.tvSmallDesc);
            _holder.TV_STATUS = (TextView) convertView.findViewById(R.id.tvStatus);

            // CHnage fonts for Text viewes
            clsGeneral.changeFonts(CONTEXT, _holder.TV_TITLE);
            clsGeneral.changeFonts(CONTEXT, _holder.TV_STATUS);

            convertView.setTag(_holder);
        } else {
            _holder = (ViewHolder) convertView.getTag();
        }
        _holder.TV_ID.setText(String.valueOf(ITEMS.get(position).ID));
        _holder.TV_TITLE.setText(ITEMS.get(position).TITLE);
        //_holder.TV_DESC.setText(ITEMS.get(position).SMALL_DESC);

        String _statusString = ITEMS.get(position).CATEGORY;
        _statusString += "  |  " + ITEMS.get(position).VIEWS + " Views";
        _statusString += "  |  " + ITEMS.get(position).LIKES + " Likes";
        _holder.TV_STATUS.setText(_statusString);

        return convertView;
    }

    // ------------------------------------------------------------------------------------------------------------ View holder
    static class ViewHolder {

        TextView TV_ID;
        TextView TV_TITLE;
        //TextView TV_DESC;
        TextView TV_STATUS;
    }
}
