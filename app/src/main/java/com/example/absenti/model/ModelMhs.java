package com.example.absenti.model;

public class ModelMhs {
    private String id;
    private String nama;
    private String stat;

    public ModelMhs(String id, String nama, String stat) {
        this.id = id;
        this.nama = nama;
        this.stat = stat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
