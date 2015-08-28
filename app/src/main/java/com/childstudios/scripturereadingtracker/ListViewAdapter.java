package com.childstudios.scripturereadingtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jonathan on 28/8/2015.
 */
public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> activity;

    public ListViewAdapter(Context context, ArrayList<String> activity) {
        this.context = context;
        this.activity = activity;
    }


    @Override
    public int getCount() {
        return activity.size();
    }

    @Override
    public Object getItem(int position) {
        return activity.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_list, null);
        }

        TextView title = (TextView)convertView.findViewById(R.id.title);
        title.setText(activity.get(position));


        return convertView;
    }

}
