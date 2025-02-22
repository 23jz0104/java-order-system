package model;

import java.io.Serializable;

public class Status implements Serializable {
    private int    id;
    private String name;

    public Status(int id, String name) {
        super();
        this.id   = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "statuses [id=" + id + ", name=" + name + "]";
    }

}
