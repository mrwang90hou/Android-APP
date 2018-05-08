
package com.diary.check;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.xxmassdeveloper.mpchartexample.R;
import com.xxmassdeveloper.mpchartexample.custom.MyMarkerView;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class LineChartActivity1 extends DemoBase implements //OnSeekBarChangeListener,
        OnChartGestureListener, OnChartValueSelectedListener {

    public static final String IS_SHOW_CH4="isShowCH4";
    public static final String IS_SHOW_NH3="isShowNH3";
    public static final String IS_SHOW_H2S="isShowH2S";
    public static final String DATE_LONG="dateLong";

    private LineChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    private TextView tvState;

    int upLimitCH4 =80;
    int upLimitNH3 =70;
    int upLimitH2S =50;

    ArrayList<ArrayList<Entry>> yValsArr=new ArrayList();
    ArrayList<Integer> upLimits=new ArrayList();
    private PtrClassicFrameLayout mPtrFrame;

    public void setyValsArr(ArrayList<ArrayList<Entry>> yValsArr) {
        this.yValsArr = yValsArr;
        setData();
        mChart.invalidate();
    }
    int colors[]={Color.BLACK,Color.RED,Color.BLUE};
//    String datasetTitles[]={"CH4","NH3","H2S"};
    ArrayList<String> datasetTitles=new ArrayList<>();

//    public void setDatasetTitles(String[] datasetTitles) {
//        this.datasetTitles = datasetTitles;
//        setData();
//        mChart.invalidate();
//    }

    ArrayList<String> xVals = new ArrayList<String>();

    public void setxVals(ArrayList<String> xVals) {
        this.xVals = xVals;
        setData();
        mChart.invalidate();
    }

//    public void setColors(int[] colors) {
//        this.colors = colors;
//        setData();
//        mChart.invalidate();
//    }

    private void generateVals(int count, float range){

        xVals.clear();
        for (int i = 0; i < count; i++) {
            xVals.add((i) + "h");
        }
        yValsArr.clear();
        for (int i=0,lenth=datasetTitles.size();i<lenth;i++){
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            int uplimit=upLimits.get(i);
            for (int j = 0; j < count; j++) {

//                float mult = (range + 1);
                int val = (int) (Math.random() * 10) + uplimit-9;// + (float)
                // ((mult *
                // 0.1) / 10);
                yVals.add(new Entry(val, j));
            }
            yValsArr.add(yVals);
        }
    }

    public void setUpLimitCH4(int upLimitCH4) {
        this.upLimitCH4 = upLimitCH4;
        LimitLine ll1 = new LimitLine(upLimitCH4, "预警值");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
//        ll1.setTypeface(tf);

//        LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
//        ll2.setLineWidth(4f);
//        ll2.enableDashedLine(10f, 10f, 0f);
//        ll2.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
//        ll2.setTextSize(10f);
//        ll2.setTypeface(tf);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        mChart.invalidate();
    }
    String dateStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_zrh_chartstatics);

        boolean isShowCH4=getIntent().getBooleanExtra(IS_SHOW_CH4,true);
        boolean isShowNH3=getIntent().getBooleanExtra(IS_SHOW_NH3,true);
        boolean isShowH2S=getIntent().getBooleanExtra(IS_SHOW_H2S,true);

        String str1CH4=SharedPreferenceUtil.get(LineChartActivity1.this,SettingActivity.UPLIMITCH4,String.valueOf(SettingActivity.UPLIMITCH4_DEFAULT));
        String str1NH3=SharedPreferenceUtil.get(LineChartActivity1.this,SettingActivity.UPLIMITNH3,String.valueOf(SettingActivity.UPLIMITNH3_DEFAULT));
        String strH2S=SharedPreferenceUtil.get(LineChartActivity1.this,SettingActivity.UPLIMITH2S,String.valueOf(SettingActivity.UPLIMITH2S_DEFAULT));
//        String str2=SharedPreferenceUtil.get(LineChartActivity2.this,SettingActivity.DAYS_KEEPED,String.valueOf(SettingActivity.DAYS_KEEPED_DEFAULT));
        try {
            upLimitCH4 =Integer.valueOf(str1CH4);
            upLimitNH3 =Integer.valueOf(str1NH3);
            upLimitH2S =Integer.valueOf(strH2S);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final long dateLong=getIntent().getLongExtra(DATE_LONG,System.currentTimeMillis());
        Date date=new Date(dateLong);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dateStr=sdf.format(date);
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
                        generateVals(24, 80);
                        setData();
                    }
                }, 1800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                if(System.currentTimeMillis()-dateLong<24*60*60*1000){//当天的能刷新
//                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
//                }else {
//                    return false;
//                }
                return false;
            }
        });

        tvX = (TextView) findViewById(R.id.tvXMax);
        tvY = (TextView) findViewById(R.id.tvYMax);
        tvState=(TextView)findViewById(R.id.tvState);
        mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
        mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);

        mSeekBarX.setProgress(24);
        mSeekBarY.setProgress(100);

