package hcmute.edu.vn.phamdinhquochoa.flatyapp.beans;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.UUID;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.dao.DataAccess;

public class Flat implements Serializable {

    private String id;
    private String name;
    private String type;
    @Exclude
    private byte[] image;
    private String description;
    private String regionId;

    public Flat() {
        this(UUID.randomUUID().toString(), "Unnamed", null, null, "No description", null);
    }

    public Flat(String id, String name, String type, byte[] image, String description, String RegionId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.image = image;
        this.description = description;
        this.regionId = RegionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    @Nullable
    @Exclude
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

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String RegionId) {
        this.regionId = RegionId;
    }

    @Exclude
    public Flat copy() {
        return new Flat(id, name, type, image, description, regionId);
    }

    @Exclude
    public Flat copyAndApply(Consumer<Flat> copyConsumer) {
        Flat copy = copy();

        copyConsumer.accept(copy);
        return copy;
    }
}
