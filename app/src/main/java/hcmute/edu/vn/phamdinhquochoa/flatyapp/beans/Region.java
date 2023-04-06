package hcmute.edu.vn.phamdinhquochoa.flatyapp.beans;

import java.io.Serializable;
import java.util.UUID;

public class Region implements Serializable {
    private String id;
    private String name;
    private String address;
    private String phone;
    private byte[] image;

    public Region() {}

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
