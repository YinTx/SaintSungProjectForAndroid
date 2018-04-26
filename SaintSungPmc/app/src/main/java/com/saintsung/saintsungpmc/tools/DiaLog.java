package com.saintsung.saintsungpmc.tools;

import android.app.Dialog;
import android.content.Context;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.saintsung.saintsungpmc.R;
import com.saintsung.saintsungpmc.loading.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2016/4/8.
 */
public class DiaLog extends Dialog {
    private Context context;
    @BindView(R.id.port)
    EditText portEdtext;

    public DiaLog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_setip);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.ip_cancel)
    void ipCancel(){
        DiaLog.this.dismiss();
    }
    @OnClick(R.id.ip_confirm)
    void ipConfirm(){
        String str = portEdtext.getText().toString();
        if (str.equals("")) {
            Toast.makeText(context, context.getResources().getString(R.string.login_errport), Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferencesUtil.putSharedPreferences(context, "Port", str);
            dismiss();
        }
    }
}
