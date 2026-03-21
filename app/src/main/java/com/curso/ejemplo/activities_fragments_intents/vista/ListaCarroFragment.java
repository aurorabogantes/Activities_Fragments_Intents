package com.curso.ejemplo.activities_fragments_intents.vista;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.curso.ejemplo.activities_fragments_intents.R;
import com.curso.ejemplo.activities_fragments_intents.modelo.Carro;
import com.curso.ejemplo.activities_fragments_intents.viewmodel.CarroViewModel;
import com.curso.ejemplo.activities_fragments_intents.vista.adapter.CarroAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListaCarroFragment extends Fragment {
    private CarroAdapter adapter;
    private CarroViewModel carroViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lista_carro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_carros);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CarroAdapter(this::mostrarDialogoCarro);
        recyclerView.setAdapter(adapter);

        carroViewModel = new ViewModelProvider(this).get(CarroViewModel.class);
        carroViewModel.getCarros().observe(getViewLifecycleOwner(), adapter::submitList);

        FloatingActionButton fabAgregar = view.findViewById(R.id.fab_agregar);
        fabAgregar.setOnClickListener(v -> mostrarDialogoCarro(null));
    }

    private void mostrarDialogoCarro(@Nullable Carro carroExistente) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_carro, null);

        EditText etPlaca = dialogView.findViewById(R.id.et_placa);
        EditText etModelo = dialogView.findViewById(R.id.et_modelo);
        Spinner spEstado = dialogView.findViewById(R.id.sp_estado);

        String[] estados = {"Activo", "Inactivo"};
        if (getContext() != null) {
            ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, estados);
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spEstado.setAdapter(adapterSpinner);
        }

        if (carroExistente != null) {
            etPlaca.setText(carroExistente.getPlaca());
            etPlaca.setEnabled(false);
            etModelo.setText(carroExistente.getModelo());
            // Set selection based on 1-based index (1: Activo, 2: Inactivo)
            int selection = carroExistente.getEstado() - 1;
            if (selection >= 0 && selection < estados.length) {
                spEstado.setSelection(selection);
            }
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(carroExistente == null ? "Agregar Carro" : "Editar Carro")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String placa = etPlaca.getText().toString();
                    String modelo = etModelo.getText().toString();
                    int estado = spEstado.getSelectedItemPosition() + 1;

                    Carro carro = new Carro(placa, modelo, estado);

                    if (carroExistente == null) {
                        carroViewModel.addCarro(carro);
                    } else {
                        // Crucial: preserve the ID for the update to work
                        carro.setId(carroExistente.getId());
                        carroViewModel.editCarro(carroExistente.getPlaca(), carro);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}