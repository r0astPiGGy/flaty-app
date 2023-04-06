package hcmute.edu.vn.phamdinhquochoa.flatyapp.beans;

public class FavoriteRegion {
    private String regionId;
    private String userId;

    public FavoriteRegion() {}

    public FavoriteRegion(String RegionId, String userId){
        this.regionId = RegionId;
        this.userId = userId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String RegionId) {
        this.regionId = RegionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
