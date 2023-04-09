package com.rodev.flatyapp.beans;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.UUID;
import java.util.function.Consumer;

import javax.annotation.Nullable;

public class Region implements Serializable {
    private String id;
    private String name;
    private String address;
    private String phone;
    @Exclude
    private byte[] image;

    public Region() {
        id = UUID.randomUUID().toString();
    }

    public Region(String name, String address, String phone, byte[] image) {
        this(UUID.randomUUID().toString(), name, address, phone, image);
    }

    public Region(String id, String name, String address, String phone, byte[] image){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Exclude
    @Nullable
    public byte[] getImage() {
        return image;
    }

    @Exclude
    public Region copy() {
        return new Region(id, name, address, phone, image);
    }

    @Exclude
    public Region copyAndApply(Consumer<Region> copyConsumer) {
        Region copy = copy();

        copyConsumer.accept(copy);
        return copy;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
