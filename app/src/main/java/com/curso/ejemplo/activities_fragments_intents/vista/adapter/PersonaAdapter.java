package com.curso.ejemplo.activities_fragments_intents.vista.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.curso.ejemplo.activities_fragments_intents.R;
import com.curso.ejemplo.activities_fragments_intents.modelo.Persona;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PersonaAdapter extends  RecyclerView.Adapter<PersonaAdapter.PersonaViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Persona persona);
    }
    private final List<Persona> personas = new ArrayList<>();
    private final OnItemClickListener listener;

    public PersonaAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Persona> nuevaLista) {
        personas.clear();
        if (nuevaLista != null) {
            personas.addAll(nuevaLista);
        }
        notifyDataSetChanged();
    }

    @Override
    public PersonaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_persona, parent, false);
        return new PersonaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonaViewHolder holder, int position) {
        // Aquí puedes vincular los datos de la persona al ViewHolder
        holder.bind(personas.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return personas.size();
    }

    public static class PersonaViewHolder extends RecyclerView.ViewHolder {
        // Aquí puedes definir las vistas del item de persona
        TextView nombre, identificacion, edad, fechaNacimiento, estadoCivil;
        public PersonaViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.text_nombre);
            identificacion = itemView.findViewById(R.id.text_identificacion);
            edad = itemView.findViewById(R.id.text_edad);
            fechaNacimiento = itemView.findViewById(R.id.text_fecha_nacimiento);
            estadoCivil = itemView.findViewById(R.id.text_estado_civil);
        }

        public void bind(final Persona persona, final OnItemClickListener listener) {
            nombre.setText(persona.getNombre());
            identificacion.setText(persona.getIdentificacion());
            edad.setText(String.valueOf(persona.getEdad()));
            estadoCivil.setText(persona.getEstadoCivilDescripcion());
            // Formatea LocalDate a String
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

            String fechaFormateada = persona.getFechaNacimiento() != null
                    ? sdf.format(persona.getFechaNacimiento())
                    : "";
            fechaNacimiento.setText(fechaFormateada);
            // Configura el listener para el clic en el item
            itemView.setOnClickListener(v -> listener.onItemClick(persona));
        }
    }

}