//        mSeekBarY.setOnSeekBarChangeListener(this);
//        mSeekBarX.setOnSeekBarChangeListener(this);

        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        // no description text
        mChart.setDescription(dateStr+"有毒气体浓度折线图");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

        // set the marker to the chart
        mChart.setMarkerView(mv);

        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = mChart.getXAxis();
        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis); // add x-axis limit line

        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        if(isShowCH4){
            datasetTitles.add("CH4");
            upLimits.add(upLimitCH4);
            LimitLine ll1 = new LimitLine(upLimitCH4, "CH4预警值");
            ll1.setLineWidth(4f);
            ll1.enableDashedLine(10f, 10f, 0f);
            ll1.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(10f);
            ll1.setTypeface(tf);

            leftAxis.addLimitLine(ll1);
        }
        if(isShowNH3){
            datasetTitles.add("NH3");
            upLimits.add(upLimitNH3);
            LimitLine ll1 = new LimitLine(upLimitNH3, "NH3预警值");
            ll1.setLineWidth(4f);
            ll1.enableDashedLine(10f, 10f, 0f);
            ll1.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(10f);
            ll1.setTypeface(tf);

            leftAxis.addLimitLine(ll1);
        }
        if(isShowH2S){
            datasetTitles.add("H2S");
            upLimits.add(upLimitH2S);
            LimitLine ll1 = new LimitLine(upLimitH2S, "H2S预警值");
            ll1.setLineWidth(4f);
            ll1.enableDashedLine(10f, 10f, 0f);
            ll1.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(10f);
            ll1.setTypeface(tf);

            leftAxis.addLimitLine(ll1);
        }


//        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaxValue(100f);
        leftAxis.setAxisMinValue(0);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        generateVals(24, 80);
        // add data
        setData();

//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

        mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);
//        mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(LegendForm.LINE);

        // // dont forget to refresh the drawing
        // mChart.invalidate();

