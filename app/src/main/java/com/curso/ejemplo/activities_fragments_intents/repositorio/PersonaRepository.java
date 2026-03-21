package com.curso.ejemplo.activities_fragments_intents.repositorio;

import com.curso.ejemplo.activities_fragments_intents.modelo.Persona;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.os.Looper;

public class PersonaRepository {
    private static final String BASE_URL = "https://10.0.2.2:7119/api/ServicioDePersonas/";
    private String apiKey = "123456";
    public PersonaRepository() {

    }
    public void getPersonasAsync(Consumer<List<Persona>> callback) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Persona> personas = new ArrayList<>();
            try {
                UnsafeHttps.allowAllSSL();
                URL url = new URL(BASE_URL + "ObtengaLaLista");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("X-API-KEY", apiKey);
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONArray array = new JSONArray(response.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    personas.add(Persona.fromJson(obj));
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.accept(personas));


            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error al listar la personas", e);
            }
        });
    }


    public void addPersonaAsync(Persona persona, Runnable onComplete) {

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
            UnsafeHttps.allowAllSSL();
            URL url = new URL(BASE_URL + "Agregue");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-API-KEY", apiKey);
            conn.setDoOutput(true);

            String json = persona.toJson().toString();
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();
            conn.getInputStream().close();
            onComplete.run();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Error al agregar la persona", e);
                }
            });

    }

    public void editPersonaAsync(String identificacion, Persona personaEditada, Runnable onComplete) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
        try {

            UnsafeHttps.allowAllSSL();
            URL url = new URL(BASE_URL + "EditeLaPersona");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-API-KEY", apiKey);
            conn.setDoOutput(true);

            String json = personaEditada.toJson().toString();
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();

            conn.getInputStream().close();
            onComplete.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        });
    }
    public Persona getPersonaByIdentificacion(String identificacion) {
        try {

            UnsafeHttps.allowAllSSL();
            URL url = new URL(BASE_URL + "ObtengaLaPersona?id=" + identificacion);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-API-KEY", apiKey);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject obj = new JSONObject(response.toString());
            return Persona.fromJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
