package com.curso.ejemplo.activities_fragments_intents.repositorio;

import android.app.Person;

import com.curso.ejemplo.activities_fragments_intents.modelo.Persona;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PersonaRepository {
    private  List<Persona> personas = new ArrayList<>();

    public PersonaRepository() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            personas.add(new Persona("01-1234-5678", "Juan Pérez", 30, sdf.parse("01-01-1994"), 1));
            personas.add(new Persona("02-2345-6789", "Ana Gómez", 25, sdf.parse("12-05-1999"), 2));
            personas.add(new Persona("03-3456-7890", "Luis Torres", 40, sdf.parse("20-03-1984"), 1));
            personas.add(new Persona("04-4567-8901", "María López", 35, sdf.parse("15-07-1989"), 2));
            personas.add(new Persona("05-5678-9012", "Carlos Ruiz", 28, sdf.parse("30-11-1996"), 1));
            personas.add(new Persona("06-6789-0123", "Sofía Martínez", 22, sdf.parse("10-02-2002"), 2));
            personas.add(new Persona("07-7890-1234", "Pedro Sánchez", 33, sdf.parse("25-08-1991"), 1));
            personas.add(new Persona("01-8901-2345", "Lucía Fernández", 27, sdf.parse("18-04-1997"), 2));
            personas.add(new Persona("02-9012-3456", "Miguel Ramírez", 45, sdf.parse("05-12-1979"), 1));
            personas.add(new Persona("03-0123-4567", "Elena Castro", 31, sdf.parse("22-09-1993"), 2));
            personas.add(new Persona("04-1234-5678", "Javier Morales", 29, sdf.parse("14-06-1995"), 1));
            personas.add(new Persona("05-2345-6789", "Patricia Herrera", 38, sdf.parse("30-03-1986"), 2));
            personas.add(new Persona("06-3456-7890", "Andrés Vargas", 26, sdf.parse("11-11-1998"), 1));
            personas.add(new Persona("07-4567-8901", "Gabriela Ríos", 24, sdf.parse("07-07-2000"), 2));
            personas.add(new Persona("01-5678-9012", "Fernando Salas", 36, sdf.parse("19-05-1988"), 1));
            personas.add(new Persona("02-6789-0123", "Valeria Paredes", 32, sdf.parse("03-10-1992"), 2));
            personas.add(new Persona("03-7890-1234", "Ricardo Molina", 41, sdf.parse("27-01-1983"), 1));
            personas.add(new Persona("04-8901-2345", "Natalia Guzmán", 23, sdf.parse("16-12-2001"), 2));
            personas.add(new Persona("05-9012-3456", "Héctor Peña", 34, sdf.parse("08-08-1990"), 1));
            personas.add(new Persona("06-0123-4567", "Camila Soto", 28, sdf.parse("21-02-1996"), 2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public List<Persona> getPersonas() {
        return new ArrayList<>(personas);
    }

    public void addPersona(Persona persona) {
        personas.add(persona);
    }

    public void editPersona(String identificacion, Persona personaEditada) {
        for (int i = 0; i < personas.size(); i++) {
            if (personas.get(i).getIdentificacion().equals(identificacion)) {
                personas.set(i, personaEditada);
                break;
            }
        }
    }
    public Persona getPersonaByIdentificacion(String identificacion) {
        for (Persona persona : personas) {
            if (persona.getIdentificacion().equals(identificacion)) {
                return persona;
            }
        }
        return null; // O lanzar una excepción si no se encuentra
    }
}
