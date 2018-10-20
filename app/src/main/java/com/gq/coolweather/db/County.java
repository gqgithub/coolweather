package com.gq.coolweather.db;

import org.litepal.crud.LitePalSupport;

/**
 * @创建者：刚强
 * @创建时间：2018/10/13 14:44
 * @描述：JavaBean----县
 */

public class County extends LitePalSupport{

    /**县级数据表的编号id*/
    private int id;
    /**县级的名称*/
    private String countyName;
    /**县级的天气*/
    private String weatherId;
    /**县级所属城市id*/
    private int cityId;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
