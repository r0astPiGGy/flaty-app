package hcmute.edu.vn.phamdinhquochoa.flatyapp.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.K;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FavoriteFlat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Flat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.DataTask;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.FavoriteFlatData;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.FlatData;

public class FavoriteFlatDataImpl extends FirebaseDataContext implements FavoriteFlatData {

    public FavoriteFlatDataImpl(Supplier<FirebaseFirestore> firebaseFirestore) {
        super(firebaseFirestore);
    }

    @Override
    public DataTask addFavorite(String id) {
        DataTask.Invokable task = createTask();

        db().collection(K.Collections.FAVORITE_FLATS)
                .add(new FavoriteFlat(id, getUserId()))
                .addOnCompleteListener(t -> handleTaskCompletion(t, task));

        return task;
    }

    @Override
    public DataTask removeFavorite(String id) {
        DataTask.Invokable task = createTask();

        db().collection(K.Collections.FAVORITE_FLATS)
                .whereEqualTo("flatId", id)
                .whereEqualTo("userId", getUserId())
                .get().addOnSuccessListener(t -> {
                    for (DocumentSnapshot d : t.getDocuments()) {
                        d.getReference()
                                .delete()
                                .addOnCompleteListener(t1 -> handleTaskCompletion(t1, task));
                    }
                }).addOnFailureListener(task::invokeOnFailure);

        return task;
    }

    @Override
    public CompletableFuture<Void> removeFavoritesByFlatId(List<String> ids) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        if(ids.isEmpty()) {
            completableFuture.complete(null);
            return completableFuture;
        }

        db().collection(K.Collections.FAVORITE_FLATS)
                .whereIn("flatId", ids)
                .get()
                .addOnSuccessListener(q -> {
                    List<DocumentReference> list = q.getDocuments()
                            .stream()
                            .map(DocumentSnapshot::getReference)
                            .collect(Collectors.toList());

                    db().runBatch(batch -> {
                        list.forEach(batch::delete);
                    }).addOnSuccessListener(completableFuture::complete)
                            .addOnFailureListener(completableFuture::completeExceptionally);
                })
                .addOnFailureListener(completableFuture::completeExceptionally);

        return completableFuture;
    }

    @Override
    public LiveData<List<FavoriteFlat>> getFavoriteFlats() {
        MutableLiveData<List<FavoriteFlat>> mutableLiveData = new MutableLiveData<>();

        db().collection(K.Collections.FAVORITE_FLATS)
                .whereEqualTo("userId", getUserId())
                .get()
                .addOnSuccessListener(t -> {
                    List<FavoriteFlat> flats = t.getDocuments().stream()
                            .map(d -> d.toObject(FavoriteFlat.class))
                            .collect(Collectors.toList());

                    mutableLiveData.postValue(flats);
                }).addOnFailureListener(Throwable::printStackTrace);

        return mutableLiveData;
    }
}
