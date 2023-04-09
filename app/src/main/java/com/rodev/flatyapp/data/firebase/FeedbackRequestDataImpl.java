package com.rodev.flatyapp.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.rodev.flatyapp.K;
import com.rodev.flatyapp.beans.FeedbackRequest;
import com.rodev.flatyapp.data.DataTask;
import com.rodev.flatyapp.data.FeedbackRequestData;

public class FeedbackRequestDataImpl extends FirebaseDataContext implements FeedbackRequestData {

    public FeedbackRequestDataImpl(Supplier<FirebaseFirestore> firebaseFirestore) {
        super(firebaseFirestore);
    }

    @Override
    public DataTask addRequest(FeedbackRequest request) {
        DataTask.Invokable task = DataTask.createDataTask();

        db().collection(K.Collections.REQUESTS)
                .add(request)
                .addOnCompleteListener(t -> handleTaskCompletion(t, task));

        return task;
    }

    @Override
    public LiveData<List<FeedbackRequest>> getAllRequests() {
        MutableLiveData<List<FeedbackRequest>> liveData = new MutableLiveData<>();

        db().collection(K.Collections.REQUESTS)
                .get()
                .addOnSuccessListener(q -> {
                    List<FeedbackRequest> requests = q.getDocuments().stream()
                            .map(d -> d.toObject(FeedbackRequest.class))
                            .collect(Collectors.toList());

                    liveData.postValue(requests);
                })
                .addOnFailureListener(Throwable::printStackTrace);

        return liveData;
    }
}
