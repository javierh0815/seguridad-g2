package com.gamer.gamerapp.dto;

public class ComentarioRequest {
    private String contenido;
    private Integer valoracion;

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public Integer getValoracion() { return valoracion; }
    public void setValoracion(Integer valoracion) { this.valoracion = valoracion; }
}