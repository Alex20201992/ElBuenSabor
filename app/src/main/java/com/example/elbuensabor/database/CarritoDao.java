package com.example.elbuensabor.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CarritoDao {
    @Insert
    public void addToCart(Carrito carrito);

    @Query("SELECT * FROM TCarrito")
    public List<Carrito>getData();

    @Query("SELECT EXISTS (SELECT 1 FROM TCarrito WHERE id=:id)")
    public int isAddToCart(int id);

    @Query("SELECT COUNT (*) from TCarrito")
    int countCart();

    @Query("DELETE FROM TCarrito WHERE id=:id ")
    int deleteItem(int id);

    @Query("DELETE FROM TCarrito")
    public void deleteCart();

    @Query("UPDATE TCarrito SET cantidad=:cantidad, total=precio*:cantidad WHERE id=:id")
    public void updateCantCart(int cantidad, int id);

    @Query("SELECT sum(total) from TCarrito")
    double getprecioTotal();

    @Query("SELECT cantidad from TCarrito WHERE id=:id")
    int getCant(int id);
}
