package com.example.elbuensabor.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TCarrito")
public class Carrito {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "nombre")
    public String nombre;

    @ColumnInfo(name = "imageid")
    public String imageid;

    @ColumnInfo(name = "precio")
    public Double precio;

    @ColumnInfo(name = "cantidad")
    public int cantidad;

    @ColumnInfo(name = "total")
    public Double total;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
