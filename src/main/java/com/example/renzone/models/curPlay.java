package com.example.renzone.models;

public class curPlay {
    private String kodeObat;
    private String namaObat;
    private String merk;
    private String kemasan;
    private String a;

    public curPlay(String kodeObat, String namaObat, String kemasan, String a) {
        this.kodeObat = kodeObat;
        this.namaObat = namaObat;
        this.kemasan = kemasan;
        this.a = a;
    }

    public curPlay(String kodeObat, String namaObat, String merk, String kemasan, String a) {
        this.kodeObat = kodeObat;
        this.namaObat = namaObat;
        this.merk = merk;
        this.kemasan = kemasan;
        this.a = a;



    }

    public String getKodeObat() {
        return kodeObat;
    }

    public void setKodeObat(String kodeObat) {
        this.kodeObat = kodeObat;
    }

    public String getNamaObat() {
        return namaObat;
    }

    public void setNamaObat(String namaObat) {
        this.namaObat = namaObat;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getKemasan() {
        return kemasan;
    }

    public void setKemasan(String kemasan) {
        this.kemasan = kemasan;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }




}
