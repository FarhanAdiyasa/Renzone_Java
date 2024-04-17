package com.example.renzone.models;
import java.util.Date;

public class TrsPeminjaman {
    private String idPeminjaman;
    private String tanggalPeminjaman;
    private String tanggalPengembalian;
    private double biaya;
    private String idMember;
    private String idKaryawan;
    private double bayar;
    private byte[] fotoKtp;
    private String rincian;

    public TrsPeminjaman(String idPeminjaman, String tanggalPeminjaman, String tanggalPengembalian, double biaya, String idMember, String idKaryawan, String rincian) {
        this.idPeminjaman = idPeminjaman;
        this.tanggalPeminjaman = tanggalPeminjaman;
        this.tanggalPengembalian = tanggalPengembalian;
        this.biaya = biaya;
        this.idMember = idMember;
        this.idKaryawan = idKaryawan;
        this.rincian = rincian;
    }

    public String getRincian() {
        return rincian;
    }

    public void setRincian(String rincian) {
        this.rincian = rincian;
    }

    public TrsPeminjaman(String idPeminjaman, String tanggalPeminjaman, String tanggalPengembalian, double biaya, String idMember, String idKaryawan, double bayar, String rincian) {
        this.idPeminjaman = idPeminjaman;
        this.tanggalPeminjaman = tanggalPeminjaman;
        this.tanggalPengembalian = tanggalPengembalian;
        this.biaya = biaya;
        this.idMember = idMember;
        this.idKaryawan = idKaryawan;
        this.bayar = bayar;
        this.rincian = rincian;
    }

    public TrsPeminjaman(String idPeminjaman, String tanggalPeminjaman, String tanggalPengembalian, double biaya, String idMember, String idKaryawan, double bayar, byte[] fotoKtp) {
        this.idPeminjaman = idPeminjaman;
        this.tanggalPeminjaman = tanggalPeminjaman;
        this.tanggalPengembalian = tanggalPengembalian;
        this.biaya = biaya;
        this.idMember = idMember;
        this.idKaryawan = idKaryawan;
        this.bayar = bayar;
        this.fotoKtp = fotoKtp;
    }

    public String getIdPeminjaman() {
        return idPeminjaman;
    }

    public void setIdPeminjaman(String idPeminjaman) {
        this.idPeminjaman = idPeminjaman;
    }

    public String getTanggalPeminjaman() {
        return tanggalPeminjaman;
    }

    public void setTanggalPeminjaman(String tanggalPeminjaman) {
        this.tanggalPeminjaman = tanggalPeminjaman;
    }

    public String getTanggalPengembalian() {
        return tanggalPengembalian;
    }

    public void setTanggalPengembalian(String tanggalPengembalian) {
        this.tanggalPengembalian = tanggalPengembalian;
    }

    public double getBiaya() {
        return biaya;
    }

    public void setBiaya(double biaya) {
        this.biaya = biaya;
    }

    public String getIdMember() {
        return idMember;
    }

    public void setIdMember(String idMember) {
        this.idMember = idMember;
    }

    public String getIdKaryawan() {
        return idKaryawan;
    }

    public void setIdKaryawan(String idKaryawan) {
        this.idKaryawan = idKaryawan;
    }

    public double getBayar() {
        return bayar;
    }

    public void setBayar(double bayar) {
        this.bayar = bayar;
    }

    public byte[] getFotoKtp() {
        return fotoKtp;
    }

    public void setFotoKtp(byte[] fotoKtp) {
        this.fotoKtp = fotoKtp;
    }
}
