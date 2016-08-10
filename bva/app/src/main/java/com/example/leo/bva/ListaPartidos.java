package com.example.leo.bva;

/**
 * Created by Leo on 05/07/2016.
 */
public class ListaPartidos {
    private Integer Resultado;
    private String Fecha;
    private String VS;

    public ListaPartidos(String VS, String Fecha, int Resultado){

        setVS(VS);
        setFecha(Fecha);
        setResultado(Resultado);
    }

    public void setResultado(Integer resultado) {
        Resultado = resultado;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public void setVS(String VS) {
        this.VS = VS;
    }

    public Integer getResultado() {
        return Resultado;
    }

    public String getVS() {
        return VS;
    }

    public String getFecha() {
        return Fecha;
    }
}
