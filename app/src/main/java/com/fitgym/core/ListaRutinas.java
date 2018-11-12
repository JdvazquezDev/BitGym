package com.fitgym.core;

import java.util.Date;
import java.util.HashMap;


public class ListaRutinas {

    private HashMap<Date,Rutina> rutinas;

    public ListaRutinas(){this.rutinas = new HashMap<>();}

    public ListaRutinas(HashMap<Date,Rutina> rutinas){
        this.rutinas = rutinas;
    }

    public HashMap<Date, Rutina> getRutinas() {
        return rutinas;
    }

    public void setRutinas(HashMap<Date, Rutina> rutinas) {
        this.rutinas = rutinas;
    }
    public void addRutina(Date fecha,Rutina rutina){
            rutinas.put(fecha,rutina);
    }

    public Rutina getRutinas(Date fecha){
        return rutinas.get(fecha);
    }

    @Override
    public String toString() {
        return "ListaRutinas{" +
                "rutinas=" + rutinas +
                '}';
    }
}

