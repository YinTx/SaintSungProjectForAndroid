package com.saintsung.saintsungpmc.loading;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.saintsung.saintsungpmc.MainActivity;
import com.saintsung.saintsungpmc.R;
import com.saintsung.saintsungpmc.configure.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.userName) EditText userNameText;
    @Bind(R.id.passWord) EditText passWordText;
    @Bind(R.id.btnLogin) Button btnLogin;
    @Bind(R.id.portSet) TextView portSetText;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        portSetText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
//                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
//                startActivityForResult(intent, REQUEST_SIGNUP);
//                finish();
//                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    /**
     * 登录方法
     */
    public void login() {
        Log.d(TAG, "Login");
        //验证用户名密码的格式是否符合要求
        if (!validate()) {
            onLoginFailed();
            return;
        }

        btnLogin.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.Login_in));
        progressDialog.show();

        String email = userNameText.getText().toString();
        String password = passWordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onLoginSuccess();

                        progressDialog.dismiss();
                    }
                }, 3000);
    }


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

    public void onLoginSuccess() {
        btnLogin.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        this.finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }
    //登录失败的方法
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "登录失败", Toast.LENGTH_LONG).show();
//
        btnLogin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = userNameText.getText().toString();
        String password = passWordText.getText().toString();
        //!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches() 验证该字符串是否为Email格式。
        if (username.isEmpty() || username.length()<4|| username.length()>10) {
            userNameText.setError(getResources().getString(R.string.Login_usrenameLenthError));
            valid = false;
        } else {
            userNameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passWordText.setError(getResources().getString(R.string.Login_passwordLenthError));
            valid = false;
        } else {
            passWordText.setError(null);
        }

        return valid;
    }
}
