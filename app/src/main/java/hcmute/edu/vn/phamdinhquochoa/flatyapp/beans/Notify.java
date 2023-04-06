package hcmute.edu.vn.phamdinhquochoa.flatyapp.beans;

import java.io.Serializable;

public class Notify implements Serializable {
    private Integer id;
    private String title;
    private String content;
    private String dateMake;

    public Notify(Integer id, String title, String content, String dateMake) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateMake = dateMake;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateMake() {
        return dateMake;
    }

    public void setDateMake(String dateMake) {
        this.dateMake = dateMake;
    }
}
