package com.example.renzone.models;

import javafx.scene.image.Image;

public class bMember {
    private String idM;
    private String namaM;
    private String alamatM;
    private String notelpM;
    private String tanggalM;
    private String statusM;
    private Image fotoKtp;

    public bMember(String idM, String namaM, String statusM) {
        this.idM = idM;
        this.namaM = namaM;
        this.statusM = statusM;
    }

    public Image getFotoKtp() {
        return fotoKtp;
    }

    public void setFotoKtp(Image fotoKtp) {
        this.fotoKtp = fotoKtp;
    }

    public bMember(String idM, String namaM, String alamatM, String notelpM, String tanggalM, String statusM, Image fotoKtp) {
        this.idM = idM;
        this.namaM = namaM;
        this.alamatM = alamatM;
        this.notelpM = notelpM;
        this.tanggalM = tanggalM;
        this.statusM = statusM;
        this.fotoKtp = fotoKtp;
    }

    public bMember(String idM, String namaM, String alamatM, String notelpM, String tanggalM, String statusM) {
        this.idM = idM;
        this.namaM = namaM;
        this.alamatM = alamatM;
        this.notelpM = notelpM;
        this.tanggalM = tanggalM;
        this.statusM = statusM;
    }

    public String getIdM() {
        return idM;
    }

    public void setIdM(String idM) {
        this.idM = idM;
    }
    public String getNamaM() {
        return namaM;
    }

    public void setNamaM(String namaM) {
        this.namaM = namaM;
    }
    public String getAlamatM() {
        return alamatM;
    }

    public void setAlamatM(String alamatM) {
        this.alamatM = alamatM;
    }
    public String getNotelpM() {
        return notelpM;
    }

    public void setNotelpM(String notelpM) {
        this.notelpM = notelpM;
    }

    public String getTanggalM() {
        return tanggalM;
    }

    public void setTanggalM(String tanggalM) {
        this.tanggalM = tanggalM;
    }
    public String getStatusM() {
        return statusM;
    }

    public void setStatusM(String statusM) {
        this.statusM = statusM;
    }

}
