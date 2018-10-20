package com.gq.coolweather.gson;

/**
 * @创建者：刚强
 * @创建时间：2018/10/20 15:28
 * @描述：
 */

public class AQI {

    /*
     "aqi":{
                "city":{
                    "aqi":"31",
                    "pm25":"14",------空气指数
                    "qlty":"优"
                }
            },
    */

    public AQICity city;

    public class AQICity{

        public String api;
        public String pm25;
    }
}
