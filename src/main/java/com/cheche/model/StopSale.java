package com.cheche.model;

import com.google.common.base.MoreObjects;

/**
 * Created by user on 2016/2/18.
 */
public class StopSale {

    private String carName; //车名
    private String year; //eg .2012款
    private String advicePrice; //指导价
    private String usedPrice;  //二手车价钱
    private String link;  //  参数配置链接

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAdvicePrice() {
        return advicePrice;
    }

    public void setAdvicePrice(String advicePrice) {
        this.advicePrice = advicePrice;
    }

    public String getUsedPrice() {
        return usedPrice;
    }

    public void setUsedPrice(String usedPrice) {
        this.usedPrice = usedPrice;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("carName", carName)
                .add("year", year)
                .add("advicePrice", advicePrice)
                .add("usedPrice", usedPrice)
                .add("link", link)
                .toString();
    }
}
