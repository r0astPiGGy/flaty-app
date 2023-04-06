package hcmute.edu.vn.phamdinhquochoa.flatyapp.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.K;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Region;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FavoriteRegion;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.DataTask;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.FavoriteRegionData;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.RegionData;

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

        db().collection(K.Collections.FAVORITE_FLATS)
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
    public LiveData<List<Region>> getFavoriteRegions() {
        MutableLiveData<List<Region>> regions = new MutableLiveData<>();
        db().collection(K.Collections.FAVORITE_REGIONS)
                .whereEqualTo("userId", getUserId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    RegionData regionData = DataAccess.getDataService().getRegionData();
                    List<Region> region = queryDocumentSnapshots
                            .getDocuments()
                            .stream()
                            .map(d -> d.toObject(FavoriteRegion.class))
                            .map(regionData::convertFavorite)
                            .collect(Collectors.toList());

                    regions.postValue(region);
                });

        return regions;
    }
}
