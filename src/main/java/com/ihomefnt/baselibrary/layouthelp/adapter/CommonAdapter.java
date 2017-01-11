package com.ihomefnt.baselibrary.layouthelp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiaMingYu on 2016/12/30.
 */
public abstract class CommonAdapter<T,E extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<E> {
    List<T> mDataList = new ArrayList<T>();
    protected Context mContext;

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public CommonAdapter(Context mContext) {
        this.mContext = mContext;
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
    public abstract E onCreateViewHolder(ViewGroup parent, int viewType);


    @Override
    public abstract void onBindViewHolder(E holder, int position);

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public T getItem(int pos) {
        return (pos < getItemCount() ? mDataList.get(pos) : null);
    }
}
