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

        RecyclerView recyclerView = view.findViewById(R.id.recycler_personas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PersonaAdapter(persona -> mostrarMenuOpciones(persona));
        recyclerView.setAdapter(adapter);

        personaViewModel = new ViewModelProvider(this).get(PersonaViewModel.class);
        personaViewModel.getPersonas().observe(getViewLifecycleOwner(), personas -> {
            adapter.submitList(personas);
        });

        // Configurar el FloatingActionButton para agregar una nueva persona
        FloatingActionButton fabAgregar = view.findViewById(R.id.fab_agregar);
        fabAgregar.setOnClickListener(v -> mostrarDialogoPersona(null, false));
    }

    private void mostrarMenuOpciones(Persona persona) {
        String[] opciones = {"Editar", "Ver"};
        new AlertDialog.Builder(requireContext())
                .setTitle("Seleccione una opción")
                .setItems(opciones, (dialog, which) -> {
                    if (which == 0) {
                        mostrarDialogoPersona(persona, false);
                    } else {
                        mostrarDialogoPersona(persona, true);
                    }
                })
                .show();
    }

    private void mostrarDialogoPersona(@Nullable Persona personaExistente, boolean isReadOnly) {
        // Usa AlertDialog para simplificar el ejemplo
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_persona, null);

        EditText etNombre = dialogView.findViewById(R.id.et_nombre);
        EditText etIdentificacion = dialogView.findViewById(R.id.et_identificacion);
        EditText et_apellidos = dialogView.findViewById(R.id.et_apellidos);
        EditText etFechaNacimiento = dialogView.findViewById(R.id.et_fecha_nacimiento);
        // Configurar el Spinner para el estado
        Spinner spEstado = dialogView.findViewById(R.id.sp_estado);
        String[] estados = {"Activo", "Inactivo"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, estados);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEstado.setAdapter(adapterSpinner);
        Calendar calendar = Calendar.getInstance();
        // Si personaExistente no es nulo, cargar los datos de la persona
        if (personaExistente != null) {
            etNombre.setText(personaExistente.getNombre());
            etIdentificacion.setText(personaExistente.getIdentificacion());
            etIdentificacion.setEnabled(false); // No permitir editar la identificación
            et_apellidos.setText(String.valueOf(personaExistente.getApellidos()));
            etFechaNacimiento.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(personaExistente.getFechaNacimiento()));
            calendar.setTime(personaExistente.getFechaNacimiento());
            
            int selection = personaExistente.getEstado() - 1;
            if (selection >= 0 && selection < estados.length) {
                spEstado.setSelection(selection);
            }
        } else {
            calendar.setTime(new Date());
        }

        if (isReadOnly) {
            etNombre.setEnabled(false);
            etIdentificacion.setEnabled(false);
            et_apellidos.setEnabled(false);
            etFechaNacimiento.setEnabled(false);
            spEstado.setEnabled(false);
        }

        // Configurar el DatePicker para la fecha de nacimiento
        etFechaNacimiento.setOnClickListener(v -> {
            if (isReadOnly) return;
            new DatePickerDialog(getContext(),
                    (view1, year, month, dayOfMonth) -> {
                        String fecha = String.format("%02d", dayOfMonth) + "-" + String.format("%02d", month + 1) + "-" + year;
                        etFechaNacimiento.setText(fecha);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        String title = personaExistente == null ? "Agregar Persona" : (isReadOnly ? "Ver Persona" : "Editar Persona");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setView(dialogView);

        if (!isReadOnly) {
            builder.setPositiveButton("Guardar", (dialog, which) -> {
                String nombre = etNombre.getText().toString();
                String identificacion = etIdentificacion.getText().toString();
                String apellidos = et_apellidos.getText().toString();
                int estado = spEstado.getSelectedItemPosition() + 1;

                String fechaStr = etFechaNacimiento.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                Date fechaNacimiento = null;
                try {
                    fechaNacimiento = sdf.parse(fechaStr);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                Persona persona = new Persona(identificacion, nombre, apellidos, fechaNacimiento, estado);

                if (personaExistente == null) {
                    personaViewModel.addPersona(persona);
                } else {
                    persona.setId(personaExistente.getId());
                    personaViewModel.editPersona(identificacion, persona);
                }
            });
        }
        builder.setNegativeButton(isReadOnly ? "Cerrar" : "Cancelar", null)
                .show();
    }
}
