package com.rodev.flatyapp.data;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.rodev.flatyapp.beans.FavoriteRegion;

public interface FavoriteRegionData {

    DataTask addFavorite(String regionId);

    DataTask removeFavorite(String regionId);

    CompletableFuture<Void> removeFavoritesByRegionId(String id);

    LiveData<List<FavoriteRegion>> getFavoriteRegions();

}
