package com.saintsung.saintsungpmc.fragment;


import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import com.saintsung.common.app.Fragment;
import com.saintsung.saintsungpmc.MyApplication;
import com.saintsung.saintsungpmc.R;
import com.saintsung.saintsungpmc.adapter.WorkOrderAdapter;
import com.saintsung.saintsungpmc.bean.WorkOrderDataBean;


import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.OnClick;


/**
 * 蓝牙控制使用GitHub上的框架
 * 框架地址 ：<a href="https://github.com/Jasonchenlijian/FastBle">FastBle项目</a>
 * Created by XLzY on 2017/7/28.
 */

public class MainWorkOrderFragment extends Fragment {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.list_device)
    ListView listDevice;
    @BindView(R.id.tv_work_gone)
    TextView tvWorkGone;
    WorkOrderAdapter adapter = new WorkOrderAdapter(getContext(), null);
    List<WorkOrderDataBean> workOrderDataBeanArr = new ArrayList<>();
    @BindView(R.id.workorder_title)
    TextView workorderTitle;
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_control;
    }

    @Override
    protected void initData() {
        super.initData();
        txtTitle.setText(R.string.workorder_title);
        listDevice.setAdapter(adapter);
        complete();
    }

    @OnClick(R.id.fragment_complete)
    void complete() {
        workorderTitle.setText("已完成工单");
        workOrderDataBeanArr = new ArrayList<>();
        for (WorkOrderDataBean workOrderBean : MyApplication.getWorkOrderBean().getData()) {
            if (workOrderBean.getWorkState().equals("4") || workOrderBean.getWorkState().equals("5")) {
                workOrderDataBeanArr.add(workOrderBean);
            }
        }
        if (workOrderDataBeanArr.size() > 0)
            tvWorkGone.setVisibility(View.GONE);
        else
            tvWorkGone.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.fragment_uncomplete)
    void unComplete() {
        workorderTitle.setText("未完成工单");
        workOrderDataBeanArr = new ArrayList<>();
        for (WorkOrderDataBean workOrderBean : MyApplication.getWorkOrderBean().getData()) {
            if (workOrderBean.getWorkState().equals("3") || workOrderBean.getWorkState().equals("8") || workOrderBean.getWorkState().equals("11") || workOrderBean.getWorkState().equals("12")) {
                workOrderDataBeanArr.add(workOrderBean);
            }
        }
        if (workOrderDataBeanArr.size() > 0)
            tvWorkGone.setVisibility(View.GONE);
        else
            tvWorkGone.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }
}
