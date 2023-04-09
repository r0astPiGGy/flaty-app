package hcmute.edu.vn.phamdinhquochoa.flatyapp.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.K;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FavoriteFlat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Flat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.DataTask;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.FlatData;

public class FlatDataImpl extends FirebaseDataContext implements FlatData {

    public FlatDataImpl(Supplier<FirebaseFirestore> firebaseFirestore) {
        super(firebaseFirestore);
    }

    @Override
    public DataTask addFlat(Flat flat) {
        return updateFlat(flat);
    }

    @Override
    public DataTask updateFlat(Flat flat) {
        DataTask.Invokable dataTask = createTask();

        db().collection(K.Collections.FLATS)
                .document(flat.getId())
                .set(flat)
                .addOnCompleteListener(t -> {
                    if(t.isSuccessful()) {
                        updateFlatImage(dataTask, flat);
                    } else {
                        dataTask.invokeOnFailure(t.getException());
                    }
                });

        return dataTask;
    }

    private void updateFlatImage(DataTask.Invokable task, Flat flat) {
        DataAccess.getDataService().getImageStorage()
                .updateImageByUri(flat.getImage(), flat.getId())
                .whenComplete((Null, exception) -> {
                    if(exception == null) {
                        task.invokeOnComplete();
                    } else {
                        task.invokeOnFailure(exception);
                    }
                });
    }

    @Override
    public LiveData<Flat> getFlatById(String id) {
        MutableLiveData<Flat> mutableLiveData = new MutableLiveData<>();

        db().collection(K.Collections.FLATS)
                .document(id)
                .get()
                .addOnSuccessListener(q -> {
                    mutableLiveData.postValue(q.toObject(Flat.class));
                }).addOnFailureListener(Throwable::printStackTrace);

        return mutableLiveData;
    }

    @Override
    public LiveData<List<Flat>> getFlatsByIds(List<String> ids) {
        MutableLiveData<List<Flat>> mutableLiveData = new MutableLiveData<>();

        db().collection(K.Collections.FLATS)
                .whereIn("id", ids)
                .get()
                .addOnSuccessListener(q -> {
                    List<Flat> flats = q.getDocuments()
                            .stream()
                            .map(d -> d.toObject(Flat.class))
                            .collect(Collectors.toList());

                    mutableLiveData.postValue(flats);
                }).addOnFailureListener(Throwable::printStackTrace);

        return mutableLiveData;
    }

    @Override
    public DataTask deleteFlatById(String id) {
        DataTask.Invokable dataTask = createTask();

        db().collection(K.Collections.FLATS)
                .document(id)
                .delete()
                .addOnSuccessListener(q -> {
                    dataTask.invokeOnComplete();
                }).addOnFailureListener(dataTask::invokeOnFailure);

        return dataTask;
    }

    @Override
    public DataTask deleteFlatsByRegionId(String regionId) {
        DataTask.Invokable task = DataTask.createDataTask();

        db().collection(K.Collections.FLATS)
                .whereEqualTo("regionId", regionId)
                .get()
                .addOnSuccessListener(q -> {
                    List<DocumentReference> references = q.getDocuments().stream()
                            .map(DocumentSnapshot::getReference)
                            .collect(Collectors.toList());

                    List<String> flatsById = references.stream()
                            .map(DocumentReference::getId)
                            .collect(Collectors.toList());

                    db().runBatch(batch -> {
                        references.forEach(batch::delete);
                    }).addOnCompleteListener(t -> {
                        deleteFlatImagesById(flatsById);
                        deleteFavoriteFlatsById(flatsById);

                        handleTaskCompletion(t, task);
                    });
                })
                .addOnFailureListener(task::invokeOnFailure);

        return task;
    }

    private void deleteFlatImagesById(List<String> ids) {
        DataAccess.getDataService()
                .getImageStorage()
                .deleteImagesByUri(ids);
    }

    private void deleteFavoriteFlatsById(List<String> ids) {
        DataAccess.getDataService()
                .getFavoriteFlatData()
                .removeFavoritesByFlatId(ids);
    }

    @Override
    public LiveData<List<Flat>> getAllFlats() {
        MutableLiveData<List<Flat>> liveData = new MutableLiveData<>();

        db().collection(K.Collections.FLATS)
                .get()
                .addOnSuccessListener(q -> {
                    List<Flat> flats = q.getDocuments().stream()
                            .map(snap -> snap.toObject(Flat.class))
                            .collect(Collectors.toList());

                    liveData.postValue(flats);
                })
                .addOnFailureListener(Throwable::printStackTrace);

        return liveData;
    }

    @Override
    public LiveData<List<Flat>> getFlatsByRegionId(String id) {
        MutableLiveData<List<Flat>> liveData = new MutableLiveData<>();

        db().collection(K.Collections.FLATS)
                .whereEqualTo("regionId", id)
                .get()
                .addOnSuccessListener(q -> {
                    List<Flat> flats = q.getDocuments().stream()
                            .map(snap -> snap.toObject(Flat.class))
                            .collect(Collectors.toList());

                    liveData.postValue(flats);
                })
                .addOnFailureListener(Throwable::printStackTrace);

        return liveData;
    }

    @Override
    public LiveData<List<Flat>> getFlatsByKeyWord(String keyWord) {
        return getAllFlats();
    }

    @Override
    public CompletableFuture<Flat> convertFavorite(FavoriteFlat favoriteFlat) {
        CompletableFuture<Flat> task = new CompletableFuture<>();

        FirebaseFirestore.getInstance()
                .collection(K.Collections.FLATS)
                .document(favoriteFlat.getFlatId())
                .get()
                .addOnSuccessListener(d -> task.complete(d.toObject(Flat.class)))
                .addOnFailureListener(task::completeExceptionally);

        return task;
    }
}
