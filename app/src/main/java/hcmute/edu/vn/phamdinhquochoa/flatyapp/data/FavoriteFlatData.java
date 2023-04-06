package hcmute.edu.vn.phamdinhquochoa.flatyapp.data;

import androidx.lifecycle.LiveData;

import java.util.List;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Flat;

public interface FavoriteFlatData {

    DataTask addFavorite(String id);

    DataTask removeFavorite(String id);

    LiveData<List<Flat>> getFavoriteFlats();

}
