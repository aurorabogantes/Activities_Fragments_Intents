package com.curso.ejemplo.activities_fragments_intents.vista;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.curso.ejemplo.activities_fragments_intents.R;
import com.curso.ejemplo.activities_fragments_intents.modelo.Persona;
import com.curso.ejemplo.activities_fragments_intents.viewmodel.PersonaViewModel;
import com.curso.ejemplo.activities_fragments_intents.vista.adapter.PersonaAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ListaPersonaFragment extends Fragment {
    private PersonaAdapter adapter;
    private PersonaViewModel personaViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.lista_persona, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize views and set up listeners here
        RecyclerView recyclerView = view.findViewById(R.id.recycler_personas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PersonaAdapter(persona -> mostrarDialogoPersona(persona));
        recyclerView.setAdapter(adapter);

        personaViewModel = new ViewModelProvider(this).get(PersonaViewModel.class);
        personaViewModel.getPersonas().observe(getViewLifecycleOwner(), personas -> {
            adapter.submitList(personas);
        });

        // Configurar el FloatingActionButton para agregar una nueva persona
        FloatingActionButton fabAgregar = view.findViewById(R.id.fab_agregar);
        fabAgregar.setOnClickListener(v -> mostrarDialogoPersona(null));
    }

    private void mostrarDialogoPersona(@Nullable Persona personaExistente) {
        // Usa AlertDialog para simplificar el ejemplo
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_persona, null);

        EditText etNombre = dialogView.findViewById(R.id.et_nombre);
        EditText etIdentificacion = dialogView.findViewById(R.id.et_identificacion);
        EditText etEdad = dialogView.findViewById(R.id.et_edad);
        EditText etFechaNacimiento = dialogView.findViewById(R.id.et_fecha_nacimiento);
        // Configurar el Spinner para el estado civil
        Spinner spEstadoCivil = dialogView.findViewById(R.id.sp_estado_civil);
        String[] estadosCiviles = {"Soltero", "Casado"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, estadosCiviles);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEstadoCivil.setAdapter(adapterSpinner);
        Calendar calendar = Calendar.getInstance();
        // Si personaExistente no es nulo, cargar los datos de la persona
        if (personaExistente != null) {
            etNombre.setText(personaExistente.getNombre());
            etIdentificacion.setText(personaExistente.getIdentificacion());
            etIdentificacion.setEnabled(false); // No permitir editar la identificación
            etEdad.setText(String.valueOf(personaExistente.getEdad()));
            etFechaNacimiento.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(personaExistente.getFechaNacimiento()));
            calendar.setTime(personaExistente.getFechaNacimiento());
        }else {
          calendar.setTime(new Date());
        }

        // Configurar el DatePicker para la fecha de nacimiento
        etFechaNacimiento.setOnClickListener(v -> {

            new DatePickerDialog(getContext(),
                    (view1, year, month, dayOfMonth) -> {
                          String fecha = String.format("%02d", dayOfMonth)  + "-" + String.format("%02d", month + 1) + "-" +year;
                        etFechaNacimiento.setText(fecha);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        new AlertDialog.Builder(getContext())
                .setTitle(personaExistente == null ? "Agregar Persona" : "Editar Persona")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString();
                    String identificacion = etIdentificacion.getText().toString();
                    int edad = Integer.parseInt(etEdad.getText().toString());
                    int estadoCivil =  spEstadoCivil.getSelectedItemPosition() +1;

                    String fechaStr = etFechaNacimiento.getText().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    Date fechaNacimiento = null;
                    try {
                        fechaNacimiento = sdf.parse(fechaStr);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }                    Persona nuevaPersona = new Persona(identificacion,nombre, edad, fechaNacimiento, estadoCivil );

                    if (personaExistente == null) {
                        personaViewModel.addPersona(nuevaPersona);
                    } else {
                        personaViewModel.editPersona(identificacion, nuevaPersona);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
