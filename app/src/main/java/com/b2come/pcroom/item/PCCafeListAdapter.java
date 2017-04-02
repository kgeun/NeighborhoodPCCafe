package com.b2come.pcroom.item;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KKLee on 2016. 11. 9..
 */

public class PCCafeListAdapter extends BaseAdapter {

    ArrayList<PCCafeItemData> items = new ArrayList<PCCafeItemData>();
    Context mContext;

    public PCCafeListAdapter(Context context) {
        mContext = context;
    }

    public void updateItemOnPosition(PCCafeItemData item, int position)
    {
        items.set(position,item);
        notifyDataSetChanged();
    }

    public void add(PCCafeItemData item)
    {
        items.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<PCCafeItemData> items)
    {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    public List<PCCafeItemData> getAll(){
        return items;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PCCafeItemView v;

        if (convertView == null) {
            v = new PCCafeItemView(mContext);
        } else {
            v = (PCCafeItemView)convertView;
        }

        v.setPCCafeItemData(items.get(position));

        return v;
    }

    void clear()
    {
        items.clear();
    }
}
