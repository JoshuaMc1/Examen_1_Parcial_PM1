package com.example.examen_1_parcial_pm1.clases;

public class Pais {
    private Integer id;
    private String nombrePais;

    public Pais(Integer id,  String nombrePais) {
        this.id = id;
        this.nombrePais= nombrePais;
    }

    public Pais(){
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer idPais) {
        this.id = id;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }
}
