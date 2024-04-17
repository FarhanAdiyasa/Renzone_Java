package com.example.renzone.models;

public class Game {
    private String id;
    private String nama;
    private String desk;
    private String tgl;
    private String jenis_ps;
    private String ctgry;

    public String getJenis_ps() {
        return jenis_ps;
    }

    public void setJenis_ps(String jenis_ps) {
        this.jenis_ps = jenis_ps;
    }

    public Game(String id, String nama, String desk, String tgl, String jenis_ps, String ctgry) {
        this.id = id;
        this.nama = nama;
        this.desk = desk;
        this.tgl = tgl;
        this.jenis_ps = jenis_ps;
        this.ctgry = ctgry;
    }

    public Game(String id, String nama, String desk, String tgl, String ctgry) {
        this.id = id;
        this.nama = nama;
        this.desk = desk;
        this.tgl = tgl;
        this.ctgry = ctgry;
    }


    public String getCtgry() {
        return ctgry;
    }

    public void setCtgry(String ctgry) {
        this.ctgry = ctgry;
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

    public String getDesk() {
        return desk;
    }

    public void setDesk(String desk) {
        this.desk = desk;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }
}
