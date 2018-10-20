package com.gq.coolweather.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @创建者：刚强
 * @创建时间：2018/10/17 13:49
 * @描述：从服务器端获取数据
 */

public class HttpUtil {

    /**
     * 发送请求服务器数据
     * @param address 请求地址
     * @param callback 回掉处理请求响应
     */
    public static void sendOkHttpRequest(String address, Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
