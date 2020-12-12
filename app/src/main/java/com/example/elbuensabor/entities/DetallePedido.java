package com.example.elbuensabor.entities;

public class DetallePedido {
    private int idproducto;
    private double precio;
    private int cantidad;

    public DetallePedido(int idproducto, double precio, int cantidad) {
        this.idproducto = idproducto;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
