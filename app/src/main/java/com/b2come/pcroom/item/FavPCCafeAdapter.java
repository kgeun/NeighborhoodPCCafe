package com.b2come.pcroom.item;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KKLee on 2016. 11. 20..
 */

public class FavPCCafeAdapter extends BaseAdapter {
    ArrayList<FavPCCafeItemData> items = new ArrayList<FavPCCafeItemData>();
    Context mContext;

    public FavPCCafeAdapter(Context context) {
        mContext = context;
    }

    public void updateItemOnPosition(FavPCCafeItemData item, int position)
    {
        items.set(position,item);
        notifyDataSetChanged();
    }

    public void add(FavPCCafeItemData item)
    {
        items.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<FavPCCafeItemData> items)
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

    public List<FavPCCafeItemData> getAll(){
        return items;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FavPCCafeItemView v;

        if (convertView == null) {
            v = new FavPCCafeItemView(mContext);
        } else {
            v = (FavPCCafeItemView)convertView;
        }

        v.setFavPCCafeItemData(items.get(position));

        return v;
    }

    void clear()
    {
        items.clear();
    }
}
