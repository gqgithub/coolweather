package com.gq.coolweather.db;

import org.litepal.crud.LitePalSupport;

/**
 * @创建者：刚强
 * @创建时间：2018/10/13 14:44
 * @描述：JavaBean----省份
 */

public class Province extends LitePalSupport{
/*
    [
    {
        "id":1,
            "name":"北京"
    },
    {
        "id":2,
            "name":"上海"
    },
    {
        "id":3,
            "name":"天津"
    }
    ]
*/
    /**省份数据表的编号id*/
    private int id;
    /**省份的名称*/
    private String provinceName;
    /**省份的编号id*/
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
