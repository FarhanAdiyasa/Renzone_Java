package com.example.renzone.models;

import javafx.scene.image.Image;

public class Employee {
    private String idKaryawan;
    private String namaKaryawan;
    private String alamat;
    private String noTelp;
    private Image fotoProfil;
    private String namaPengguna;
    private String kataSandi;

    public Employee(String idKaryawan, String namaKaryawan, String alamat, String noTelp, Image fotoProfil, String namaPengguna) {
        this.idKaryawan = idKaryawan;
        this.namaKaryawan = namaKaryawan;
        this.alamat = alamat;
        this.noTelp = noTelp;
        this.fotoProfil = fotoProfil;
        this.namaPengguna = namaPengguna;
    }

    public Employee(String idKaryawan, String namaKaryawan, String alamat, String noTelp, Image fotoProfil,
                    String namaPengguna, String kataSandi) {
        this.idKaryawan = idKaryawan;
        this.namaKaryawan = namaKaryawan;
        this.alamat = alamat;
        this.noTelp = noTelp;
        this.fotoProfil = fotoProfil;
        this.namaPengguna = namaPengguna;
        this.kataSandi = kataSandi;
    }

    public String getIdKaryawan() {
        return idKaryawan;
    }

    public void setIdKaryawan(String idKaryawan) {
        this.idKaryawan = idKaryawan;
    }

    public String getNamaKaryawan() {
        return namaKaryawan;
    }

    public void setNamaKaryawan(String namaKaryawan) {
        this.namaKaryawan = namaKaryawan;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public Image getFotoProfil() {
        return fotoProfil;
    }

    public void setFotoProfil(Image fotoProfil) {
        this.fotoProfil = fotoProfil;
    }

    public String getNamaPengguna() {
        return namaPengguna;
    }

    public void setNamaPengguna(String namaPengguna) {
        this.namaPengguna = namaPengguna;
    }

    public String getKataSandi() {
        return kataSandi;
    }

    public void setKataSandi(String kataSandi) {
        this.kataSandi = kataSandi;
    }
}

