package com.rodev.flatyapp.data;

import androidx.lifecycle.LiveData;

import java.util.List;

import com.rodev.flatyapp.beans.Notify;

public interface NotificationData {

    DataTask addNotify(Notify notify);

    LiveData<List<Notify>> getAllNotifications();

}
