package com.example.renzone.models;

public class Ps {
    private String idPs;
    private String noSeri;
    private String deskripsi;
    private String hargaSewa;
    private String jenisPs;
    private String  namaGame;
    private String  kondisi;
    private int  totalGame;

    public Ps(String idPs, String noSeri, String deskripsi, String jenisPs, String kondisi, int totalGame) {
        this.idPs = idPs;
        this.noSeri = noSeri;
        this.deskripsi = deskripsi;
        this.jenisPs = jenisPs;
        this.kondisi = kondisi;
        this.totalGame = totalGame;
    }

    public int getTotalGame() {
        return totalGame;
    }
    public void setTotalGame(int totalGame) {
        this.totalGame = totalGame;
    }
    public Ps(String idPs, String noSeri, String deskripsi, String hargaSewa, String jenisPs, String namaGame, String kondisi, int totalGame) {
        this.idPs = idPs;
        this.noSeri = noSeri;
        this.deskripsi = deskripsi;
        this.hargaSewa = hargaSewa;
        this.jenisPs = jenisPs;
        this.namaGame = namaGame;
        this.kondisi = kondisi;
        this.totalGame = totalGame;
    }
    public Ps(String idPs, String noSeri, String deskripsi, String hargaSewa, String jenisPs) {
        this.idPs = idPs;
        this.noSeri = noSeri;
        this.deskripsi = deskripsi;
        this.hargaSewa = hargaSewa;
        this.jenisPs = jenisPs;

    }

    public Ps(String idPs, String noSeri, String deskripsi, String hargaSewa, String jenisPs, String kondisi, int totalGame) {
        this.idPs = idPs;
        this.noSeri = noSeri;
        this.deskripsi = deskripsi;
        this.hargaSewa = hargaSewa;
        this.jenisPs = jenisPs;
        this.kondisi = kondisi;
        this.totalGame = totalGame;
    }

    /*    public Ps(String idPs, String noSeri, String deskripsi, int hargaSewa, String jenisPs, String namaGame) {
            this.idPs = idPs;
            this.noSeri = noSeri;
            this.deskripsi = deskripsi;
            this.hargaSewa = hargaSewa;
            this.jenisPs = jenisPs;
            this.namaGame = namaGame;
        }*/
    public Ps(String idPs, String noSeri, String deskripsi, String hargaSewa, String jenisPs, String kondisi) {
        this.idPs = idPs;
        this.noSeri = noSeri;
        this.deskripsi = deskripsi;
        this.hargaSewa = hargaSewa;
        this.jenisPs = jenisPs;
        this.kondisi = kondisi;
    }

    public String getIdPs() {
        return idPs;
    }

    public void setIdPs(String idPs) {
        this.idPs = idPs;
    }

    public String getNoSeri() {
        return noSeri;
    }

    public void setNoSeri(String noSeri) {
        this.noSeri = noSeri;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getHargaSewa() {
        return hargaSewa;
    }

    public void setHargaSewa(String hargaSewa) {
        this.hargaSewa = hargaSewa;
    }

    public String getJenisPs() {
        return jenisPs;
    }

    public void setJenisPs(String jenisPs) {
        this.jenisPs = jenisPs;
    }


    public String getNamaGame() {
        return namaGame;
    }

    public void setNamaGame(String jenisPs) {
        this.namaGame = namaGame;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }


}

