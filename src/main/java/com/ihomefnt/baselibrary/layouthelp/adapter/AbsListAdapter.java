package com.ihomefnt.baselibrary.layouthelp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsListAdapter<T> extends BaseAdapter {

    List<T> mDataList = new ArrayList<T>();
    protected Context mContext;

    public AbsListAdapter(Context context) {
        mContext = context;
    }

    public void setDataList(List<T> list) {
        if (list == null) {
            mDataList = new ArrayList<T>();
        } else {
            mDataList = list;
        }
        notifyDataSetChanged();
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public void appendList(List<T> list) {
        if (mDataList == null) {
            mDataList = new ArrayList<T>();
        }
        mDataList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public abstract View getView(int pos, View convertView, ViewGroup viewGroup);

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public T getItem(int pos) {
        return (pos < getCount() ? mDataList.get(pos) : null);
    }

    @Override
    public int getCount() {
        return mDataList != null ? mDataList.size() : 0;
    }
}
