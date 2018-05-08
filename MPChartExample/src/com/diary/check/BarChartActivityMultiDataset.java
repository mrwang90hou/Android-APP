
package com.diary.check;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.xxmassdeveloper.mpchartexample.R;
import com.xxmassdeveloper.mpchartexample.custom.MyMarkerView;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

import net.tsz.afinal.FinalDb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class BarChartActivityMultiDataset extends DemoBase implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private BarChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    private TextView tvState;
    PtrClassicFrameLayout mPtrFrame;

    private Typeface tf;
    private int upLimitCH4 =80;
    private int upLimitNH3 =70;
    private int upLimitH2S =50;
    String dateStr;
    public static final String DATE_LONG="dateLong";
//    ArrayList<String>sensorListString;
    ArrayList<String> xVals ;
    ArrayList<Integer> sensorIndexList ;
    public static final String IS_SHOW_CH4="isShowCH4";
    public static final String IS_SHOW_NH3="isShowNH3";
    public static final String IS_SHOW_H2S="isShowH2S";
    boolean isShowCH4;
    boolean isShowNH3;
    boolean isShowH2S;

    boolean isRefresh=false;

    long dateLong;

    int showIndex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_zrh_barchart);

        isShowCH4 =getIntent().getBooleanExtra(IS_SHOW_CH4,true);
        isShowNH3=getIntent().getBooleanExtra(IS_SHOW_NH3,true);
        isShowH2S=getIntent().getBooleanExtra(IS_SHOW_H2S,true);

        String str1CH4=SharedPreferenceUtil.get(this,SettingActivity.UPLIMITCH4,String.valueOf(SettingActivity.UPLIMITCH4_DEFAULT));
        String str1NH3=SharedPreferenceUtil.get(this,SettingActivity.UPLIMITNH3,String.valueOf(SettingActivity.UPLIMITNH3_DEFAULT));
        String strH2S=SharedPreferenceUtil.get(this,SettingActivity.UPLIMITH2S,String.valueOf(SettingActivity.UPLIMITH2S_DEFAULT));
//        String str2=SharedPreferenceUtil.get(LineChartActivity2.this,SettingActivity.DAYS_KEEPED,String.valueOf(SettingActivity.DAYS_KEEPED_DEFAULT));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dateLong=getIntent().getLongExtra(DATE_LONG,System.currentTimeMillis());

        Date date=new Date(dateLong);
        dateStr=sdf.format(date);
        try {
            dateLong=sdf.parse(dateStr).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        getIntent().getLongExtra()
        try {
            upLimitCH4 =Integer.valueOf(str1CH4);
            upLimitNH3 =Integer.valueOf(str1NH3);
            upLimitH2S =Integer.valueOf(strH2S);
        } catch (Exception e) {
            e.printStackTrace();
        }
        xVals=getIntent().getStringArrayListExtra(SearchActivity.SENSOR_LIST_STRING);
        sensorIndexList=getIntent().getIntegerArrayListExtra(SearchActivity.SENSOR_INDEX_LIST_INTEGER);
//        getIntent().getin
        if(xVals==null){
            xVals = new ArrayList<String>();
            sensorIndexList=new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                xVals.add((i+1) + "号");
                sensorIndexList.add(i+1);
            }
        }
        mPtrFrame= (PtrClassicFrameLayout) findViewById(R.id.fragment_rotate_header_with_view_group_frame);
        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
// default is false
        mPtrFrame.setPullToRefresh(false);
// default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.setLastUpdateTimeKey("lastUpDateTime");
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                        isRefresh=true;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        mChart.setDescription(sdf.format(new Date())+" 有毒气体浓度柱状图");
                        resetData();
                    }
                }, 1800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if(System.currentTimeMillis()-dateLong<24*60*60*1000){//当天的能刷新
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }else {
                    return false;
                }
//                return false;
            }
        });



        tvX = (TextView) findViewById(R.id.tvXMax);
        tvY = (TextView) findViewById(R.id.tvYMax);
        tvState=(TextView)findViewById(R.id.tvState);

        mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
        mSeekBarX.setOnSeekBarChangeListener(this);

        mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);
        mSeekBarY.setOnSeekBarChangeListener(this);

        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDescription(dateStr+"有毒气体浓度柱状图");

//        mChart.setDrawBorders(true);
        
        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawBarShadow(false);

        mChart.setDrawGridBackground(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

        // define an offset to change the original position of the marker
        // (optional)
        // mv.setOffsets(-mv.getMeasuredWidth() / 2, -mv.getMeasuredHeight());

        // set the marker to the chart
        mChart.setMarkerView(mv);

        mSeekBarX.setProgress(5);
        mSeekBarY.setProgress(100);

        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART_INSIDE);
        l.setTypeface(tf);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xl = mChart.getXAxis();
        xl.setTypeface(tf);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(tf);
