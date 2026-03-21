package com.curso.ejemplo.activities_fragments_intents.repositorio;

import com.curso.ejemplo.activities_fragments_intents.modelo.Carro;
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

public class CarroRepository {
    private static final String BASE_URL = "https://10.0.2.2:7119/api/ServicioDeAutos/";
    private final String apiKey = "123456";

    public void getCarrosAsync(Consumer<List<Carro>> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Carro> carros = new ArrayList<>();
            try {
                UnsafeHttps.allowAllSSL();
                URL url = new URL(BASE_URL + "ObtengaLaLista");
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

                JSONArray array = new JSONArray(response.toString());
                for (int i = 0; i < array.length(); i++) {
                    carros.add(Carro.fromJson(array.getJSONObject(i)));
                }
                new Handler(Looper.getMainLooper()).post(() -> callback.accept(carros));
            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() -> callback.accept(new ArrayList<>()));
            }
        });
    }

    public void addCarroAsync(Carro carro, Runnable onComplete) {
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

                OutputStream os = conn.getOutputStream();
                os.write(carro.toJson().toString().getBytes());
                os.flush();
                os.close();
                conn.getInputStream().close();
                new Handler(Looper.getMainLooper()).post(onComplete);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void editCarroAsync(String placa, Carro carroEditado, Runnable onComplete) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                UnsafeHttps.allowAllSSL();
                // For PUT operations in ASP.NET Core with strict routing, sometimes the ID is required in the path
                // However, based on the C# repository, it uses _context.Update(carro), which depends on the ID inside the object.
                // We'll also use the "?id=" parameter just in case the controller itself requires it for routing.
                URL url = new URL(BASE_URL + "EditeLaCarro?id=" + carroEditado.getId());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("X-API-KEY", apiKey);
                conn.setDoOutput(true);

                JSONObject json = carroEditado.toJson();
                
                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();
                os.close();
                
                int responseCode = conn.getResponseCode();
                if (responseCode >= 200 && responseCode <= 299) {
                    new Handler(Looper.getMainLooper()).post(onComplete);
                }
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Carro getCarroByPlaca(String placa) {
        try {
            UnsafeHttps.allowAllSSL();
            URL url = new URL(BASE_URL + "Obtenga?id=" + placa);
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
            return Carro.fromJson(new JSONObject(response.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
