package com.curso.ejemplo.activities_fragments_intents.modelo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Persona {
    private String nombre;
    private Date fechaNacimiento;
    private int estado; // 1: Activo, 2: inactivo
    private String identificacion;

    private String apellidos;
    private int id;


    public Persona(String identificacion, String nombre, String apellidos, Date fechaNacimiento, int estado) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.estado = estado;
        this.id = id;
    }

    private Persona() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return  id;
    }
    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estadoCivil) {
        this.estado = estadoCivil;
    }

    public String getEstadoDescripcion() {
        switch (estado) {
            case 1:
                return "Activo";
            case 2:
                return "Inactivo";
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

    public static Persona fromJson(JSONObject obj) throws JSONException {
        Persona persona = new Persona();
        persona.id = obj.optInt("id", 0);
        persona.identificacion = obj.optString("identificacion", "");
        persona.nombre = obj.optString("nombre", "");
        persona.apellidos = obj.optString("apellidos", "");
        String fechaStr = obj.optString("fechaDeNacimiento", null);
        if (fechaStr != null) {
            try {
                persona.fechaNacimiento = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(fechaStr);
            } catch (ParseException e) {
                persona.fechaNacimiento = null;
            }
        }
        persona.estado = obj.optInt("estado", 0);
        return persona;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", id);
            obj.put("identificacion", identificacion);
            obj.put("nombre", nombre);
            obj.put("apellidos", apellidos);
            if (fechaNacimiento != null) {
                String fechaStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(fechaNacimiento);
                obj.put("fechaDeNacimiento", fechaStr);
            }
            obj.put("estado", estado);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }


}
