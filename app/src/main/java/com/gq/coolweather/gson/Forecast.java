package com.gq.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * @创建者：刚强
 * @创建时间：2018/10/20 15:43
 * @描述：数组类型的json实体类（定义出单日天气的实体类，然后在声明实体类的时候使用集合类型）
 */

public class Forecast {

    /*
    *    "daily_forecast":[
                {
                    "date":"2018-10-20",
                    "cond":{
                        "txt_d":"阴"
                    },
                    "tmp":{
                        "max":"21",
                        "min":"14"
                    }
                }
                {},
                {}
                .....
            ],
    */

    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature{

        public String max;
        public String min;
    }

    public class More{

        @SerializedName("txt_d")
        public String info;
    }

}
