package com.gq.coolweather;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gq.coolweather.gson.Forecast;
import com.gq.coolweather.gson.Weather;
import com.gq.coolweather.service.AutoUpdateService;
import com.gq.coolweather.util.Contants;
import com.gq.coolweather.util.HttpUtil;
import com.gq.coolweather.util.JsonUtil;
import com.gq.coolweather.util.SPUtils;

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

    /**背景圖片*/
    private ImageView bingPicImg;

    /**下拉刷新*/
    public SwipeRefreshLayout swipeRefresh;
    private String mWeatherId;

    /**侧滑菜单*/
    public DrawerLayout drawerLayout;
    private Button navButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //动态设置通知栏颜色
        if(Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
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


        String weatherString=(String)SPUtils.getParam(this,Contants.WEATHER_INFO,null);

        //判断是否有缓存
        if(weatherString!=null){
            //有缓存直接解析数据
            Weather weather= JsonUtil.handleWeatherResponse(weatherString);
            mWeatherId=weather.basic.weatherId;
            showWeatherInfo(weather);
        }else{
            //无缓存查询网络
            mWeatherId=getIntent().getStringExtra(Contants.WEATHER_ID);
            //请求数据之前先将空布局隐藏，不然空数据的界面显得有些奇怪
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);


        }

        //添加背景图片
        bingPicImg= (ImageView) findViewById(R.id.bing_pic_img);
        String bingPic=(String) SPUtils.getParam(this,Contants.BINGPICIMG,null);
        if(bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else{
            loadBingPic();
        }

        //添加下拉刷新
        swipeRefresh= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

        //添加侧滑菜单功能
        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton= (Button) findViewById(R.id.nav_button);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


    }

    /**
     * 加载背景图片
     */
    private void loadBingPic() {
        HttpUtil.sendOkHttpRequest(Contants.BINGPICIMG_PATH, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                SPUtils.setParam(WeatherActivity.this,Contants.BINGPICIMG,bingPic);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });

    }

    /**
     * 查询天气信息数据
     * @param weatherId
     */
    public void requestWeather(String weatherId) {
        String weatherUrl="http://guolin.tech/api/weather?cityid="+weatherId+"&key=0802e92ad0514d93bdb79d161d091eb3";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
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
                            SPUtils.setParam(WeatherActivity.this,Contants.WEATHER_INFO,responseText);
                            Log.e("GANG","CESHI"+(String) SPUtils.getParam(WeatherActivity.this,Contants.WEATHER_INFO,null));
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                        //停止刷新
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });

        //刷新图片
        loadBingPic();

    }

    /**
     * 显示天气信息数据
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        if (weather!=null&&"ok".equals(weather.status)){
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

            Intent intent=new Intent(this, AutoUpdateService.class);
            startService(intent);
        }else{
            Toast.makeText(this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
        }


    }

}
