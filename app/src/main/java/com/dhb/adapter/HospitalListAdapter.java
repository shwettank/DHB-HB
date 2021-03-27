package com.dhb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.models.HealthCareFirm;

import java.util.ArrayList;

public class HospitalListAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    Context context;
    ArrayList<HealthCareFirm> districtModelArrayList;
    String selectedString;

    public HospitalListAdapter(Context context, ArrayList<HealthCareFirm> districtModelArrayList, String selectedData) {

        this.context = context;
        this.districtModelArrayList = districtModelArrayList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.selectedString = selectedData;

    }

    public void setSelectedString(String selectedString){
        this.selectedString = selectedString;
    }

    @Override
    public int getCount() {
        return districtModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return districtModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_name_image,
                    parent, false);

            holder.txtStateName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.imgSelector = (ImageView) convertView.findViewById(R.id.img_tick);

            setFont(holder);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setData(holder, position);
        return convertView;

    }

    private void setFont(ViewHolder holder) {
        //holder.txtName.setTypeface();
    }

    public class ViewHolder {
        TextView txtStateName;
        ImageView imgSelector;
    }

    public void setData(ViewHolder viewHolder, int position) {
        viewHolder.txtStateName.setText(districtModelArrayList.get(position).getName().toString());
        if (districtModelArrayList.get(position).getName().toString().equalsIgnoreCase(selectedString)){
            viewHolder.imgSelector.setImageResource(R.drawable.bullet_click);
        } else {
            viewHolder.imgSelector.setImageResource(R.drawable.bullet);
        }

    }

}
