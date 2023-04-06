package hcmute.edu.vn.phamdinhquochoa.flatyapp.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
    public LiveData<List<Flat>> getFavoriteFlats() {
        MutableLiveData<List<Flat>> mutableLiveData = new MutableLiveData<>();

        db().collection(K.Collections.FAVORITE_FLATS)
                .whereEqualTo("userId", getUserId())
                .get().addOnSuccessListener(t -> {
                    FlatData flatData = DataAccess.getDataService().getFlatData();

                    List<Flat> flats = t.getDocuments().stream()
                            .map(d -> d.toObject(FavoriteFlat.class))
                            .map(flatData::convertFavorite)
                            .collect(Collectors.toList());

                    mutableLiveData.postValue(flats);
                });

        return mutableLiveData;
    }
}
