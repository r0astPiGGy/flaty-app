package hcmute.edu.vn.phamdinhquochoa.flatyapp.beans;

import java.io.Serializable;

public class Flat implements Serializable {

    private Integer id;
    private String name;
    private String type;
    private byte[] image;
    private String description;
    private Integer RegionId;

    public Flat() {}

    public Flat(Integer id, String name, String type, byte[] image, String description, Integer RegionId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.image = image;
        this.description = description;
        this.RegionId = RegionId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRegionId() {
        return RegionId;
    }

    public void setRegionId(Integer RegionId) {
        this.RegionId = RegionId;
    }
}
