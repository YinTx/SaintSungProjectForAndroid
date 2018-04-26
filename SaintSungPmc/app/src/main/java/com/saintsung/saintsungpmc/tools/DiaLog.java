package com.saintsung.saintsungpmc.tools;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.saintsung.saintsungpmc.R;
import com.saintsung.saintsungpmc.configure.Constant;
import com.saintsung.saintsungpmc.loading.SharedPreferencesUtil;

/**
 * Created by Administrator on 2016/4/8.
 */
public class DiaLog extends Dialog {
    public DiaLog(Context context, Intent intent) {
        super(context);
        this.context = context;
        this.intent = intent;
        this.msg = msg;
    }

    public DiaLog(Context context) {
        super(context);
        this.context = context;
    }

    private Intent intent;
    private Button ipConfirm, ipCancel;
    private Context context;
    private String msg = "";
    private TextView diaMsg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_setip);
        diaMsg = (TextView) findViewById(R.id.dia_msg);
        diaMsg.setText(msg);
        ipCancel = (Button) findViewById(R.id.ip_cancel);
        ipConfirm = (Button) findViewById(R.id.ip_confirm);
        ipCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiaLog.this.dismiss();
            }
        });
        ipConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent == null) {
                    EditText Ed_port = (EditText) findViewById(R.id.port);
                    String str = Ed_port.getText().toString();
                    if (str.equals("")) {
                        Toast.makeText(context, "端口号不能为空！", Toast.LENGTH_SHORT).show();
                    } else {
                        switch (str) {
                            case "89":
                                setPort("89");
                                break;
                            case "98":
                                setPort("98");
                                break;
                            case "102":
                                setPort("102");
                                break;
                            case "103":
                                setPort("103");
                                break;
                            default:
                                Toast.makeText(context,"请输入正确的端口号！",Toast.LENGTH_LONG).show();
                                break;
                        }

                    }
                } else {
                    context.startActivity(intent);
                    DiaLog.this.dismiss();
                }
            }
        });
    }
    private void setPort(String str){
        SharedPreferencesUtil.putSharedPreferences(context, "port", str);
        dismiss();
    }
}
