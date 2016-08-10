package com.example.leo.bva;

/**
 * Created by Leo on 05/07/2016.
 */
public class ListaJugador {
    private Integer Pelota;
    private String Nombre;
    private String Posicion;

    public ListaJugador(String Nombre, String Posicion, int Pelota){

        setNombre(Nombre);
        setPosicion(Posicion);
        setPelota(Pelota);
    }

    public void setPelota(Integer pelota) {
        this.Pelota = pelota;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public void setPosicion(String posicion) {
        this.Posicion = posicion;
    }

    public Integer getPelota() {
        return Pelota;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getPosicion() {
        return Posicion;
    }
}
