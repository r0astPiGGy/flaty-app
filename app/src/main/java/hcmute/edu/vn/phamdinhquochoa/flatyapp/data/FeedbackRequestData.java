package hcmute.edu.vn.phamdinhquochoa.flatyapp.data;

import androidx.lifecycle.LiveData;

import java.util.List;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FeedbackRequest;

public interface FeedbackRequestData {

    DataTask addRequest(FeedbackRequest request);

    LiveData<List<FeedbackRequest>> getAllRequests();

}
