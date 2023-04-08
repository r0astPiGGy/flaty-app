package hcmute.edu.vn.phamdinhquochoa.flatyapp.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.K;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FeedbackRequest;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.DataTask;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.FeedbackRequestData;

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
