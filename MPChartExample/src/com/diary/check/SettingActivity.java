package com.diary.check;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xxmassdeveloper.mpchartexample.R;

/**
 * Created by Administrator on 2016/4/30 0030.
 */
public class SettingActivity extends BaseActivity {


    EditText etUpLimitCH4;
    EditText etUpLimitNH3;
    EditText etUpLimitH2S;
    EditText etDays;
    TextView tvSave;

    public static final String UPLIMITCH4 ="upLimitCH4";
    public static final String UPLIMITNH3 ="upLimitNH3";
    public static final String UPLIMITH2S ="upLimitH2S";
    public static final String DAYS_KEEPED="daysKeeped";
    public static final int UPLIMITCH4_DEFAULT =80;
    public static final int UPLIMITNH3_DEFAULT =70;
    public static final int UPLIMITH2S_DEFAULT =50;
    public static final int DAYS_KEEPED_DEFAULT=7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zrh_setting);
        mTemplateTitleText.setText("设置");
//        mTemplateRightText.setVisibility(View.VISIBLE);
//        mTemplateRightText.setText("设置");
//        mTemplateRightImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        etUpLimitCH4 =(EditText) findViewById(R.id.etUpLimitCH4);
        etUpLimitNH3 =(EditText) findViewById(R.id.etUpLimitNH3);
        etUpLimitH2S =(EditText) findViewById(R.id.etUpLimitH2S);
        etDays=(EditText) findViewById(R.id.etDays);
        String str2=SharedPreferenceUtil.get(SettingActivity.this,DAYS_KEEPED,String.valueOf(DAYS_KEEPED_DEFAULT));
        etDays.setText(str2);

        String str1CH4=SharedPreferenceUtil.get(SettingActivity.this, UPLIMITCH4,String.valueOf(UPLIMITCH4_DEFAULT));
        etUpLimitCH4.setText(str1CH4);
        String str1NH3=SharedPreferenceUtil.get(SettingActivity.this, UPLIMITNH3,String.valueOf(UPLIMITNH3_DEFAULT));
        etUpLimitNH3.setText(str1NH3);
        String str1H2S=SharedPreferenceUtil.get(SettingActivity.this, UPLIMITH2S,String.valueOf(UPLIMITH2S_DEFAULT));
        etUpLimitH2S.setText(str1H2S);

        tvSave=(TextView)findViewById(R.id.tvSave);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1CH4= etUpLimitCH4.getText().toString();
                String str1NH3= etUpLimitNH3.getText().toString();
                String str1H2S= etUpLimitH2S.getText().toString();
                String str2Days=etDays.getText().toString();
                try {
                    int intCH4=Integer.valueOf(str1CH4);
                    int intNH3=Integer.valueOf(str1NH3);
                    int intH2S=Integer.valueOf(str1H2S);
                    int intDays=Integer.valueOf(str2Days);
                    SharedPreferenceUtil.put(SettingActivity.this, UPLIMITCH4,str1CH4);
                    SharedPreferenceUtil.put(SettingActivity.this, UPLIMITNH3,str1NH3);
                    SharedPreferenceUtil.put(SettingActivity.this, UPLIMITH2S,str1H2S);
                    SharedPreferenceUtil.put(SettingActivity.this,DAYS_KEEPED,str2Days);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("请输入整数");
                }
            }
        });
    }
}
