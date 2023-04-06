package hcmute.edu.vn.phamdinhquochoa.flatyapp.beans;

public class FlatSaved {
    private Integer FlatId;
    private Integer size;
    private Integer userId;

    public FlatSaved(Integer FlatId, Integer size, Integer userId){
        this.FlatId = FlatId;
        this.size = size;
        this.userId = userId;
    }

    public Integer getFlatId() {
        return FlatId;
    }

    public void setFlatId(Integer FlatId) {
        this.FlatId = FlatId;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
