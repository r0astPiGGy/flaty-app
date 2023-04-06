package hcmute.edu.vn.phamdinhquochoa.flatyapp.data;

import java.util.concurrent.CompletableFuture;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.beans.User;

public interface UserData {

    DataTask addUser(User user);

    CompletableFuture<User> getUserById(String id);

    DataTask updateUser(User user);

}
