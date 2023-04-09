package com.rodev.flatyapp.data;

import androidx.lifecycle.LiveData;

import java.util.List;

import com.rodev.flatyapp.beans.Region;

public interface RegionData {

    DataTask addRegion(Region region);

    DataTask updateRegion(Region region);

    DataTask deleteRegion(Region region);

    LiveData<Region> getRegionById(String id);

    LiveData<List<Region>> getRegionsByIds(List<String> ids);

    LiveData<List<Region>> getAllRegions();

}
