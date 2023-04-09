package com.rodev.flatyapp.data;

import androidx.lifecycle.LiveData;

import java.util.List;

import com.rodev.flatyapp.beans.FeedbackRequest;

public interface FeedbackRequestData {

    DataTask addRequest(FeedbackRequest request);

    LiveData<List<FeedbackRequest>> getAllRequests();

}
