package com.example.web3.myapplication.model_class;

import java.io.Serializable;

/**
 * Created by web3 on 5/5/2016.
 */
public class Tracking_Details_model implements Serializable{
    private String caf_number,del_number,cust_name, date, status, rating, remark;

    public Tracking_Details_model(){}
    public Tracking_Details_model(String caf_number, String del_number, String cust_name, String date, String status, String rating, String remark) {
        this.caf_number = caf_number;
        this.del_number = del_number;
        this.cust_name = cust_name;
        this.date = date;
        this.status = status;
        this.rating = rating;
        this.remark = remark;
    }
    public Tracking_Details_model(String caf_number, String cust_name, String date, String status, String rating, String remark) {
        this.caf_number = caf_number;

        this.cust_name = cust_name;
        this.date = date;
        this.status = status;
        this.rating = rating;
        this.remark = remark;
    }

    public String getDel_number() {
        return del_number;
    }

    public void setDel_number(String del_number) {
        this.del_number = del_number;
    }

    public String getCaf_number() {
        return caf_number;
    }

    public void setCaf_number(String caf_number) {
        this.caf_number = caf_number;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
