package hcmute.edu.vn.phamdinhquochoa.flatyapp.data;

import androidx.lifecycle.LiveData;

import java.util.List;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Notify;

public interface NotificationData {

    DataTask addNotify(Notify notify);

    LiveData<List<Notify>> getAllNotifications();

}
