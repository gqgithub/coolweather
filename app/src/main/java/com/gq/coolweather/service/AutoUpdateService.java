package com.gq.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gq.coolweather.gson.Weather;
import com.gq.coolweather.util.Contants;
import com.gq.coolweather.util.HttpUtil;
import com.gq.coolweather.util.JsonUtil;
import com.gq.coolweather.util.SPUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @创建者：刚强
 * @创建时间：2018/11/5 16:06
 * @描述：后台自动更新----定时更新功能
 */

public class AutoUpdateService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour=8*60*60*1000;//8个小时毫秒数
        long triggerAtTime= SystemClock.elapsedRealtime()+anHour;
        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        Log.e("GANG","已开启后台服务");

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新图片信息
     */
    private void updateBingPic() {

        HttpUtil.sendOkHttpRequest(Contants.BINGPICIMG_PATH, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic=response.body().string();
                SPUtils.setParam(AutoUpdateService.this,Contants.BINGPICIMG,bingPic);
            }
        });

    }

    /**
     * 更新天气信息
     */
    private void updateWeather() {
        String weatherJson= (String) SPUtils.getParam(this, Contants.WEATHER_INFO,null);
        if(weatherJson!=null){
            //有缓存直接解析
            Weather weather= JsonUtil.handleWeatherResponse(weatherJson);
            String weatherId=weather.basic.weatherId;
            String weatherUrl="http://guolin.tech/api/weather?cityid="+weatherId+"&key=0802e92ad0514d93bdb79d161d091eb3";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText=response.body().string();
                    Weather weather=JsonUtil.handleWeatherResponse(responseText);
                    if (weather!=null&&"ok".equals(weather.status)){
                        SPUtils.setParam(AutoUpdateService.this,Contants.WEATHER_INFO,responseText);
                    }
                }
            });
        }

    }
}
