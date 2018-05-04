package com.saintsung.saintsungpmc.fragment;


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
    WorkOrderAdapter adapter = new WorkOrderAdapter(getContext(),null);
    List<WorkOrderDataBean> workOrderDataBeanArr=new ArrayList<>();
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_control;
    }

    @Override
    protected void initData() {
        super.initData();
        txtTitle.setText(R.string.workorder_title);

        listDevice.setAdapter(adapter);
    }

    @OnClick(R.id.fragment_complete)
    void complete() {
        workOrderDataBeanArr=new ArrayList<>();
        for (WorkOrderDataBean workOrderBean : MyApplication.getWorkOrderBean().getData()) {
            if(workOrderBean.getWorkState().equals("4") ||workOrderBean.getWorkState().equals("5")){
                workOrderDataBeanArr.add(workOrderBean);
            }
            if(workOrderBean.getWorkState().equals("3") ||workOrderBean.getWorkState().equals("8")||workOrderBean.getWorkState().equals("11")||workOrderBean.getWorkState().equals("12")){

            }
        }
        adapter.notifyDataSetChanged();
    }

    @OnClick()
    void unComplete() {

    }
}
