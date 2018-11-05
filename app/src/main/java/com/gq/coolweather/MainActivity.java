package com.gq.coolweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gq.coolweather.util.Contants;
import com.gq.coolweather.util.SPUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String weather= (String) SPUtils.getParam(this, Contants.WEATHER_INFO,null);
        if (weather!=null){
            Intent intent=new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this,"缓存失败",Toast.LENGTH_SHORT).show();

        }

    }
}
