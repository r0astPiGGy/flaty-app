package hcmute.edu.vn.phamdinhquochoa.flatyapp.data.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.function.Supplier;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.User;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.AuthData;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.DataTask;

public class AuthDataImpl extends FirebaseDataContext implements AuthData {

    private User user;

    public AuthDataImpl(Supplier<FirebaseFirestore> firebaseFirestore) {
        super(firebaseFirestore);
    }

    @Override
    public DataTask signIn(String email, String password) {
        DataTask.Invokable task = DataTask.createDataTask();
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(authResult -> handleTaskCompletion(authResult, task));

        return task;
    }

    @Override
    public DataTask changePassword(String password) {
        DataTask.Invokable task = DataTask.createDataTask();
        FirebaseAuth.getInstance()
                .getCurrentUser()
                .updatePassword(password)
                .addOnCompleteListener(updateTask -> handleTaskCompletion(updateTask, task));

        return task;
    }

    @Override
    public DataTask register(String email, String password) {
        DataTask.Invokable task = DataTask.createDataTask();

        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(creationTask -> handleTaskCompletion(creationTask, task));

        return task;
    }

    @Override
    public DataTask logout() {
        DataTask.Invokable task = DataTask.createDataTask();

        FirebaseAuth.getInstance().signOut();
        task.invokeOnComplete();

        user = null;

        return task;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }
}
