package com.rodev.flatyapp.data;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.rodev.flatyapp.beans.FavoriteFlat;
import com.rodev.flatyapp.beans.Flat;

public interface FlatData {

    DataTask addFlat(Flat flat);

    LiveData<Flat> getFlatById(String id);

    LiveData<List<Flat>> getFlatsByIds(List<String> ids);

    DataTask deleteFlatById(String id);

    DataTask deleteFlatsByRegionId(String regionId);

    DataTask updateFlat(Flat flat);

    LiveData<List<Flat>> getAllFlats();

    LiveData<List<Flat>> getFlatsByRegionId(String id);

    LiveData<List<Flat>> getFlatsByKeyWord(String keyWord);

    CompletableFuture<Flat> convertFavorite(FavoriteFlat flat);

}
