package com.curso.ejemplo.activities_fragments_intents.modelo;

import org.json.JSONException;
import org.json.JSONObject;

public class Carro {
    private String placa;
    private String modelo;
    private int estado; // 1: Activo, 2: Inactivo
    private int id;

    public Carro(String placa, String modelo, int estado) {
        this.placa = placa;
        this.modelo = modelo;
        this.estado = estado;
    }

    private Carro() {
    }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }

    public String getEstadoDescripcion() {
        switch (estado) {
            case 1: return "Activo";
            case 2: return "Inactivo";
            default: return "Desconocido";
        }
    }

    public static Carro fromJson(JSONObject obj) throws JSONException {
        Carro carro = new Carro();
        // Handle both "id" and "Id"
        carro.id = obj.has("id") ? obj.getInt("id") : obj.optInt("Id", 0);
        carro.placa = obj.has("placa") ? obj.getString("placa") : obj.optString("Placa", "");
        carro.modelo = obj.has("modelo") ? obj.getString("modelo") : obj.optString("Modelo", "");
        // Handle both "estado" and "Estado"
        carro.estado = obj.has("estado") ? obj.getInt("estado") : obj.optInt("Estado", 0);
        return carro;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        try {
            // Sending both common casing styles to ensure API compatibility
            obj.put("id", id);
            obj.put("Id", id);
            obj.put("placa", placa);
            obj.put("Placa", placa);
            obj.put("modelo", modelo);
            obj.put("Modelo", modelo);
            obj.put("estado", estado);
            obj.put("Estado", estado);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
