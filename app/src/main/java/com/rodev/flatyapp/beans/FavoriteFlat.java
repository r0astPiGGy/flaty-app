package com.rodev.flatyapp.beans;

public class FavoriteFlat {
    private String flatId;
    private String userId;

    public FavoriteFlat() {}

    public FavoriteFlat(String FlatId, String userId){
        this.flatId = FlatId;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getFlatId() {
        return flatId;
    }
}
