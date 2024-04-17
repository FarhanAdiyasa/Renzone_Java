package com.example.renzone.models;

public class bMeja {
    private String idMeja;
    private String idPS;
    private String namaTv;
    private String desk;

    public bMeja(String idMeja, String idPS, String namaTv, String desk) {
        this.idMeja = idMeja;
        this.idPS = idPS;
        this.namaTv = namaTv;
        this.desk = desk;
    }

    public String getIdMeja() {
        return idMeja;
    }

    public void setIdMeja(String idMeja) {
        this.idMeja = idMeja;
    }

    public String getIdPS() {
        return idPS;
    }

    public void setIdPS(String idPS) {
        this.idPS = idPS;
    }

    public String getNamaTv() {
        return namaTv;
    }

    public void setNamaTv(String namaTv) {
        this.namaTv = namaTv;
    }

    public String getDesk() {
        return desk;
    }

    public void setDesk(String desk) {
        this.desk = desk;
    }
}
