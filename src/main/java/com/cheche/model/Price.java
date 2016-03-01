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
    private String bigImg;  //大图
    private String factImg;  //实拍图片
    private String videoImg; //视频图片
    private String engine; //发动机
    private String specData;  //具体的数据
    private String status = "在售";  //销售状态
    private String SId;   //id

    public String getSId() {
        return SId;
    }

    public void setSId(String SId) {
        this.SId = SId;
    }

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

    public String getBigImg() {
        return bigImg;
    }

    public void setBigImg(String bigImg) {
        this.bigImg = bigImg;
    }

    public String getVideoImg() {
        return videoImg;
    }

    public void setVideoImg(String videoImg) {
        this.videoImg = videoImg;
    }

    public String getFactImg() {
        return factImg;
    }

    public void setFactImg(String factImg) {
        this.factImg = factImg;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getSpecData() {
        return specData;
    }

    public void setSpecData(String specData) {
        this.specData = specData;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("\"").append(carName).append("\"").append(",");
        sb.append("\"").append(newPrice).append("\"").append(",");
        sb.append("\"").append(oldPrice).append("\"").append(",");
        sb.append("\"").append(carType).append("\"").append(",");
        sb.append("\"").append(carSource).append("\"").append(",");
        sb.append("\"").append(score).append("\"").append(",");
        sb.append("\"").append(bigImg).append("\"").append(",");
        sb.append("\"").append(factImg).append("\"").append(",");
        sb.append("\"").append(videoImg).append("\"").append(",");
        sb.append("\"").append(engine).append("\"").append(",");
        sb.append("\"").append(specData).append("\"").append(",");
        sb.append("\"").append(SId).append("\"").append(",");
        sb.append("\"").append(status).append("\"");
        return sb.toString();
    }
}
