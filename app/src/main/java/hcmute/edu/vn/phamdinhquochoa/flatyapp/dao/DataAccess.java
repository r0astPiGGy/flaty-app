package hcmute.edu.vn.phamdinhquochoa.flatyapp.dao;

import android.util.Log;

import javax.annotation.Nullable;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.User;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.DataService;

public class DataAccess {

    private static DataService dataService;
    private static int initCounter = 0;

    public static void setDataService(DataService dataService) {
        if(initCounter > 0) {
            Log.w("@@@@", "DataService initialized more than once!");
        }

        initCounter++;
        DataAccess.dataService = dataService;
    }

    public static DataService getDataService() {
        return dataService;
    }

    @Nullable
    public static User getUser() {
        return getDataService().getAuthData().getUser();
    }
}