package com.fintech.match.pay.common.route;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public class Target implements Parcelable {

    private String targetType;

    private String targetUrl;

    private String targetParam;

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTargetParam() {
        return targetParam;
    }

    public <T> T getParam(Class<T> clazz){
        try {
            T t = new Gson().fromJson(targetParam, clazz);
            return t;
        }catch (Exception ex){

            return null;
        }
    }

    public void setTargetParam(String targetParam) {
        this.targetParam = targetParam;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.targetType);
        dest.writeString(this.targetUrl);
        dest.writeString(this.targetParam);
    }

    public Target() {
    }

    protected Target(Parcel in) {
        this.targetType = in.readString();
        this.targetUrl = in.readString();
        this.targetParam = in.readString();
    }

    public static final Creator<Target> CREATOR = new Creator<Target>() {
        @Override
        public Target createFromParcel(Parcel source) {
            return new Target(source);
        }

        @Override
        public Target[] newArray(int size) {
            return new Target[size];
        }
    };
}
