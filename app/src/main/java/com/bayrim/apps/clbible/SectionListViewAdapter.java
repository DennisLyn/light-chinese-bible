package com.bayrim.apps.clbible;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by dennis on 11/14/2015.
 */
public class SectionListViewAdapter extends BaseAdapter {


    public ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
    Activity activity;
    TextView mSection_num;
    TextView msection_content;

    public SectionListViewAdapter(Activity activity,ArrayList<ArrayList<String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.section_list, null);


            /*
            txtFirst=(TextView) convertView.findViewById(R.id.name);
            txtSecond=(TextView) convertView.findViewById(R.id.gender);
            txtThird=(TextView) convertView.findViewById(R.id.age);
            txtFourth=(TextView) convertView.findViewById(R.id.status);
            */

        }

        ArrayList<String> map=list.get(position);
        if(map!=null) {
            mSection_num=(TextView) convertView.findViewById(R.id.section_num);
            msection_content=(TextView) convertView.findViewById(R.id.section_content);
            mSection_num.setText(map.get(0));
            msection_content.setText(map.get(1));
        }


        /*
        txtFirst.setText(map.get(FIRST_COLUMN));
        txtSecond.setText(map.get(SECOND_COLUMN));
        txtThird.setText(map.get(THIRD_COLUMN));
        txtFourth.setText(map.get(FOURTH_COLUMN));
        */

        return convertView;
    }
}
