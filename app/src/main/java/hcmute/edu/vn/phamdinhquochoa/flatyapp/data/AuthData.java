package hcmute.edu.vn.phamdinhquochoa.flatyapp.data;

import javax.annotation.Nullable;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.User;

public interface AuthData {

    DataTask signIn(String email, String password);

    DataTask changePassword(String password);

    DataTask register(String email, String password);

    DataTask logout();

    void setUser(User user);

    @Nullable
    User getUser();

    String getUserId();

}
