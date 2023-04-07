package hcmute.edu.vn.phamdinhquochoa.flatyapp.data;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FavoriteFlat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Flat;

public interface FlatData {

    DataTask addFlat(Flat flat);

    LiveData<Flat> getFlatById(String id);

    LiveData<List<Flat>> getFlatsByIds(List<String> ids);

    DataTask deleteFlatById(String id);

    DataTask updateFlat(Flat flat);

    LiveData<List<Flat>> getAllFlats();

    LiveData<List<Flat>> getFlatsByRegionId(String id);

    LiveData<List<Flat>> getFlatsByKeyWord(String keyWord);

    CompletableFuture<Flat> convertFavorite(FavoriteFlat flat);

}
