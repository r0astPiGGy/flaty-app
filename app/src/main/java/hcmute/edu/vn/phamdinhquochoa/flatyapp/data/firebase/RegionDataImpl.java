package hcmute.edu.vn.phamdinhquochoa.flatyapp.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.K;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FavoriteRegion;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Flat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Region;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.RegionData;

public class RegionDataImpl extends FirebaseDataContext implements RegionData {

    public RegionDataImpl(Supplier<FirebaseFirestore> firebaseFirestore) {
        super(firebaseFirestore);
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
    public Region getRegionByIdBlocking(String id) {
        final Object locker = new Object();
        AtomicReference<Region> flatAtomicReference = new AtomicReference<>();

        db().collection(K.Collections.REGIONS)
                .document(id)
                .get()
                .addOnCompleteListener(d -> {
                    if(d.isSuccessful()) {
                        flatAtomicReference.set(d.getResult().toObject(Region.class));
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

    @Override
    public Region convertFavorite(FavoriteRegion favoriteRegion) {
        return getRegionByIdBlocking(favoriteRegion.getRegionId());
    }
}