//        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        if(isShowCH4){
            LimitLine ll1 = new LimitLine(upLimitCH4, "CH4预警值");
            ll1.setLineWidth(4f);
            ll1.enableDashedLine(10f, 10f, 0f);
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(10f);
            ll1.setTypeface(tf);
            leftAxis.addLimitLine(ll1);
        }

        if(isShowNH3){
            LimitLine ll2 = new LimitLine(upLimitNH3, "NH3预警值");
            ll2.setLineWidth(4f);
            ll2.enableDashedLine(10f, 10f, 0f);
            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll2.setTextSize(10f);
            ll2.setTypeface(tf);
            leftAxis.addLimitLine(ll2);
        }
        if(isShowH2S){
            LimitLine ll3 = new LimitLine(upLimitH2S, "H2S预警值");
            ll3.setLineWidth(4f);
            ll3.enableDashedLine(10f, 10f, 0f);
            ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll3.setTextSize(10f);
            ll3.setTypeface(tf);
            leftAxis.addLimitLine(ll3);
        }

        mChart.getAxisRight().setEnabled(false);
        mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);

//        handler=new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what){
//                    case 1:
//                        resetData();
//                        handler.sendEmptyMessageDelayed(1, 2000);
//                        break;
//                }
//            }
//        };
//        handler.sendEmptyMessageDelayed(1,2000);
    }
    static Handler handler;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler!=null){
            handler.removeMessages(1);
            handler=null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                for (IBarDataSet set : mChart.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

                mChart.invalidate();
                break;
            }
            case R.id.actionTogglePinch: {
                if (mChart.isPinchZoomEnabled())
                    mChart.setPinchZoom(false);
                else
                    mChart.setPinchZoom(true);

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleAutoScaleMinMax: {
                mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
                mChart.notifyDataSetChanged();
                break;
            }
            case R.id.actionToggleBarBorders: {
                for (IBarDataSet set : mChart.getData().getDataSets())
                    ((BarDataSet)set).setBarBorderWidth(set.getBarBorderWidth() == 1.f ? 0.f : 1.f);

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleHighlight: {
                if(mChart.getData() != null) {
                    mChart.getData().setHighlightEnabled(!mChart.getData().isHighlightEnabled());
                    mChart.invalidate();
                }
                break;
            }
            case R.id.actionToggleHighlightArrow: {
                if (mChart.isDrawHighlightArrowEnabled())
                    mChart.setDrawHighlightArrow(false);
                else
                    mChart.setDrawHighlightArrow(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionSave: {
                // mChart.saveToGallery("title"+System.currentTimeMillis());
                mChart.saveToPath("title" + System.currentTimeMillis(), "");
                break;
            }
            case R.id.animateX: {
                mChart.animateX(3000);
                break;
            }
            case R.id.animateY: {
                mChart.animateY(3000);
                break;
            }
            case R.id.animateXY: {

                mChart.animateXY(3000, 3000);
                break;
            }
        }
        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        resetData();
    }

    private void resetData() {
        tvX.setText("" + (mSeekBarX.getProgress() * 3));
        tvY.setText("" + (mSeekBarY.getProgress()));

//        ArrayList<String> xVals = new ArrayList<String>();
//        for (int i = 0; i < mSeekBarX.getProgress(); i++) {
//            xVals.add((i+1) + "号");
//        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();

//        int mult = mSeekBarY.getProgress();
        ArrayList<StringBuilder>stringBuilders=new ArrayList<>();
        for (int i = 0; i < xVals.size(); i++) {
            StringBuilder sb=new StringBuilder();
            stringBuilders.add(sb);
        }
        int meanCH4 = 0,minCH4=Integer.MAX_VALUE,maxCH4=0;

        FinalDb finalDb=FinalDb.create(this);
        Calendar calendar=Calendar.getInstance();
        showIndex=calendar.get(Calendar.HOUR_OF_DAY);
        if(isShowCH4){
            int gasIndex=DataVo.CH4;
            for (int i = 0; i < xVals.size(); i++) {
//            float val = (float) (Math.random() * mult) + 3;
                int val = (int) (Math.random() * 10) + upLimitCH4-8;

                int sensorIndex=sensorIndexList.get(i);

                DataVo dataVo= finalDb.findById(DataVo.generateId(dateLong,sensorIndex,gasIndex,showIndex),DataVo.class);
                if(dataVo==null){
                    dataVo=new DataVo(dateLong,showIndex,sensorIndex,gasIndex,val);
                    finalDb.save(dataVo);
                }
                val=dataVo.getVal();

                meanCH4+=val;
                if(val<minCH4){
                    minCH4=val;
                }
                if(val>maxCH4){
                    maxCH4=val;
                }
                yVals1.add(new BarEntry(val, i));
                StringBuilder sb=stringBuilders.get(i);
                if(val> upLimitCH4){
                    sb.append(i+1).append("号传感器CH4、");
                }

            }
            meanCH4/=xVals.size();
        }

        int meanNH3 = 0,minNH3=Integer.MAX_VALUE,maxNH3=0;
        if(isShowNH3){
            int gasIndex=DataVo.NH3;
            for (int i = 0; i < xVals.size(); i++) {
                int val = (int) (Math.random() * 10) + upLimitNH3-8;

                int sensorIndex=sensorIndexList.get(i);

                DataVo dataVo= finalDb.findById(DataVo.generateId(dateLong,sensorIndex,gasIndex,showIndex),DataVo.class);
                if(dataVo==null){
                    dataVo=new DataVo(dateLong,showIndex,sensorIndex,gasIndex,val);
                    finalDb.save(dataVo);
                }
                val=dataVo.getVal();

                meanNH3+=val;
                if(val<minNH3){
                    minNH3=val;
                }
                if(val>maxNH3){
                    maxNH3=val;
                }

                yVals2.add(new BarEntry(val, i));
                StringBuilder sb=stringBuilders.get(i);
                if(val> upLimitCH4){
                    if(sb.length()>0){
                        sb.append("NH3、");
                    }else {
                        sb.append(i+1).append("号传感器NH3、");
                    }
                }
            }
            meanNH3/=xVals.size();

        }

        StringBuilder sbText=new StringBuilder();
        int meanH2S = 0,minH2S=Integer.MAX_VALUE,maxH2S=0;
        if(isShowH2S){
            int gasIndex=DataVo.H2S;
            for (int i = 0; i < xVals.size(); i++) {
                int val = (int) (Math.random() * 10) + upLimitH2S-8;

                int sensorIndex=sensorIndexList.get(i);

                DataVo dataVo= finalDb.findById(DataVo.generateId(dateLong,sensorIndex,gasIndex,showIndex),DataVo.class);
                if(dataVo==null){
                    dataVo=new DataVo(dateLong,showIndex,sensorIndex,gasIndex,val);
                    finalDb.save(dataVo);
                }
                val=dataVo.getVal();

                meanH2S+=val;
                if(val<minH2S){
                    minH2S=val;
                }
                if(val>maxH2S){
                    maxH2S=val;
                }

                yVals3.add(new BarEntry(val, i));
                StringBuilder sb=stringBuilders.get(i);
                if(val> upLimitCH4){
                    if(sb.length()>0){
                        sb.append("H2S、");
                    }else {
                        sb.append(i+1).append("号传感器H2S、");
                    }
                }


            }
            meanH2S/=xVals.size();

        }
        for (int i = 0; i < xVals.size(); i++) {
            StringBuilder sb=stringBuilders.get(i);
            if(sb.length()>0){
                sb.deleteCharAt(sb.length()-1);
                sb.append("超标");
            }
            sbText.append(sb).append("\n");
        }
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if(sbText.length()>0){
            if(isRefresh){
                sbText.append(sdf.format(new Date())).append("\n")  ;
            }
        }else {
            if(isRefresh){
                sbText.append(sdf.format(new Date())).append("\n")  ;
            }
           sbText.append("一切正常\n");
        }
        if(isShowCH4){
            sbText
                    .append(String.format("CH4浓度 平均值%s,最大值%s,最小值%s\n",meanCH4,maxCH4,minCH4));

        }
        if(isShowNH3){
            sbText .append(String.format("NH3浓度 平均值%s,最大值%s,最小值%s\n",meanNH3,maxNH3,minNH3));

        }
        if(isShowH2S){
            sbText     .append(String.format("H2S浓度 平均值%s,最大值%s,最小值%s\n",meanH2S,maxH2S,minH2S))
            ;

        }


        tvState.setText(sbText.toString());

        BarDataSet set1, set2, set3;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet)mChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet)mChart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet)mChart.getData().getDataSetByIndex(2);
            set1.setYVals(yVals1);
            set2.setYVals(yVals2);
            set3.setYVals(yVals3);
            mChart.getData().setXVals(xVals);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create 3 datasets with different types
            set1 = new BarDataSet(yVals1, "CH4");
            // set1.setColors(ColorTemplate.createColors(getApplicationContext(),
            // ColorTemplate.FRESH_COLORS));
            set1.setColor(Color.rgb(104, 241, 175));
            set2 = new BarDataSet(yVals2, "NH3");
            set2.setColor(Color.rgb(164, 228, 251));
            set3 = new BarDataSet(yVals3, "H2S");
            set3.setColor(Color.rgb(242, 247, 158));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);
            dataSets.add(set3);

            BarData data = new BarData(xVals, dataSets);
//        data.setValueFormatter(new LargeValueFormatter());

            // add space between the dataset groups in percent of bar-width
            data.setGroupSpace(80f);
            data.setValueTypeface(tf);

            mChart.setData(data);
        }

        mChart.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("Activity", "Nothing selected.");
    }
}
