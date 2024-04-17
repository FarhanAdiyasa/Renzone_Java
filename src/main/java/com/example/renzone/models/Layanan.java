package com.example.renzone.models;

public class Layanan {
    private String idPaket;
    private String namaPaket;
    private String jenisPs;
    private String deskripsi;
    private int durasi;
    private int biaya;

    public Layanan(String namaPaket, String deskripsi, int durasi, int biaya) {
        this.namaPaket = namaPaket;
        this.deskripsi = deskripsi;
        this.durasi = durasi;
        this.biaya = biaya;
    }

    public Layanan(String namaPaket, int biaya) {
        this.namaPaket = namaPaket;
        this.biaya = biaya;
    }

    public Layanan(String idPaket, String namaPaket, String jenisPs, String deskripsi, int durasi, int biaya) {
        this.idPaket = idPaket;
        this.namaPaket = namaPaket;
        this.jenisPs = jenisPs;
        this.deskripsi = deskripsi;
        this.durasi = durasi;
        this.biaya = biaya;
    }

    public String getIdPaket() {
        return idPaket;
    }

    public void setIdPaket(String idPaket) {
        this.idPaket = idPaket;
    }

    public String getNamaPaket() {
        return namaPaket;
    }

    public void setNamaPaket(String namaPaket) {
        this.namaPaket = namaPaket;
    }

    public String getJenisPs() {
        return jenisPs;
    }

    public void setJenisPs(String jenisPs) {
        this.jenisPs = jenisPs;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getDurasi() {
        return durasi;
    }

    public void setDurasi(int durasi) {
        this.durasi = durasi;
    }

    public int getBiaya() {
        return biaya;
    }

    public void setBiaya(int biaya) {
        this.biaya = biaya;
    }
}
