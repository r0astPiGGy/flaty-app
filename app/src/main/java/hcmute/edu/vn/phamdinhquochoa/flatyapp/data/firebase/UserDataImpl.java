package hcmute.edu.vn.phamdinhquochoa.flatyapp.data.firebase;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.K;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.User;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.DataTask;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.UserData;

public class UserDataImpl extends FirebaseDataContext implements UserData {

    public UserDataImpl(Supplier<FirebaseFirestore> firebaseFirestore) {
        super(firebaseFirestore);
    }

    @Override
    public DataTask addUser(User user) {
        return updateUser(user);
    }

    @Override
    public CompletableFuture<User> getUserById(String id) {
        CompletableFuture<User> userCompletableFuture = new CompletableFuture<>();

        db().collection(K.Collections.USERS).document(id)
                .get()
                .addOnSuccessListener(s -> {
                    User user = s.toObject(User.class);
                    userCompletableFuture.complete(user);
                })
                .addOnFailureListener(userCompletableFuture::completeExceptionally);

        return userCompletableFuture;
    }

    @Override
    public DataTask updateUser(User user) {
        DataTask.Invokable dataTask = createTask();

        db().collection(K.Collections.USERS)
                .document(user.getId())
                .set(user)
                .addOnCompleteListener(t -> handleTaskCompletion(t, dataTask));

        return dataTask;
    }
}
