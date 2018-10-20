package com.gq.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * @创建者：刚强
 * @创建时间：2018/10/20 15:15
 * @描述：GSON对应的实体类
 */

public class Basic {

    /*"basic":{
        "cid":"CN101190401",
                "location":"苏州",
                "parent_city":"苏州",
                "admin_area":"江苏",
                "cnty":"中国",
                "lat":"31.29937935",
                "lon":"120.61958313",
                "tz":"+8.00",
        "city":"苏州",---------------------城市名
        "id":"CN101190401",----------------城市对应的天气id
        "update":{-------------------------
            "loc":"2018-10-20 14:45",-------天气的更新时间
                    "utc":"2018-10-20 06:45"
        }
    },
*/

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{

        @SerializedName("loc")
        public String updateTime;
    }



}
