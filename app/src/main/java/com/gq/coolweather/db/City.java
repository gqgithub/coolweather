package com.gq.coolweather.db;

import org.litepal.crud.LitePalSupport;

/**
 * @创建者：刚强
 * @创建时间：2018/10/13 14:44
 * @描述：JavaBean----市
 */

public class City extends LitePalSupport{

    private int id;
    private String cityName;
    private int cityCode;
    private int provinceId;

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }
}
