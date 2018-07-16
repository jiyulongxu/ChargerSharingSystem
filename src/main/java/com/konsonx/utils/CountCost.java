package com.konsonx.utils;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component("CountCost")
public class CountCost {
    public static long period = 1000*60*30l; //计费单位 半小时
    public static float price = 0.6f;//价格 0.6元半小时
    public float count(Date start,Date end) {
        Long duration = end.getTime() - start.getTime();
        long consumed = duration / period + 1;
        return consumed * price;
    }
}
