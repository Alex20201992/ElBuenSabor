package com.example.elbuensabor.entities;

public class Repartidor {
    private int idrepartidor;
    private String nombre;

    public Repartidor(int idrepartidor, String nombre) {
        this.idrepartidor = idrepartidor;
        this.nombre = nombre;
    }

    public int getIdrepartidor() {
        return idrepartidor;
    }

    public void setIdrepartidor(int idrepartidor) {
        this.idrepartidor = idrepartidor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Repartidor){
            Repartidor c = (Repartidor ) obj;
            if(c.getNombre().equals(nombre) && c.getIdrepartidor()==idrepartidor) return true;
        }

        return false;
    }
}
