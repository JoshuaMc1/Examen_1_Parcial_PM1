package com.example.examen_1_parcial_pm1.clases;

import java.sql.Blob;

public class Contactos {
    private Integer id;
    private String nombre;
    private String pais;
    private String telefono;
    private String nota;
    private byte[] imagen;

    public Contactos(Integer id, String nombre, String pais, String telefono, String nota) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.telefono = telefono;
        this.nota = nota;
    }

    public Contactos() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}
