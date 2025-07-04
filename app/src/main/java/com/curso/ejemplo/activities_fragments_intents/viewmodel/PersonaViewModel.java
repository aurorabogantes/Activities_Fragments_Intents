package com.curso.ejemplo.activities_fragments_intents.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.curso.ejemplo.activities_fragments_intents.modelo.Persona;
import com.curso.ejemplo.activities_fragments_intents.repositorio.PersonaRepository;

import java.util.List;

public class PersonaViewModel extends ViewModel {
    // Aquí puedes agregar lógica para manejar el estado de la UI relacionado con las personas
    // Por ejemplo, podrías cargar datos desde un repositorio o manejar eventos de la UI
    private final PersonaRepository repositorio = new PersonaRepository();
    private final MutableLiveData<List<Persona>> personas = new MutableLiveData<>();

    public PersonaViewModel() {
        // Constructor donde puedes inicializar datos o realizar configuraciones iniciales
        personas.setValue(repositorio.getPersonas());
    }

    // Métodos para interactuar con los datos de las personas pueden ser añadidos aquí
    public LiveData<List<Persona>> getPersonas() {
        return personas;
    }
    public void addPersona(Persona persona) {
        repositorio.addPersona(persona);
        personas.setValue(repositorio.getPersonas());
    }
    public void editPersona(String identificacion, Persona personaEditada) {
        repositorio.editPersona(identificacion, personaEditada);
        personas.setValue(repositorio.getPersonas());
    }
    public Persona getPersonaByIdentificacion(String identificacion) {
        return repositorio.getPersonaByIdentificacion(identificacion);
    }
}
