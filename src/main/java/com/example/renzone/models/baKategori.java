package com.example.renzone.models;

public class baKategori {
    private String idK;
    private String namaK;

    public baKategori(String idK, String namaK) {
        this.idK = idK;
        this.namaK= namaK;
    }

    public String getIdK() {
        return idK;
    }

    public void setIdK(String idAk) {
        this.idK = idK;
    }

    public String getNamaK() {
        return namaK;
    }

    public void setNamaK(String namaK) {
        this.namaK = namaK;
    }
}


