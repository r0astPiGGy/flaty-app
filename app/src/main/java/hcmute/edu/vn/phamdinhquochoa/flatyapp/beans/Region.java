package hcmute.edu.vn.phamdinhquochoa.flatyapp.beans;

import java.io.Serializable;

public class Region implements Serializable {
    private Integer id;
    private String name;
    private String address;
    private String phone;
    private byte[] image;

    public Region(Integer id, String name, String address, String phone, byte[] image){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
