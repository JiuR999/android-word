package com.swust.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.swust.R;
import com.swust.pojo.Function;

import java.util.List;

public class FuntionAdapter extends BaseAdapter {
    private Context mContext;
    private List<Function> mFunctionList;

    public FuntionAdapter(Context mContext, List<Function> mFunctionList) {
        this.mContext = mContext;
        this.mFunctionList = mFunctionList;
    }

    @Override
    public int getCount() {
        return mFunctionList == null ? 0 : mFunctionList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFunctionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FuntionAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_function, parent, false);
            viewHolder = new FuntionAdapter.ViewHolder();
            viewHolder.tvFunTitle = convertView.findViewById(R.id.tv_fun_title);
            viewHolder.tvFunSubtitle = convertView.findViewById(R.id.tv_fun_subtitle);
            viewHolder.imgFunIcon = convertView.findViewById(R.id.img_fun_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FuntionAdapter.ViewHolder) convertView.getTag();
        }
        Function function = mFunctionList.get(position);
        viewHolder.tvFunTitle.setText(function.getTitle());
        viewHolder.tvFunSubtitle.setText(function.getSubtitle());
        viewHolder.imgFunIcon.setImageResource(function.getIcon());
        return convertView;
    }

    static class ViewHolder {
        TextView tvFunTitle;
        TextView tvFunSubtitle;
        ImageView imgFunIcon;
    }
}
