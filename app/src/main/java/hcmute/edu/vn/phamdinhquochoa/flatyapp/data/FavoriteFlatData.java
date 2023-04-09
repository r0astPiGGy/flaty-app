package hcmute.edu.vn.phamdinhquochoa.flatyapp.data;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FavoriteFlat;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Flat;

public interface FavoriteFlatData {

    DataTask addFavorite(String id);

    DataTask removeFavorite(String id);

    CompletableFuture<Void> removeFavoritesByFlatId(List<String> ids);

    LiveData<List<FavoriteFlat>> getFavoriteFlats();

}
