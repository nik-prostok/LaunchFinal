package com.example.nik.launch;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nik on 19.02.2018.
 */

public class CustomAdapter extends BaseExpandableListAdapter {

    private Context c;
    private ArrayList<Rocket> rockets;
    private LayoutInflater inflater;

    HashMap<ImageView, String> imgMap1 = new HashMap<>();


    public CustomAdapter(Context c, ArrayList<Rocket> rockets){
        this.c = c;
        this.rockets = rockets;
        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return rockets.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 4;
    }

    @Override
    public Object getGroup(int i) {
        return rockets.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return rockets.get(i).getChild(i1);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = inflater.inflate(R.layout.group_item, null);
        }

        Rocket r = (Rocket) getGroup(i);

        TextView nameTv = (TextView) view.findViewById(R.id.textView1);
        ImageView img = (ImageView) view.findViewById(R.id.imageView1);

        String name = r.getRocketName();
        Integer flightNumber = r.getFlightNumber();
        nameTv.setText(new StringBuilder(name + flightNumber + ")"));

        Log.d("N", String.valueOf(i));
        if (imgMap1.get(img) == null) {
            imgMap1.put(img, r.getChild(4));
            new DownloadImageTask(img).execute(rockets.get(i).getChild(4));
            return view;
        }

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if(view == null){
            view = inflater.inflate(R.layout.child_item, null);
        }
        String  child=(String) getChild(i, i1);
        TextView nameTextView = (TextView)view.findViewById(R.id.textView1);
        nameTextView.setText(child);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        switch (i1){
            case 2:
                return true;
            case 3:
                return true;
            default:
                return false;
        }
    }
}
