package com.example.renzone.models;

import javafx.scene.image.Image;

import java.sql.Blob;

public class Member {
    private String idMember;
    private String namaMember;
    private String alamat;
    private String noTelp;
    private String tglDaftar;
    private String statusAnggota;
    private Blob fotoProfil;
    private String namaPengguna;
    private String kataSandi;
    private Image ktp;

    public Member() {
        // Default constructor
    }

    public Member(String idMember, String namaMember, String alamat, String noTelp, String tglDaftar, String statusAnggota, Image ktp) {
        this.idMember = idMember;
        this.namaMember = namaMember;
        this.alamat = alamat;
        this.noTelp = noTelp;
        this.tglDaftar = tglDaftar;
        this.statusAnggota = statusAnggota;
        this.ktp = ktp;
    }

    public Member(String idMember, String namaMember) {
        this.idMember = idMember;
        this.namaMember = namaMember;
    }

    public Member(String idMember, String namaMember, String alamat, String noTelp, String tglDaftar,
                  String statusAnggota, Blob fotoProfil, String namaPengguna, String kataSandi) {
        this.idMember = idMember;
        this.namaMember = namaMember;
        this.alamat = alamat;
        this.noTelp = noTelp;
        this.tglDaftar = tglDaftar;
        this.statusAnggota = statusAnggota;
        this.fotoProfil = fotoProfil;
        this.namaPengguna = namaPengguna;
        this.kataSandi = kataSandi;
    }

    public String getIdMember() {
        return idMember;
    }

    public void setIdMember(String idMember) {
        this.idMember = idMember;
    }

    public String getNamaMember() {
        return namaMember;
    }

    public void setNamaMember(String namaMember) {
        this.namaMember = namaMember;
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

    public String getTglDaftar() {
        return tglDaftar;
    }

    public void setTglDaftar(String tglDaftar) {
        this.tglDaftar = tglDaftar;
    }

    public String getStatusAnggota() {
        return statusAnggota;
    }

    public void setStatusAnggota(String statusAnggota) {
        this.statusAnggota = statusAnggota;
    }

    public Blob getFotoProfil() {
        return fotoProfil;
    }

    public void setFotoProfil(Blob fotoProfil) {
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
