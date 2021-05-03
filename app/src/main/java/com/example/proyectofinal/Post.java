package com.example.proyectofinal;

public class Post {
    private String nombre;
    private String descripcion;

    public Post(String nombre, String descripcion){
        this.nombre=nombre;
        this.descripcion=descripcion;
    }

    public String getNombre(){
        return nombre;
    }

    public String getDescripcion(){
        return descripcion;
    }
}
