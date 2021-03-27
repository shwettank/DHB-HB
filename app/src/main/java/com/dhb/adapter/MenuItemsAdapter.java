package com.dhb.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.utils.UiUtils;

import java.util.ArrayList;

public class MenuItemsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> listName;
    private ArrayList<Integer> listImage;
    private LayoutInflater inflater;
    private TextView txtItemName;
    Typeface fontOpenRobotoRegular;
    ImageView imgItemImage;

    public MenuItemsAdapter(Context context, ArrayList<String> listName, ArrayList<Integer> listImage) {
        this.context = context;
        this.listName = listName;
        this.listImage = listImage;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fontOpenRobotoRegular = Typeface.createFromAsset(this.context.getAssets(), "fonts/roboto-regular.ttf");

    }

    @Override
    public int getCount() {
        return listName.size();
    }

    @Override
    public Object getItem(int i) {
        return listName.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.item_drawer_list, parent, false);
        txtItemName = (TextView) convertView.findViewById(R.id.txt_list_name);
        imgItemImage = (ImageView) convertView.findViewById(R.id.img_list_item_image);

        txtItemName.setText(listName.get(i));
        txtItemName.setTypeface(UiUtils.getInstance().createTypeFace(context, "roboto-regular.ttf"));
        //imgItemImage.setBackgroundResource(listImage.get(i));
        if (listImage.get(i) != null){
            imgItemImage.setImageResource(listImage.get(i));
        } else {
            imgItemImage.setVisibility(View.INVISIBLE);
        }
        txtItemName.setTypeface(fontOpenRobotoRegular);
        return convertView;
    }
}
