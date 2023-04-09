package hcmute.edu.vn.phamdinhquochoa.flatyapp.data;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FavoriteRegion;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Region;

public interface FavoriteRegionData {

    DataTask addFavorite(String regionId);

    DataTask removeFavorite(String regionId);

    CompletableFuture<Void> removeFavoritesByRegionId(String id);

    LiveData<List<FavoriteRegion>> getFavoriteRegions();

}
