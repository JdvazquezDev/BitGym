package com.fitgym.core;

public class Ejercicio {

    private String nombre;
    private String descripcion;

    public Ejercicio(String n,String descripcion){
        this.nombre = n;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString()
    {
        return this.getNombre() + this.getDescripcion();
    }
}


