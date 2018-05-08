package com.diary.check;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xxmassdeveloper.mpchartexample.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/30 0030.
 */
public class HomeActivity extends BaseActivity {


    TextView tvCheck,tvSearch;
    TextView tvLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zrh_home);
        mTemplateRightText.setVisibility(View.VISIBLE);
        mTemplateRightText.setText("设置");
        mTemplateLeftImg.setVisibility(View.GONE);
        mTemplateRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });
        tvCheck=(TextView)findViewById(R.id.tvCheck);
        tvSearch=(TextView)findViewById(R.id.tvSearch);
        tvLogout=(TextView)findViewById(R.id.tvLogout);
        tvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,BarChartActivityMultiDataset.class);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                intent.putExtra(BarChartActivityMultiDataset.DATE_LONG,new Date().getTime());
                startActivity(intent);
            }
        });
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceUtil.remove(HomeActivity.this,MyApplication.UNAME);
                Intent intent=new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
