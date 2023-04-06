package hcmute.edu.vn.phamdinhquochoa.flatyapp.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.K;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FavoriteFlat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Flat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.DataTask;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.FlatData;

public class FlatDataImpl extends FirebaseDataContext implements FlatData {

    public FlatDataImpl(Supplier<FirebaseFirestore> firebaseFirestore) {
        super(firebaseFirestore);
    }

    @Override
    public DataTask addFlat(Flat flat) {
        DataTask.Invokable task = DataTask.createDataTask();

        db().collection(K.Collections.FLATS)
                .document(flat.getId())
                .set(flat)
                .addOnCompleteListener(t -> handleTaskCompletion(t, task));

        return task;
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
    public DataTask updateFlat(Flat flat) {
        DataTask.Invokable dataTask = createTask();

        db().collection(K.Collections.FLATS)
                .document(flat.getId())
                .set(flat)
                .addOnCompleteListener(t -> handleTaskCompletion(t, dataTask));

        return dataTask;
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
    public Flat convertFavorite(FavoriteFlat favoriteFlat) {
        final Object locker = new Object();
        AtomicReference<Flat> flatAtomicReference = new AtomicReference<>();

        db().collection(K.Collections.FLATS)
                .document(favoriteFlat.getFlatId())
                .get()
                .addOnCompleteListener(d -> {
                    if(d.isSuccessful()) {
                        flatAtomicReference.set(d.getResult().toObject(Flat.class));
                    }

                    synchronized (locker) {
                        locker.notifyAll();
                    }
                });

        synchronized (locker) {
            try {
                locker.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return flatAtomicReference.get();
    }
}
