package com.fitgym.core;

import java.util.ArrayList;
import java.util.Date;


public class Rutina {

    public ArrayList<Ejercicio> ejercicios;

    public Rutina(ArrayList<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
    }

    public ArrayList<Ejercicio> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(ArrayList<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
    }

    public void addEjercicio(Ejercicio ejercicio){
        ejercicios.add(ejercicio);
    }

    public Ejercicio getEjercicio(int i){
        return ejercicios.get(i);
    }


    @Override
    public String toString() {
        return "Rutina{" +
                "ejercicios=" + ejercicios +
                '}';
    }
}
