package com.saintsung.saintsungpmc.tools;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.saintsung.saintsungpmc.R;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2016/4/8.
 */
public class DiaLog extends Dialog {
    private Context context;
    SharedPreferences myPortSharedPreferences;
    @Bind(R.id.ip_confirm)
    Button ipConfirm;
    @Bind(R.id.ip_cancel)
    Button ipCancel;
    @Bind(R.id.port)
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
        ipCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiaLog.this.dismiss();
            }
        });
        ipConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPortSharedPreferences = context.getSharedPreferences("port", Context.MODE_PRIVATE);
                String str = portEdtext.getText().toString();
                if (str.equals("")) {
                    Toast.makeText(context, context.getResources().getString(R.string.login_errport), Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = myPortSharedPreferences.edit();
                    editor.putString("port", str);
                    editor.commit();
                    dismiss();
                }

            }
        });
    }
}
