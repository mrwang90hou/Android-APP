/**
 *
 */
package com.diary.check;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xxmassdeveloper.mpchartexample.R;


/**
 *
 */
public class BaseActivity extends AppCompatActivity implements
		OnClickListener {

	protected FragmentManager mFragmentManager;


	private ViewGroup mBaseActivityContainer;
	protected View mTemplateTitleBarLayout; // 标题栏总布局
	protected ImageView mTemplateLeftImg; // 若左边按钮为图片 用该控件 并设置leftText隐藏
	protected ImageView mTemplateRightImg; // 若右边边按钮为图片 用该控件 并设置rightText隐藏
	protected TextView mTemplateLeftText; // 若左边按钮钮为文字 用该控件 并设置leftImg隐藏
	protected TextView mTemplateRightText;// 若右边按钮钮为文字 用该控件 并设置rightImg隐藏
	protected TextView mTemplateTitleText;
	protected FrameLayout mTemplateContainer;

	protected boolean isTemplate = true; // 是否使用模板
	protected boolean isAdjustPan=false;
	protected boolean isImmersiveStatusBar =true;
	@Override
	protected void onCreate(Bundle arg0) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(arg0);
//		StatusBarCompat.compat(this, getResources().getColor(R.color.text_orange));
//		CarJobUtils.addAppActivity(this);
		mFragmentManager = getSupportFragmentManager();
		setContentView(R.layout.template);
		//解决沉浸式状态栏的键盘冲突问题
//		if(isAdjustPan){
//			AndroidBug5497Workaround.assistActivity(this);
//		}
		initTemplate();
		//沉浸式状态栏去掉了 和输入框的冲突问题不好解决
		if(isImmersiveStatusBar){
			int color=getResources().getColor(R.color.text_top_status_color);
			setStatusBarColor(color);
		}
	}
	boolean isStatusBarAdded=false;
	public void setStatusBarColor(int color) {
		if (isStatusBarAdded){
			StatusBarCompat.resetStatusBar(this);
		}
		//4.4版本不再处理
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
//			if(isStatusBarAdded){
//				mBaseActivityContainer.removeViewAt(0);
//			}
//			View statusBarView = new View(this);
////		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, tintManager.getConfig().getStatusBarHeight());
//			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,StatusBarCompat.getStatusBarHeight(this));
//			statusBarView.setBackgroundColor(color);
//			LogUtils.i("statusBarheight==" + StatusBarCompat.getStatusBarHeight(this));
//			mBaseActivityContainer.addView(statusBarView,0,lp);
//
//		}
		StatusBarCompat.compat(this, color);
		isStatusBarAdded=true;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		CarJobUtils.removeAppActivity(this);
	}
	/** 简化findView操作，无需每次都去强制转换
	 * @param id
	 * @return
	 */
	protected <T extends View>  T getViewById(int id) {
		return (T) findViewById(id);
	}
	/** 简化findView操作，无需每次都去强制转换
	 * @param id
	 * @return
	 */
	protected <T extends View>  T getViewById(View v,int id) {
		return (T) v.findViewById(id);
	}
	private void initTemplate() {
		mTemplateTitleBarLayout = (View) findViewById(R.id.titleview); // 标题栏总布局
		mTemplateContainer = (FrameLayout) findViewById(R.id.view_mainBody);
		mTemplateLeftImg = (ImageView) findViewById(R.id.title_left_btn_img);
		mTemplateRightImg = (ImageView) findViewById(R.id.title_right_btn_img);
		mTemplateLeftText = (TextView) findViewById(R.id.title_left_tv);
		mTemplateRightText = (TextView) findViewById(R.id.title_right_tv);
		mTemplateTitleText = (TextView) findViewById(R.id.title_middle_tv);
		mTemplateRightImg.setVisibility(View.GONE);
		mTemplateRightText.setVisibility(View.GONE);
		if (!isTemplate) {
			mTemplateTitleBarLayout.setVisibility(View.GONE);
			return;
		}
		mTemplateLeftImg.setOnClickListener(this);

	}


	public void showToast(String message) {
		if (message == null) {
			return;
		}
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void setContentView(int layoutResID) {
		if (layoutResID == R.layout.template) {
			mBaseActivityContainer =(ViewGroup) LayoutInflater.from(this).inflate(
					layoutResID, null);
			super.setContentView(mBaseActivityContainer);
		} else {
			mTemplateContainer.removeAllViews();
			View inflate = this.getLayoutInflater().inflate(layoutResID, null);
			inflate.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
			mTemplateContainer.addView(inflate);
		}

	}

	@Override
	public void setContentView(View view) {
		setContentView(view, null);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		if (mTemplateContainer != null) {
			mTemplateContainer.removeAllViews();
			if (params != null) {
				mTemplateContainer.addView(view, params);
			} else {
				mTemplateContainer.addView(view);
			}
		} else {
			super.setContentView(view, params);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_left_btn_img:
				onBackPressed();
//			overridePendingTransition(R.anim.push_left_out,
//					R.anim.push_left_in);
				break;

			default:
				break;
		}
	}

//	@Override
//	public void onBackPressed() {
//		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//		if (imm.hideSoftInputFromWindow(mTemplateTitleText.getWindowToken(), 0)) {
//		} else {
//			super.onBackPressed();
//		}
//	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		// onresume时，取消notification显示
//		EaseUI.getInstance().getNotifier().reset();
//		if (CarApplication.sUMSwitch) {
//			MobclickAgent.onResume(this);
//		}
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		if (CarApplication.sUMSwitch) {
//			MobclickAgent.onPause(this);
//		}
//	}
//	public boolean isLogin(){
//		boolean isLogin=TextUtil.stringIsNotNull(CarApplication.getUserId())
//				&& EMChat.getInstance().isLoggedIn();//登录环信
//		if(!isLogin){
//			Intent intent=new Intent(this,LoginActivity.class);
//            intent.putExtra(CarConstans.IS_FROM_HELP, "1");
//            startActivity(intent);
//		}
//		return isLogin;
//	}
}
