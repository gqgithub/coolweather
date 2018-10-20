package com.gq.coolweather.db;

import org.litepal.crud.LitePalSupport;

/**
 * @创建者：刚强
 * @创建时间：2018/10/13 14:44
 * @描述：JavaBean----市
 */

public class City extends LitePalSupport{

    /**市级数据表的编号id*/
    private int id;
    /**市级的名称*/
    private String cityName;
    /**市级的编号id*/
    private int cityCode;
    /**市级所属省级的编号id*/
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
