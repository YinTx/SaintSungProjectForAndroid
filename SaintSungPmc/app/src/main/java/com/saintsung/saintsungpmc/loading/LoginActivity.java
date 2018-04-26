package com.saintsung.saintsungpmc.loading;


import android.content.Intent;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.saintsung.common.app.Activity;
import com.saintsung.saintsungpmc.MainActivity;
import com.saintsung.saintsungpmc.R;
import com.saintsung.saintsungpmc.bean.LoginBean;
import com.saintsung.saintsungpmc.bean.LoginDataBean;
import com.saintsung.saintsungpmc.configure.Constant;
import com.saintsung.saintsungpmc.text.RetrofitService;
import com.saintsung.saintsungpmc.tools.MD5;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.ResponseBody;
import rx.functions.Action1;


public class LoginActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    @BindView(R.id.userName)
    EditText userNameText;
    @BindView(R.id.passWord)
    EditText passWordText;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.portSet)
    TextView portSetText;

    //    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        initView();
//        Log.e("LoginActivityTAG", "onCreate");
//    }
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        btnLogin.setOnClickListener(this);
        portSetText.setOnClickListener(this);
        //设置全局常量，IEME
//        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
//        baseApplication.setIEME(DataProcess.ComplementZeor(tm.getDeviceId(),18));
    }

    @Override
    protected void initData() {

    }
//    private void initView() {
//        btnLogin.setOnClickListener(this);
//        portSetText.setOnClickListener(this);
//        //设置全局常量，IEME
//        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
//        baseApplication.setIEME(DataProcess.ComplementZeor(tm.getDeviceId(),18));
//    }

    /**
     * 判断是否存在端口号
     */
    private boolean isPort() {
        String port = SharedPreferencesUtil.getSharedPreferences(LoginActivity.this, "Port", "");
        if (port.equals("")) {
//            getDiaLog();
            return false;
        } else {
//            baseApplication.setPort(port);
            return true;
        }

    }

//    private void getDiaLog() {
//        DiaLog diaLog = new DiaLog(LoginActivity.this);
//        diaLog.show();
//        setWidthAndH(diaLog);
//    }

//    private void setWidthAndH(DiaLog diaLog) {
//        WindowManager windowManager = getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        WindowManager.LayoutParams lp = diaLog.getWindow().getAttributes();
//        lp.width = (int) (display.getWidth()); //设置宽度
//        diaLog.getWindow().setAttributes(lp);
//    }

    /**
     * 登录方法
     */
//    public void login() {
//        Log.d(TAG, "Login");
//        //验证用户名密码的格式是否符合要求
//        if (!validate()) {
//            onLoginFailed(getResources().getString(R.string.login_errlenth));
//            return;
//        }
//        //判断是否有端口号
//        if (!isPort()) {
//            onLoginFailed(getResources().getString(R.string.login_errport));
//            return;
//        }
////        btnLogin.setEnabled(false);
//
//        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
//                R.style.AppTheme_Dark_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage(getResources().getString(R.string.Login_in));
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
////
////        String username = userNameText.getText().toString();
////        String password = passWordText.getText().toString();
////        senDataService(username,password);
//
//
//
//
////        final String parameter = Constant.loginServiceLable + DataProcess.ComplementSpace(username, 10) + DataProcess.ComplementSpace(password, 10) + baseApplication.getIEME();
////
////        asyncTaskConnetion.execute(baseApplication.getPort(), DataProcess.createRequestPacket(parameter));
////        asyncTaskConnetion.getResult(new IGetResultInService() {
////            @Override
////            public void getResultInService(final String s) {
////                if (DataProcess.getLoginReturn(s)) {
////                    baseApplication.setUserId(DataProcess.getUserIdInService(s));
////                    new android.os.Handler().postDelayed(
////                            new Runnable() {
////                                public void run() {
////                                    onLoginSuccess();
////                                    progressDialog.dismiss();
////                                }
////                            }, 500);
////                    SharedPreferencesUtil.putSharedPreferences(LoginActivity.this,"UserNameAndPassword",DataProcess.createRequestPacket(parameter));
////                } else {
////                    new android.os.Handler().postDelayed(
////                            new Runnable() {
////                                public void run() {
////                                    onLoginFailed(DataProcess.getErrorInService(s));
////                                    progressDialog.dismiss();
////                                }
////                            }, 500);
////                }
////                Log.d(TAG, "登录服务器返回：" + s);
////            }
////        });
//
//    }
    private void senDataService(String username, String password) {
        Gson gson = new Gson();
        LoginBean loginBean = new LoginBean();
        LoginDataBean loginDataBean = new LoginDataBean();
        String sing = "";
        loginDataBean.setUserNo(username);
        loginDataBean.setUserPwd(MD5.toMD5(password));
        loginBean.setOptCode("OptUserLogin");
        loginBean.setData(loginDataBean);

        sing = MD5.toMD5(loginBean.getOptCode() + gson.toJson(loginBean.getData()));
        loginBean.setSign(sing);
        RetrofitService retrofitService = new RetrofitService();
        retrofitService.sendRequest(gson.toJson(loginBean), Constant.LoginService, action1);
    }

    private Action1<ResponseBody> action1 = new Action1<ResponseBody>() {

        @Override
        public void call(ResponseBody responseBody) {
            try {
//                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                Log.e("TAG", "" + responseBody.string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {

            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    /**
     * 登录成功运行该方法
     */
    public void onLoginSuccess() {
//        btnLogin.setEnabled(true);
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(intent);
        this.finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    //登录失败的方法
    public void onLoginFailed(String error) {
        Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();
//        btnLogin.setEnabled(true);
    }

//    public boolean validate() {
//        boolean valid = true;
//        String username = userNameText.getText().toString();
//        String password = passWordText.getText().toString();
//        //!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches() 验证该字符串是否为Email格式。
//        if (username.isEmpty() || username.length() < 4 || username.length() > 10) {
//            userNameText.setError(getResources().getString(R.string.Login_usrenameLenthError));
//            valid = false;
//        } else {
//            userNameText.setError(null);
//        }
//
//        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
//            passWordText.setError(getResources().getString(R.string.Login_passwordLenthError));
//            valid = false;
//        } else {
//            passWordText.setError(null);
//        }
//
//        return valid;
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                login();
                break;
            case R.id.portSet:
//                getDiaLog();
                break;
            default:
                break;
        }
    }
}
