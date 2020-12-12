package com.example.elbuensabor.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities={Carrito.class},version = 1)
public abstract class CarritoDB extends RoomDatabase {
    public abstract CarritoDao carritoDao();
}
