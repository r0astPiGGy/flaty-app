package hcmute.edu.vn.phamdinhquochoa.flatyapp.beans;

import java.io.Serializable;

public class FlatSize implements Serializable {
    private Integer FlatId;
    private Integer size;
    private Double price;

    public FlatSize() {
    }

    public FlatSize(Integer FlatId, Integer size, Double price) {
        this.FlatId = FlatId;
        this.size = size;
        this.price = price;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
