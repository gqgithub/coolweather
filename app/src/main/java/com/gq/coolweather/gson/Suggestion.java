package com.gq.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * @创建者：刚强
 * @创建时间：2018/10/20 15:37
 * @描述：
 */

public class Suggestion {

    /*
    *   "suggestion":{
                "comf":{
                    "type":"comf",
                    "brf":"舒适",
     "txt":"白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。"
                },
                "sport":{
                    "type":"sport",
                    "brf":"较适宜",
     "txt":"阴天，较适宜进行各种户内外运动。"
                },
                "cw":{
                    "type":"cw",
                    "brf":"较适宜",
     "txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"
                }
            }
    */

    /**舒适度*/
    @SerializedName("comf")
    public Comfort comfort;

    /**汽车指数*/
    @SerializedName("cw")
    public CarWash carWash;

    /**运动建议*/
    public Sport sport;

    public class Comfort{
        @SerializedName("txt")
        public String info;
    }

    public class CarWash{
        @SerializedName("txt")
        public String info;
    }

    public class Sport{
        @SerializedName("txt")
        public String info;
    }

}
