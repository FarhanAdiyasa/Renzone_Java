package com.example.renzone.models;



public class bLayanan {
    private String Id;
    private String Nama;
    private String Tipe;
    private String Deskripsi;
    private int Durasi;
    private String Biaya;
    private String JenisP;

    public bLayanan(String id, String nama, String tipe, String deskripsi, int durasi, String biaya, String jenisP) {
        Id = id;
        Nama = nama;
        Tipe = tipe;
        Deskripsi = deskripsi;
        Durasi = durasi;
        Biaya = biaya;
        JenisP = jenisP;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getTipe() {
        return Tipe;
    }

    public void setTipe(String tipe) {
        Tipe = tipe;
    }

    public String getDeskripsi() {
        return Deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        Deskripsi = deskripsi;
    }

    public int getDurasi() {
        return Durasi;
    }

    public void setDurasi(int durasi) {
        Durasi = durasi;
    }

    public String getBiaya() {
        return Biaya;
    }

    public void setBiaya(String biaya) {
        Biaya = biaya;
    }

    public String getJenisP() {
        return JenisP;
    }

    public void setJenisP(String jenisP) {
        JenisP = jenisP;
    }
}

