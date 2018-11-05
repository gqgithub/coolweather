package com.gq.coolweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gq.coolweather.gson.Forecast;
import com.gq.coolweather.gson.Weather;
import com.gq.coolweather.util.Contants;
import com.gq.coolweather.util.HttpUtil;
import com.gq.coolweather.util.JsonUtil;
import com.gq.coolweather.util.SharedPreferencesUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;

    /**标题名*/
    private TextView titleCity;

    /**标题数据更新时间*/
    private TextView titleUpdateTime;

    /**当前气温*/
    private TextView danqian_qiwen;

    /**天气概况*/
    private TextView danqian_tqgk;

    private LinearLayout weilai_tqxxLayout;

    /**aqi指数*/
    private TextView aqiText;

    /**PM指数*/
    private TextView pm25Text;

    /**舒适度*/
    private TextView comfortText;

    /**洗车指数*/
    private TextView carWashText;

    /**运动指数*/
    private TextView sportText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        initView();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        weatherLayout= (ScrollView) findViewById(R.id.weather_layout);
        titleCity= (TextView) findViewById(R.id.weather_title_city);
        titleUpdateTime= (TextView) findViewById(R.id.weather_title_update_time);
        danqian_qiwen= (TextView) findViewById(R.id.weather_danqian_qiwen);
        danqian_tqgk= (TextView) findViewById(R.id.weather_danqian_tqgk);
        weilai_tqxxLayout= (LinearLayout) findViewById(R.id.weather_weilai_tqxx);

        aqiText= (TextView) findViewById(R.id.aqi_text);
        pm25Text= (TextView) findViewById(R.id.pm25_text);
        comfortText= (TextView) findViewById(R.id.comfort_text);
        carWashText= (TextView) findViewById(R.id.car_wash_text);
        sportText= (TextView) findViewById(R.id.sport_text);


        /*String weatherString=(String)SharedPreferencesUtils.getParam(this,Contants.WEATHER_INFO,null);
        //判断是否有缓存
        if(weatherString!=null){
            //有缓存直接解析数据
            Weather weather= JsonUtil.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }else{
            //无缓存查询网络
            String weatherId=getIntent().getStringExtra(Contants.WEATHER_ID);
            //请求数据之前先将空布局隐藏，不然空数据的界面显得有些奇怪
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);

        }*/

        //无缓存查询网络
        String weatherId=getIntent().getStringExtra(Contants.WEATHER_ID);
        //请求数据之前先将空布局隐藏，不然空数据的界面显得有些奇怪
        weatherLayout.setVisibility(View.INVISIBLE);
        requestWeather(weatherId);


    }

    /**
     * 查询天气信息数据
     * @param weatherId
     */
    private void requestWeather(String weatherId) {
        String weatherUrl="http://guolin.tech/api/weather?cityid="+weatherId+"&key=0802e92ad0514d93bdb79d161d091eb3";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Weather weather=JsonUtil.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather!=null&&"ok".equals(weather.status)){
                            //保存查询的天气信息
                            SharedPreferencesUtils.setParam(WeatherActivity.this,Contants.WEATHER_INFO,responseText);
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    /**
     * 显示天气信息数据
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        String cityName=weather.basic.cityName;
        String updateTime=weather.basic.update.updateTime.split(" ")[1];
        String tem=weather.now.temperature+"摄氏度";
        String weatherInfo=weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        danqian_qiwen.setText(tem);
        danqian_tqgk.setText(weatherInfo);

        weilai_tqxxLayout.removeAllViews();
        for (Forecast forecast:weather.forecastList){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,weilai_tqxxLayout,false);
            TextView dateText= (TextView) view.findViewById(R.id.forecast_item_time);
            TextView infoText= (TextView) view.findViewById(R.id.forecast_item_tqqk);
            TextView maxText= (TextView) view.findViewById(R.id.forecast_item_max);
            TextView minText= (TextView) view.findViewById(R.id.forecast_item_min);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            weilai_tqxxLayout.addView(view);
        }

        if(weather.aqi!=null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }

        String comform="舒适度："+weather.suggestion.comfort.info;
        String carWash="洗车指数："+weather.suggestion.carWash.info;
        String sport="运动指数："+weather.suggestion.sport.info;

        comfortText.setText(comform);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);

    }

}