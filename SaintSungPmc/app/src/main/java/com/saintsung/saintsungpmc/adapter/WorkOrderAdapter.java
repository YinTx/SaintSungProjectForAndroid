package com.saintsung.saintsungpmc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.saintsung.saintsungpmc.R;
import com.saintsung.saintsungpmc.bean.WorkOrderBean;

/**
 * Created by EvanShu on 2018/4/27.
 */

public class WorkOrderAdapter extends BaseAdapter {

    private Context context;
    private WorkOrderBean workOrderNumber;

    public WorkOrderAdapter(Context context, WorkOrderBean workOrderNumber) {
        this.context = context;
        this.workOrderNumber = workOrderNumber;
    }

    @Override
    public int getCount() {
        if (workOrderNumber == null)
            return 0;
        else
            return workOrderNumber.getData().size();
    }

    @Override
    public Object getItem(int i) {
        return workOrderNumber.getData().size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(context, R.layout.fragment_list_item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.workorderNumber = (TextView) convertView.findViewById(R.id.workorder_number);
        }
        holder.workorderNumber.setText(workOrderNumber.getData().get(position).getWorkOrderNo());
        return convertView;
    }

    class ViewHolder {
        TextView workorderNumber;
    }

}
