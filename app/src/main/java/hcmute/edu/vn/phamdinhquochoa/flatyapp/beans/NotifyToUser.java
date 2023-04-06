package hcmute.edu.vn.phamdinhquochoa.flatyapp.beans;

import java.io.Serializable;

public class NotifyToUser implements Serializable {
    private Integer notifyId;
    private Integer userId;

    public NotifyToUser(Integer notifyId, Integer userId) {
        this.notifyId = notifyId;
        this.userId = userId;
    }

    public NotifyToUser() {
    }

    public Integer getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(Integer notifyId) {
        this.notifyId = notifyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
