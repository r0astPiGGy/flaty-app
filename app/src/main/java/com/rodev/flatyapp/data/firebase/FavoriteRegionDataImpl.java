package com.rodev.flatyapp.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.rodev.flatyapp.K;
import com.rodev.flatyapp.beans.FavoriteRegion;
import com.rodev.flatyapp.data.DataTask;
import com.rodev.flatyapp.data.FavoriteRegionData;

public class FavoriteRegionDataImpl extends FirebaseDataContext implements FavoriteRegionData {

    public FavoriteRegionDataImpl(Supplier<FirebaseFirestore> firebaseFirestore) {
        super(firebaseFirestore);
    }

    @Override
    public DataTask addFavorite(String regionId) {
        DataTask.Invokable task = DataTask.createDataTask();
        String userId = getUserId();
        FavoriteRegion favoriteRegion = new FavoriteRegion(regionId, userId);

        db().collection(K.Collections.FAVORITE_REGIONS)
                .add(favoriteRegion)
                .addOnCompleteListener(d -> handleTaskCompletion(d, task));

        return task;
    }

    @Override
    public DataTask removeFavorite(String regionId) {
        DataTask.Invokable task = createTask();

        db().collection(K.Collections.FAVORITE_REGIONS)
                .whereEqualTo("regionId", regionId)
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
    public CompletableFuture<Void> removeFavoritesByRegionId(String id) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        db().collection(K.Collections.FAVORITE_REGIONS)
                .whereEqualTo("regionId", id)
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
    public LiveData<List<FavoriteRegion>> getFavoriteRegions() {
        MutableLiveData<List<FavoriteRegion>> regions = new MutableLiveData<>();
        db().collection(K.Collections.FAVORITE_REGIONS)
                .whereEqualTo("userId", getUserId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<FavoriteRegion> region = queryDocumentSnapshots
                            .getDocuments()
                            .stream()
                            .map(d -> d.toObject(FavoriteRegion.class))
                            .collect(Collectors.toList());

                    regions.postValue(region);
                });

        return regions;
    }
}
