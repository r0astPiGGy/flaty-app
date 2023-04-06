package hcmute.edu.vn.phamdinhquochoa.flatyapp.data;

import androidx.lifecycle.LiveData;

import java.util.List;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Region;

public interface FavoriteRegionData {

    DataTask addFavorite(String regionId);

    DataTask removeFavorite(String regionId);

    LiveData<List<Region>> getFavoriteRegions();

}
