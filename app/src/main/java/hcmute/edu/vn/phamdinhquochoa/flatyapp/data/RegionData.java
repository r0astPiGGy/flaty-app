package hcmute.edu.vn.phamdinhquochoa.flatyapp.data;

import androidx.lifecycle.LiveData;

import java.util.List;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.Region;
import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.FavoriteRegion;

public interface RegionData {

    DataTask addRegion(Region region);

    DataTask updateRegion(Region region);

    DataTask deleteRegion(Region region);

    LiveData<Region> getRegionById(String id);

    LiveData<List<Region>> getRegionsByIds(List<String> ids);

    LiveData<List<Region>> getAllRegions();

}
