package com.cheche.model;

/**
 * Created by user on 2016/2/18.
 */

import com.google.common.base.MoreObjects;

/**
 * 车系首页需要获取的数据
 */
public class Price {

    private String carName; //车名
    private String newPrice; //新车指导价
    private String oldPrice; //二手车指导价
    private String carType; //车型
    private String carSource; // 车源
    private String score; //用户评分

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getCarSource() {
        return carSource;
    }

    public void setCarSource(String carSource) {
        this.carSource = carSource;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("carName", carName)
                .add("newPrice", newPrice)
                .add("oldPrice", oldPrice)
                .add("carType", carType)
                .add("carSource", carSource)
                .add("score", score)
                .toString();
    }
}
