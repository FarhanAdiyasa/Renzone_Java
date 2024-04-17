package com.example.renzone.models;

public class bAksesoris {
    private String idAk;
    private String namaAk;
    private String hargaSewa;
    private String jenis;
    private String kondisi;

    public bAksesoris(String idAk, String namaAk, String hargaSewa, String jenis, String kondisi) {
        this.idAk = idAk;
        this.namaAk = namaAk;
        this.hargaSewa = hargaSewa;
        this.jenis = jenis;
        this.kondisi = kondisi;
    }

    public String getIdAk() {
        return idAk;
    }

    public void setIdAk(String idAk) {
        this.idAk = idAk;
    }

    public String getNamaAk() {
        return namaAk;
    }

    public void setNamaAk(String namaAk) {
        this.namaAk = namaAk;
    }

    public String getHargaSewa() {
        return hargaSewa;
    }

    public void setHargaSewa(String hargaSewa) {
        this.hargaSewa = hargaSewa;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

}
