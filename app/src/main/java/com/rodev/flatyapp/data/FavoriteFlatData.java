package com.rodev.flatyapp.data;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.rodev.flatyapp.beans.FavoriteFlat;

public interface FavoriteFlatData {

    DataTask addFavorite(String id);

    DataTask removeFavorite(String id);

    CompletableFuture<Void> removeFavoritesByFlatId(List<String> ids);

    LiveData<List<FavoriteFlat>> getFavoriteFlats();

}
