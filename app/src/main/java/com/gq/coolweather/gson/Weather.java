package com.gq.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @创建者：刚强
 * @创建时间：2018/10/20 15:54
 * @描述：总的json实体类
 */

public class Weather {

    /*
    *   {
    "HeWeather":[
        {
            "basic":{}，
            "status":"ok",
            "now":{},------------------单个实体类型解析
            "daily_forecast":[],-----数组类型实体类解析
            "aqi":{},
            "suggestion":{}
        }
    ]
}
    */

    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecast;


}
