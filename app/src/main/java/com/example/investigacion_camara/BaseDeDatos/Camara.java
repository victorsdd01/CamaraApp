package com.example.investigacion_camara.BaseDeDatos;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class Camara extends SQLiteOpenHelper{

    String tablaUsuario="create table usuarios(nombre_usuario text,password_usuario text,correo_usuario text)";

    public Camara(@Nullable Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         db.execSQL(tablaUsuario);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
