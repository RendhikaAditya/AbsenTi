package com.example.absenti.model;

public class ModelKelas {
    private String id;
    private String dosen;
    private String kelas;
    private String tgl;
    private String matkul;
    private String code;

    public ModelKelas(String id, String dosen, String kelas, String tgl, String matkul, String code) {
        this.id = id;
        this.dosen = dosen;
        this.kelas = kelas;
        this.tgl = tgl;
        this.matkul = matkul;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDosen() {
        return dosen;
    }

    public void setDosen(String dosen) {
        this.dosen = dosen;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getMatkul() {
        return matkul;
    }

    public void setMatkul(String matkul) {
        this.matkul = matkul;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
