package com.example.leo.bva;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Leo on 24/05/2016.
 */
public class ProyectoFinalDB extends SQLiteOpenHelper {

    public ProyectoFinalDB(Context paramContext, String paramString, SQLiteDatabase.CursorFactory paramCursorFactory, int paramInt)
    {
        super(paramContext, paramString, paramCursorFactory, paramInt);
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
        try {
            Log.d("SQLite", "Declaro e Incializo la tabla Jugadores");
            Log.d("SQLite", "Declaro e Invoco al creador de la tabla");
            paramSQLiteDatabase.execSQL("create table Jugadores(IdJugador INTEGER PRIMARY KEY AUTOINCREMENT,nombre text,edad text,altura text,posicion text,mail text,direccion text, peso text, partidosJug text)");
            Log.d("SQLite", "Tabla1Creada");
            paramSQLiteDatabase.execSQL("create table EstadisticasXEquipo(IdPartido integer,Puntos integer,Asistencias integer,RebotesDef integer,RebotesOff Integer,Tapones integer,Faltas integer,Perdidas integer,PRIMARY KEY (IdPartido))");
            Log.d("SQLite", "Tabla2Creada");
            paramSQLiteDatabase.execSQL("create table Partidos(idPartido INTEGER PRIMARY KEY AUTOINCREMENT, Vs text, Año text,Mes text,Dia text, Condicion text,PuntosLocal integer,PuntosVisitante integer)");
            Log.d("SQLite", "Tabla3Creada");

            paramSQLiteDatabase.execSQL("create table EstadisticasXJugador(IdPartido integer, IdJugador integer,Puntos integer DEFAULT 0,DoblesErrados integer DEFAULT 0,TirosLibErrados integer DEFAULT 0, TriplesErrados integer DEFAULT 0,DoblesMetidos integer DEFAULT 0, TirosLibMetidos integer DEFAULT 0, TriplesMetidos integer DEFAULT 0,Asistencias integer DEFAULT 0,Robos integer DEFAULT 0,Tapones integer DEFAULT 0,RebOff integer DEFAULT 0,RebDef integer DEFAULT 0,Faltas integer DEFAULT 0,Perdidas integer DEFAULT 0,primary key (IdPartido, IdJugador))");
            Log.d("SQLite", "Tabla4Creada");




        }catch(Exception e){
            Log.d("Error", e.getMessage());
        }


    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {

    }
}