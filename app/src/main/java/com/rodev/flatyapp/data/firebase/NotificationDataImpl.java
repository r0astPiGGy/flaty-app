package com.rodev.flatyapp.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.rodev.flatyapp.K;
import com.rodev.flatyapp.beans.Notify;
import com.rodev.flatyapp.data.DataTask;
import com.rodev.flatyapp.data.NotificationData;

public class NotificationDataImpl extends FirebaseDataContext implements NotificationData {

    public NotificationDataImpl(Supplier<FirebaseFirestore> firebaseFirestore) {
        super(firebaseFirestore);
    }

    @Override
    public DataTask addNotify(Notify notify) {
        DataTask.Invokable dataTask = createTask();

        db().collection(K.Collections.NOTIFICATIONS)
                .document(notify.getId())
                .set(notify)
                .addOnCompleteListener(d -> handleTaskCompletion(d, dataTask));

        return dataTask;
    }

    @Override
    public LiveData<List<Notify>> getAllNotifications() {
        MutableLiveData<List<Notify>> data = new MutableLiveData<>();

        db().collection(K.Collections.NOTIFICATIONS)
                .whereEqualTo("userId", getUserId())
                .get()
                .addOnSuccessListener(snap -> {
                    List<Notify> notifies = snap.getDocuments().stream()
                            .map(s -> s.toObject(Notify.class))
                            .collect(Collectors.toList());

                    data.postValue(notifies);
                }).addOnFailureListener(Throwable::printStackTrace);

        return data;
    }
}
