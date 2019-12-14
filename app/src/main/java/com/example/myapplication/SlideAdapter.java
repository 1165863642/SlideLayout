package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SlideAdapter extends BaseAdapter implements SlideLayout.OnStateChangeListener {

    private Context context;
    private List<String> dataList;

    public SlideAdapter(Context context, List<String> dataLists) {
        this.dataList = dataLists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public String getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.slide_layout, null);
            convertView.setTag(new SlideHolder(convertView));
        }

        SlideHolder holder = (SlideHolder) convertView.getTag();

        holder.textView1.setText(dataList.get(i));
        holder.textView2.setText(dataList.get(i));
        holder.textView3.setText(getItem(i));

        SlideLayout slideLayout = (SlideLayout) convertView;
        slideLayout.setOnStateChangeListener(this);
        return convertView;
    }

    SlideLayout slideLayout = null;

    @Override
    public void onOpen(SlideLayout slideLayout) {
        if (this.slideLayout == slideLayout)
            this.slideLayout.closeMenu();
        else
            this.slideLayout = slideLayout;
    }

    @Override
    public void onMove(SlideLayout slideLayout) {
        if (this.slideLayout != slideLayout && this.slideLayout != null)
            this.slideLayout.closeMenu();
    }

    @Override
    public void onClose(SlideLayout slideLayout) {
        if (this.slideLayout == slideLayout)
            this.slideLayout = null;
    }

    class SlideHolder {
        private TextView textView1, textView2, textView3;

        public SlideHolder(View view) {
            textView1 = view.findViewById(R.id.item5_user);
            textView2 = view.findViewById(R.id.item5_name);
            textView3 = view.findViewById(R.id.item5_tel);
        }
    }
}
