package com.curso.ejemplo.activities_fragments_intents.modelo;

import java.time.LocalDate;
import java.util.Date;

public class Persona {
    private String nombre;
    private int edad;
    private Date fechaNacimiento;
    private int estadoCivil; // 1: soltero, 2: casado
    private String identificacion;
    public Persona(String identificacion, String nombre, int edad, Date fechaNacimiento, int estadoCivil) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.edad = edad;
        this.fechaNacimiento = fechaNacimiento;
        this.estadoCivil = estadoCivil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(int estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getEstadoCivilDescripcion() {
        switch (estadoCivil) {
            case 1:
                return "Soltero";
            case 2:
                return "Casado";
            default:
                return "Desconocido";
        }
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }
}
