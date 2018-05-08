package com.diary.check;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.xxmassdeveloper.mpchartexample.R;

/**
 * Created by Administrator on 2016/5/1 0001.
 */
public class WelcomeActivity extends Activity {

    TextView tvWelcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zrh_welcome);
        tvWelcome= (TextView) findViewById(R.id.tvWelcome);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
            aa.setDuration(3000);
        aa.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
               String uName= SharedPreferenceUtil.get(WelcomeActivity.this,MyApplication.UNAME,"");
                Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
                if(TextUtil.stringIsNotNull(uName)){
                    intent=new Intent(WelcomeActivity.this,HomeActivity.class);
                }
                startActivity(intent);
               finish();
            }
        });
        tvWelcome.startAnimation(aa);
    }
}
