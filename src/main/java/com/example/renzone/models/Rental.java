package com.example.renzone.models;

public class Rental {

    private String idAK;
    private String idM;
    private String Jenis;
    private String namaAK;

    public Rental(String idAK, String idM, String jenis, String namaAK) {
        this.idAK = idAK;
        this.idM = idM;
        Jenis = jenis;
        this.namaAK = namaAK;
    }

    public String getJenis() {
        return Jenis;
    }

    public void setJenis(String jenis) {
        Jenis = jenis;
    }

    public Rental(String idAK, String idM, String namaAK) {
        this.idAK = idAK;
        this.idM = idM;
        this.namaAK = namaAK;
    }

    public String getIdAK() {
        return idAK;
    }

    public void setIdAK(String idAK) {
        this.idAK = idAK;
    }

    public String getIdM() {
        return idM;
    }

    public void setIdM(String idM) {
        this.idM = idM;
    }

    public String getNamaAK() {
        return namaAK;
    }

    public void setNamaAK(String namaAK) {
        this.namaAK = namaAK;
    }
}
