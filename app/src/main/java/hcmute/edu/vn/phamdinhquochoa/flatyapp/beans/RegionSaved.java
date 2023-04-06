package hcmute.edu.vn.phamdinhquochoa.flatyapp.beans;

public class RegionSaved {
    private Integer RegionId;
    private Integer userId;

    public RegionSaved(Integer RegionId, Integer userId){
        this.RegionId = RegionId;
        this.userId = userId;
    }

    public Integer getRegionId() {
        return RegionId;
    }

    public void setRegionId(Integer RegionId) {
        this.RegionId = RegionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
