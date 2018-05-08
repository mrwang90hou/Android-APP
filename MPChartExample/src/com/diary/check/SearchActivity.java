package com.diary.check;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xxmassdeveloper.mpchartexample.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/30 0030.
 */
public class SearchActivity extends BaseActivity {


    TextView tvSensor,tvDate;
    ListView lvDate;
//    ListView lvSensor;
    LinearLayout llDropDown;
//    SensorAdapter sensorAdapter;
    DateAdapter dateAdapter;
//    TextView tvSearch;
    CheckBox cbCH4,cbNH3,cbH2S;
    CheckBox cbSensor1,cbSensor2,cbSensor3,cbSensor4,cbSensor5;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static final String SENSOR_LIST_STRING="sensorListString";
    public static final String SENSOR_INDEX_LIST_INTEGER="sensorIndexListInteger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zrh_search);
        cbCH4= (CheckBox) findViewById(R.id.cbCH4);
        cbNH3= (CheckBox) findViewById(R.id.cbNH3);
        cbH2S= (CheckBox) findViewById(R.id.cbH2S);

        cbSensor1= (CheckBox) findViewById(R.id.cbSensor1);
        cbSensor2= (CheckBox) findViewById(R.id.cbSensor2);
        cbSensor3= (CheckBox) findViewById(R.id.cbSensor3);
        cbSensor4= (CheckBox) findViewById(R.id.cbSensor4);
        cbSensor5= (CheckBox) findViewById(R.id.cbSensor5);

        tvSensor=(TextView)findViewById(R.id.tvSensor);
        tvDate=(TextView)findViewById(R.id.tvDate);
//        lvSensor=(ListView)findViewById(R.id.lvSensor);
        lvDate=(ListView)findViewById(R.id.lvDate);
//        lvSensor.setVisibility(View.INVISIBLE);
        lvDate.setVisibility(View.INVISIBLE);
        llDropDown=(LinearLayout)findViewById(R.id.llDropDown);
        llDropDown.setVisibility(View.GONE);
//        sensorAdapter= new SensorAdapter();
//        lvSensor.setAdapter(sensorAdapter);
        dateAdapter =new DateAdapter();
        lvDate.setAdapter(dateAdapter);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lvDate.getVisibility()!=View.VISIBLE){
                    lvDate.setVisibility(View.VISIBLE);
//                    lvSensor.setVisibility(View.INVISIBLE);
                    llDropDown.setVisibility(View.VISIBLE);
                }
            }
        });
        lvDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //ToDo 搜索该传感器的数据
                tvDate.setText(dateAdapter.getItem(position));
                lvDate.setVisibility(View.INVISIBLE);
                llDropDown.setVisibility(View.GONE);
            }
        });
        tvDate.setText(dateAdapter.getItem(0));
//        tvSearch=(TextView)findViewById(R.id.tvSearch);

        mTemplateRightText.setVisibility(View.VISIBLE);
        mTemplateRightText.setText("搜索");
        mTemplateRightImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mTemplateRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToDo 条件查询，模拟数据代替
                Intent intent=new Intent(SearchActivity.this,BarChartActivityMultiDataset.class);
                intent.putExtra(LineChartActivity2.IS_SHOW_CH4,cbCH4.isChecked());
                intent.putExtra(LineChartActivity2.IS_SHOW_NH3,cbNH3.isChecked());
                intent.putExtra(LineChartActivity2.IS_SHOW_H2S,cbH2S.isChecked());
                ArrayList<String>sensorList=new ArrayList<String>();
                ArrayList<Integer>sensorIndexList=new ArrayList<Integer>();
                if(cbSensor1.isChecked()){
                    sensorList.add(cbSensor1.getText().toString());
                    sensorIndexList.add(1);
                }
                if(cbSensor2.isChecked()){
                    sensorList.add(cbSensor2.getText().toString());
                    sensorIndexList.add(2);
                }
                if(cbSensor3.isChecked()){
                    sensorList.add(cbSensor3.getText().toString());
                    sensorIndexList.add(3);
                }
                if(cbSensor4.isChecked()){
                    sensorList.add(cbSensor4.getText().toString());
                    sensorIndexList.add(4);
                }
                if(cbSensor5.isChecked()){
                    sensorList.add(cbSensor5.getText().toString());
                    sensorIndexList.add(5);
                }
                if(sensorList.size()==0){
                    Toast.makeText(SearchActivity.this,"请选择传感器",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(cbCH4.isChecked()||cbNH3.isChecked()||cbH2S.isChecked()){

                }else {
                    Toast.makeText(SearchActivity.this,"请选择气体",Toast.LENGTH_SHORT).show();
                }
                if(sensorList.size()<2){
                    intent.setClass(SearchActivity.this,LineChartActivity2.class);
                }
                try {
                    intent.putExtra(SENSOR_LIST_STRING,sensorList);
                    intent.putExtra(SENSOR_INDEX_LIST_INTEGER,sensorIndexList);
                    intent.putExtra(LineChartActivity2.DATE_LONG,sdf.parse(tvDate.getText().toString()).getTime());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                startActivity(intent);
            }
        });

    }

    class DateAdapter extends BaseAdapter{
        public DateAdapter() {

        }

        @Override
        public int getCount() {
            String str2=SharedPreferenceUtil.get(SearchActivity.this,SettingActivity.DAYS_KEEPED,String.valueOf(SettingActivity.DAYS_KEEPED_DEFAULT));
            try {
                return Integer.valueOf(str2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 7;
        }

        @Override
        public String  getItem(int position) {
            Date date=new Date(System.currentTimeMillis()-position*24*60*60*1000);
            return sdf.format(date);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView= LayoutInflater.from(SearchActivity.this).inflate(R.layout.zrh_item_search,null);
            }
            TextView textView= (TextView) convertView.findViewById(R.id.tvName);
            textView.setText(getItem(position));
            return convertView;
        }
    }
    class SensorAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public String  getItem(int position) {
            return String.format("%s号",position+1);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView= LayoutInflater.from(SearchActivity.this).inflate(R.layout.zrh_item_search,null);
            }
            TextView textView= (TextView) convertView.findViewById(R.id.tvName);
            textView.setText(getItem(position));
            return convertView;
        }
    }
}
