package com.gq.coolweather.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.gq.coolweather.db.City;
import com.gq.coolweather.db.County;
import com.gq.coolweather.db.Province;
import com.gq.coolweather.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @创建者：刚强
 * @创建时间：2018/10/17 13:56
 * @描述：JSONObject解析简单的json数据
 */

public class JsonUtil {

    /**
     * 解析并处理省级数据
     * @param response 服务器json数据
     * @return
     * 简单的json数据[{}，{}，{}]
     */
    public static boolean handleProvinceReponse(String response){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvinces=new JSONArray(response);
                for(int i=0;i<allProvinces.length();i++){
                    JSONObject provinceObject=allProvinces.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.setProvinceName(provinceObject.getString("name"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析并处理市级数据
     * @param response 服务器json数据
     * @param provinceId 属于哪个省
     * @return
     * 简单的json数据[{}，{}，{}]
     */
    public static boolean handleCityReponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCitys=new JSONArray(response);
                for(int i=0;i<allCitys.length();i++){
                    JSONObject cityObject=allCitys.getJSONObject(i);
                    City city=new City();
                    city.setProvinceId(provinceId);
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析并处理县级数据
     * @param response 服务器json数据
     * @param cityId 属于哪个市
     * @return
     * 简单的json数据[{}，{}，{}]
     */
    public static boolean handleCountyReponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCountys=new JSONArray(response);
                for(int i=0;i<allCountys.length();i++){
                    JSONObject countyObject=allCountys.getJSONObject(i);
                    County county=new County();
                    county.setCityId(cityId);
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response){

        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
            //获取JSON数组中的第一项
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
