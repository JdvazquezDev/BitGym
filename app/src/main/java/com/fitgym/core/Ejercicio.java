package com.fitgym.core;

public class Ejercicio {

    private String nombre;
    private String descripcion;
    private byte[] imagen;

    public Ejercicio(String n,String descripcion,byte[] imagen){
        this.nombre = n;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public byte[] getImagen(){ return imagen; }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setImagen(byte[] imagen){this.imagen = imagen;}
    @Override
    public String toString()
    {
        return this.getNombre() + this.getDescripcion() + this.getImagen();
    }
}


