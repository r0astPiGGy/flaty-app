package hcmute.edu.vn.phamdinhquochoa.flatyapp.data;

import androidx.lifecycle.LiveData;

import java.util.List;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Region;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FavoriteRegion;

public interface RegionData {

    LiveData<Region> getRegionById(String id);

    Region getRegionByIdBlocking(String id);

    LiveData<List<Region>> getAllRegions();

    Region convertFavorite(FavoriteRegion favoriteRegion);

}
