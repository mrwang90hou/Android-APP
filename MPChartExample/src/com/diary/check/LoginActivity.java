package com.diary.check;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xxmassdeveloper.mpchartexample.R;

import net.tsz.afinal.FinalDb;

/**
 * Created by Administrator on 2016/4/30 0030.
 */
public class LoginActivity extends BaseActivity {


    TextView tvRegister;
    TextView tvLogin;

    EditText etUname,etPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zrh_register);
        tvRegister= (TextView) findViewById(R.id.tvRegister);
        tvLogin= (TextView) findViewById(R.id.tvLogin);

        etUname= (EditText) findViewById(R.id.etUname);
        etPwd= (EditText) findViewById(R.id.etPwd);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName=etUname.getText().toString();
                String pwd=etPwd.getText().toString();
                if (isVerifiedUname(uName)&&isVerifiedPwd(pwd)){
                    SharedPreferenceUtil.put(LoginActivity.this,MyApplication.UNAME,uName);
                    User user=new User(uName,pwd);
                    try {
                        FinalDb finalDb=FinalDb.create(LoginActivity.this);
                        finalDb.save(user);
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast("注册失败，该账号可能已存在");
                        return;
                    }
                    Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this,"注册成功,请登录",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName=etUname.getText().toString();
                String pwd=etPwd.getText().toString();
                if (isVerifiedUname(uName)&&isVerifiedPwd(pwd)){
                    User user=new User(uName,pwd);
                    FinalDb finalDb=FinalDb.create(LoginActivity.this);
                    User userInDB= finalDb.findById(uName,User.class);
                    if(userInDB==null){
                        showToast("该账号不存在");
                    }else if(user.equals(userInDB)){
                        SharedPreferenceUtil.put(LoginActivity.this,MyApplication.UNAME,uName);
                        Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        showToast("密码不正确");
                    }
                }
            }
        });
    }
    boolean isVerifiedUname(String uName){
        if(TextUtil.stringIsNull(uName)){
            showToast("账号不能为空");
            return false;
        }
        return true;
    }
    boolean isVerifiedPwd(String pwd){
        if(TextUtil.stringIsNull(pwd)){
            showToast("密码不能为空");
            return false;
        }
        return true;
    }

}
