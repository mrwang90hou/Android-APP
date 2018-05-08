package com.diary.check;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Property;
import net.tsz.afinal.annotation.sqlite.Transient;

import java.io.Serializable;

/**
 * Created by huzhou on 6/9/16.
 */
public class DataVo implements Serializable{

    @Transient
    public static final int CH4=1;
    @Transient
    public static final int NH3=2;
    @Transient
    public static final int H2S=3;

    private long time;//时间戳
    @Property(column ="dataIndex")
    private int index;
    private int sensorIndex;
    private int gasIndex;
    private int val;

    public int getGasIndex() {
        return gasIndex;
    }

    public DataVo() {
    }

    public DataVo(long time, int index, int sensorIndex, int gasIndex, int val) {
        this.time = time;
        this.index = index;
        this.sensorIndex = sensorIndex;
        this.gasIndex = gasIndex;
        this.val = val;
        this.generateId();
    }

    public void setGasIndex(int gasIndex) {
        this.gasIndex = gasIndex;
    }

    @Id
    private String id;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSensorIndex() {
        return sensorIndex;
    }

    public void setSensorIndex(int sensorIndex) {
        this.sensorIndex = sensorIndex;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void generateId(){
       id= generateId(this.time,this.sensorIndex,this.gasIndex,this.index);

    }
    public static String  generateId(long time,int sensorIndex,int gasIndex,int index) {
        return String.format("%s_%s_%s_%s",time,sensorIndex,gasIndex,index);

    }





}
