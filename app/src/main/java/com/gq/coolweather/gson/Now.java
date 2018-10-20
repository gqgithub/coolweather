package com.gq.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * @创建者：刚强
 * @创建时间：2018/10/20 15:32
 * @描述：
 */

public class Now {

    /*
         "now":{
                "cloud":"60",
                "cond_code":"104",
                "cond_txt":"阴",
                "fl":"19",
                "hum":"54",
                "pcpn":"0.0",
                "pres":"1025",
        "tmp":"20",--------------------温度
                "vis":"20",
                "wind_deg":"85",
                "wind_dir":"东风",
                "wind_sc":"2",
                "wind_spd":"10",
        "cond":{
                    "code":"104",
                    "txt":"阴"----------天气
                }
            },
    */
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{

        @SerializedName("txt")
        public String info;
    }

}
