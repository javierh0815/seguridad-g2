package com.gamer.gamerapp.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(nullable = false, length = 30)
    private String modalidad; 

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(nullable = false, length = 100)
    private String videojuego;

    @Column(nullable = false, length = 80)
    private String fechaHora;

    @Column(nullable = false, length = 200)
    private String ubicacionOEnlace;

    @Column(nullable = false, length = 100)
    private String organizador;

    @Column(nullable = false, length = 300)
    private String requisitos;

    @Column(nullable = false, length = 300)
    private String inscripcion;

    @Column(nullable = false, length = 500)
    private String imagenPortada;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventoMedia> multimedia = new ArrayList<>();

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventoComentario> comentarios = new ArrayList<>();

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventoCompartido> compartidos = new ArrayList<>();


    public double getPromedioValoracion() {
        if (comentarios == null || comentarios.isEmpty()) {
            return 0.0;
        }
        return comentarios.stream()
                .mapToInt(EventoComentario::getValoracion)
                .average()
                .orElse(0.0);
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getVideojuego() { return videojuego; }
    public void setVideojuego(String videojuego) { this.videojuego = videojuego; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    public String getUbicacionOEnlace() { return ubicacionOEnlace; }
    public void setUbicacionOEnlace(String ubicacionOEnlace) { this.ubicacionOEnlace = ubicacionOEnlace; }

    public String getOrganizador() { return organizador; }
    public void setOrganizador(String organizador) { this.organizador = organizador; }

    public String getRequisitos() { return requisitos; }
    public void setRequisitos(String requisitos) { this.requisitos = requisitos; }

    public String getInscripcion() { return inscripcion; }
    public void setInscripcion(String inscripcion) { this.inscripcion = inscripcion; }

    public String getImagenPortada() { return imagenPortada; }
    public void setImagenPortada(String imagenPortada) { this.imagenPortada = imagenPortada; }

    public List<EventoMedia> getMultimedia() { return multimedia; }
    public void setMultimedia(List<EventoMedia> multimedia) { this.multimedia = multimedia; }

    public List<EventoComentario> getComentarios() { return comentarios; }
    public void setComentarios(List<EventoComentario> comentarios) { this.comentarios = comentarios; }

    public List<EventoCompartido> getCompartidos() { return compartidos; }
    public void setCompartidos(List<EventoCompartido> compartidos) { this.compartidos = compartidos; }
}