//        handler=new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what){
//                    case 1:
//                        generateVals(24, 80);
//                        setData();
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.line, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.actionToggleValues: {
//                List<ILineDataSet> sets = mChart.getData()
//                        .getDataSets();
//
//                for (ILineDataSet iSet : sets) {
//
//                    LineDataSet set = (LineDataSet) iSet;
//                    set.setDrawValues(!set.isDrawValuesEnabled());
//                }
//
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleHighlight: {
//                if(mChart.getData() != null) {
//                    mChart.getData().setHighlightEnabled(!mChart.getData().isHighlightEnabled());
//                    mChart.invalidate();
//                }
//                break;
//            }
//            case R.id.actionToggleFilled: {
//
//                List<ILineDataSet> sets = mChart.getData()
//                        .getDataSets();
//
//                for (ILineDataSet iSet : sets) {
//
//                    LineDataSet set = (LineDataSet) iSet;
//                    if (set.isDrawFilledEnabled())
//                        set.setDrawFilled(false);
//                    else
//                        set.setDrawFilled(true);
//                }
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleCircles: {
//                List<ILineDataSet> sets = mChart.getData()
//                        .getDataSets();
//
//                for (ILineDataSet iSet : sets) {
//
//                    LineDataSet set = (LineDataSet) iSet;
//                    if (set.isDrawCirclesEnabled())
//                        set.setDrawCircles(false);
//                    else
//                        set.setDrawCircles(true);
//                }
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleCubic: {
//                List<ILineDataSet> sets = mChart.getData()
//                        .getDataSets();
//
//                for (ILineDataSet iSet : sets) {
//
//                    LineDataSet set = (LineDataSet) iSet;
//                    set.setMode(set.getMode() == LineDataSet.Mode.CUBIC_BEZIER
//                            ? LineDataSet.Mode.LINEAR
//                            :  LineDataSet.Mode.CUBIC_BEZIER);
//                }
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleStepped: {
//                List<ILineDataSet> sets = mChart.getData()
//                        .getDataSets();
//
//                for (ILineDataSet iSet : sets) {
//
//                    LineDataSet set = (LineDataSet) iSet;
//                    set.setMode(set.getMode() == LineDataSet.Mode.STEPPED
//                            ? LineDataSet.Mode.LINEAR
//                            :  LineDataSet.Mode.STEPPED);
//                }
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleHorizontalCubic: {
//                List<ILineDataSet> sets = mChart.getData()
//                        .getDataSets();
//
//                for (ILineDataSet iSet : sets) {
//
//                    LineDataSet set = (LineDataSet) iSet;
//                    set.setMode(set.getMode() == LineDataSet.Mode.HORIZONTAL_BEZIER
//                            ? LineDataSet.Mode.LINEAR
//                            :  LineDataSet.Mode.HORIZONTAL_BEZIER);
//                }
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionTogglePinch: {
//                if (mChart.isPinchZoomEnabled())
//                    mChart.setPinchZoom(false);
//                else
//                    mChart.setPinchZoom(true);
//
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleAutoScaleMinMax: {
//                mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
//                mChart.notifyDataSetChanged();
//                break;
//            }
//            case R.id.animateX: {
//                mChart.animateX(3000);
//                break;
//            }
//            case R.id.animateY: {
//                mChart.animateY(3000, Easing.EasingOption.EaseInCubic);
//                break;
//            }
//            case R.id.animateXY: {
//                mChart.animateXY(3000, 3000);
//                break;
//            }
//            case R.id.actionSave: {
//                if (mChart.saveToPath("title" + System.currentTimeMillis(), "")) {
//                    Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
//                            Toast.LENGTH_SHORT).show();
//                } else
//                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
//                            .show();
//
//                // mChart.saveToGallery("title"+System.currentTimeMillis())
//                break;
//            }
//        }
//        return true;
//    }

//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//        tvX.setText("" + (mSeekBarX.getProgress() + 1));
//        tvY.setText("" + (mSeekBarY.getProgress()));
//        generateVals(mSeekBarX.getProgress() + 1, mSeekBarY.getProgress());
//        setData();
//
//        // redraw
//        mChart.invalidate();
//    }

//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//        // TODO Auto-generated method stub
//
//    }

    private void setData() {

//        if (mChart.getData() != null &&
//                mChart.getData().getDataSetCount() > 0) {
//            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
//            set1.setYVals(yVals);
//            mChart.getData().setXVals(xVals);
//            mChart.notifyDataSetChanged();
//        } else {

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            for (int i=0,lenth=datasetTitles.size();i<lenth;i++){

                // create a dataset and give it a type
                LineDataSet set1 = new LineDataSet(yValsArr.get(i), datasetTitles.get(i));

                // set1.setFillAlpha(110);
                // set1.setFillColor(Color.RED);

                // set the line to be drawn like this "- - - - - -"
                set1.enableDashedLine(10f, 5f, 0f);
                set1.enableDashedHighlightLine(10f, 5f, 0f);
                set1.setColor(colors[i]);
                set1.setCircleColor(colors[i]);
                set1.setLineWidth(1f);
                set1.setCircleRadius(3f);
                set1.setDrawCircleHole(false);
                set1.setValueTextSize(9f);
                set1.setDrawFilled(false);

//                if (Utils.getSDKInt() >= 18) {
//                    // fill drawable only supported on api level 18 and above
//                    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
//                    set1.setFillDrawable(drawable);
//                }
//                else {
//                    set1.setFillColor(Color.BLACK);
//                }

                dataSets.add(set1); // add the datasets
            }



            // create a data object with the datasets
            LineData data = new LineData(xVals, dataSets);

            // set data
            mChart.setData(data);
            mChart.invalidate();
//        }
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<yValsArr.size();i++){
            ArrayList<Entry> entries=  yValsArr.get(i);
//            StringBuilder stringBuilder=new StringBuilder();
            int uplimit=upLimits.get(i);
            StringBuilder sbInner=new StringBuilder();
            for(int j=0;j<entries.size();j++){
                if(entries.get(j).getVal()>= uplimit){
                    sbInner.append(xVals.get(j)).append("、");
                    //报警
//                    break;
                }
            }
            if(sbInner.length()>0){
                sbInner.deleteCharAt(sbInner.length()-1);
                sbInner.append("  ");
                sb.append(sbInner.toString()).append(datasetTitles.get(i)).append("超出标准\n");
            }

        }
        if (sb.length()>0){
//           sb.deleteCharAt(sb.length()-1);
//            sb.append("超出标准\n");
        }else {
            sb.append("一切正常\n");
        }
        for (int i=0;i<yValsArr.size();i++){
            ArrayList<Entry> entries=  yValsArr.get(i);
            int mean=0,min=Integer.MAX_VALUE,max=0;
            for(int j=0;j<entries.size();j++){
                int val= (int) entries.get(j).getVal();
                mean+=val;
                if(val>max){
                   max=val;
                }
                if(val<min){
                    min=val;
                }
            }
            mean/=entries.size();
            sb.append(String.format("%s浓度 平均值%s,最大值%s,最小值%s\n",datasetTitles.get(i),mean,max,min));
        }
        tvState.setText(sb.toString());


    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOWHIGH", "low: " + mChart.getLowestVisibleXIndex() + ", high: " + mChart.getHighestVisibleXIndex());
        Log.i("MIN MAX", "xmin: " + mChart.getXChartMin() + ", xmax: " + mChart.getXChartMax() + ", ymin: " + mChart.getYChartMin() + ", ymax: " + mChart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }
}
