package com.curso.ejemplo.activities_fragments_intents.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.curso.ejemplo.activities_fragments_intents.modelo.Carro; // IMPORTANTE
import com.curso.ejemplo.activities_fragments_intents.repositorio.CarroRepository; // IMPORTANTE
import java.util.List;

public class CarroViewModel extends ViewModel {
    private final CarroRepository repositorio;
    private final MutableLiveData<List<Carro>> carros = new MutableLiveData<>();

    public CarroViewModel() {
        repositorio = new CarroRepository();
        CargarCarros();
    }

    private void CargarCarros() {
        repositorio.getCarrosAsync(carros::setValue);
    }

    public LiveData<List<Carro>> getCarros() {
        return carros;
    }

    public void addCarro(Carro carro) {
        repositorio.addCarroAsync(carro, this::CargarCarros);
    }

    public void editCarro(String placa, Carro carroEditado) {
        repositorio.editCarroAsync(placa, carroEditado, this::CargarCarros);
    }

    public Carro getCarroByPlaca(String placa) {
        return repositorio.getCarroByPlaca(placa);
    }
}