package com.fitgym.core;

public class Ejercicio {

    private String nombre;

    public Ejercicio(String n)
    {
        this.nombre = n;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString()
    {
        return this.getNombre();
    }
}


