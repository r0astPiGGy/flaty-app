package com.rodev.flatyapp.data;

import java.util.concurrent.CompletableFuture;

import com.rodev.flatyapp.beans.User;

public interface UserData {

    DataTask addUser(User user);

    CompletableFuture<User> getUserById(String id);

    DataTask updateUser(User user);

}
