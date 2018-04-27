package com.saintsung.saintsungpmc.fragment;


import android.widget.ListView;
import android.widget.TextView;


import com.saintsung.common.app.Fragment;
import com.saintsung.saintsungpmc.MyApplication;
import com.saintsung.saintsungpmc.R;
import com.saintsung.saintsungpmc.adapter.WorkOrderAdapter;


import butterknife.BindView;


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
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_control;
    }

    @Override
    protected void initData() {
        super.initData();
        txtTitle.setText(R.string.workorder_title);
        WorkOrderAdapter adapter=new WorkOrderAdapter(getContext(),MyApplication.getWorkOrderBean());
        listDevice.setAdapter(adapter);
    }

}
