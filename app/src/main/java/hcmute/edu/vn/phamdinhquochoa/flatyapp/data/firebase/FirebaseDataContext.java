package hcmute.edu.vn.phamdinhquochoa.flatyapp.data.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.function.Supplier;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.DataTask;

public class FirebaseDataContext {

    private final Supplier<FirebaseFirestore> firebaseFirestore;

    public FirebaseDataContext(Supplier<FirebaseFirestore> firebaseFirestore) {
        this.firebaseFirestore = firebaseFirestore;
    }

    public FirebaseFirestore db() {
        return firebaseFirestore.get();
    }

    public String getUserId() {
        return FirebaseUser.getUniqueId();
    }

    public DataTask.Invokable createTask() {
        return DataTask.createDataTask();
    }

    protected void handleTaskCompletion(Task<?> task, DataTask.Invokable dataTask) {
        if(task.isSuccessful()) {
            dataTask.invokeOnComplete();
        } else {
            task.getException().printStackTrace();
            dataTask.invokeOnFailure(task.getException());
        }
    }

}
