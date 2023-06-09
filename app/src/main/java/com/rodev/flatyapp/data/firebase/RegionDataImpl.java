package com.rodev.flatyapp.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.rodev.flatyapp.K;
import com.rodev.flatyapp.beans.Region;
import com.rodev.flatyapp.dao.DataAccess;
import com.rodev.flatyapp.data.DataTask;
import com.rodev.flatyapp.data.RegionData;

public class RegionDataImpl extends FirebaseDataContext implements RegionData {

    public RegionDataImpl(Supplier<FirebaseFirestore> firebaseFirestore) {
        super(firebaseFirestore);
    }

    @Override
    public DataTask addRegion(Region region) {
        return updateRegion(region);
    }

    @Override
    public DataTask updateRegion(Region region) {
        DataTask.Invokable task = createTask();

        db().collection(K.Collections.REGIONS)
                .document(region.getId())
                .set(region)
                .addOnSuccessListener(t -> updateRegionImage(task, region))
                .addOnFailureListener(task::invokeOnFailure);

        return task;
    }

    @Override
    public DataTask deleteRegion(Region region) {
        DataTask.Invokable task = createTask();

        db().collection(K.Collections.REGIONS)
                .document(region.getId())
                .delete()
                .addOnSuccessListener(t -> {
                    deleteFavoriteRegions(region);
                    deleteRegionImage(task, region);
                })
                .addOnFailureListener(task::invokeOnFailure);

        return task;
    }

    private void updateRegionImage(DataTask.Invokable task, Region region) {
        DataAccess.getDataService()
                .getImageStorage()
                .updateImageByUri(region.getImage(), region.getId())
                .whenComplete((Null, exception) -> {
                    if (exception == null) {
                        task.invokeOnComplete();
                    } else {
                        task.invokeOnFailure(exception);
                    }
                });
    }

    private void deleteRegionImage(DataTask.Invokable task, Region region) {
        DataAccess.getDataService()
                .getImageStorage()
                .deleteImageByUri(region.getId())
                .whenComplete((Null, exception) -> {
                    if (exception == null) {
                        task.invokeOnComplete();
                    } else {
                        task.invokeOnFailure(exception);
                    }
                });
    }

    private void deleteFavoriteRegions(Region region) {
        DataAccess.getDataService()
                .getFavoriteRegionData()
                .removeFavoritesByRegionId(region.getId());
    }

    @Override
    public LiveData<Region> getRegionById(String id) {
        MutableLiveData<Region> regionLiveData = new MutableLiveData<>();

        db().collection(K.Collections.REGIONS)
                .document(id)
                .get()
                .addOnSuccessListener(d -> {
                    regionLiveData.postValue(d.toObject(Region.class));
                })
                .addOnFailureListener(Throwable::printStackTrace);

        return regionLiveData;
    }

    @Override
    public LiveData<List<Region>> getRegionsByIds(List<String> ids) {
        MutableLiveData<List<Region>> mutableLiveData = new MutableLiveData<>();

        db().collection(K.Collections.REGIONS)
                .whereIn("id", ids)
                .get()
                .addOnSuccessListener(q -> {
                    List<Region> regions = q.getDocuments()
                            .stream()
                            .map(d -> d.toObject(Region.class))
                            .collect(Collectors.toList());

                    mutableLiveData.postValue(regions);
                }).addOnFailureListener(Throwable::printStackTrace);

        return mutableLiveData;
    }

    @Override
    public LiveData<List<Region>> getAllRegions() {
        MutableLiveData<List<Region>> mutableLiveData = new MutableLiveData<>();

        db().collection(K.Collections.REGIONS)
                .get()
                .addOnSuccessListener(s -> {
                    List<Region> regions = s.getDocuments().stream()
                            .map(snap -> snap.toObject(Region.class))
                            .collect(Collectors.toList());

                    mutableLiveData.postValue(regions);
                })
                .addOnFailureListener(Throwable::printStackTrace);

        return mutableLiveData;
    }
}
