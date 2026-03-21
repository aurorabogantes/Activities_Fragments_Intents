package com.curso.ejemplo.activities_fragments_intents.vista.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.curso.ejemplo.activities_fragments_intents.R;
import com.curso.ejemplo.activities_fragments_intents.modelo.Carro;
import java.util.ArrayList;
import java.util.List;

public class CarroAdapter extends RecyclerView.Adapter<CarroAdapter.CarroViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Carro carro);
    }

    private final List<Carro> carros = new ArrayList<>();
    private final OnItemClickListener listener;

    public CarroAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Carro> nuevaLista) {
        carros.clear();
        if (nuevaLista != null) {
            carros.addAll(nuevaLista);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carro, parent, false);
        return new CarroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarroViewHolder holder, int position) {
        holder.bind(carros.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return carros.size();
    }

    public static class CarroViewHolder extends RecyclerView.ViewHolder {
        TextView placa, modelo, estado;

        public CarroViewHolder(View itemView) {
            super(itemView);
            placa = itemView.findViewById(R.id.text_placa);
            modelo = itemView.findViewById(R.id.text_modelo);
            estado = itemView.findViewById(R.id.text_estado);
        }

        public void bind(final Carro carro, final OnItemClickListener listener) {
            placa.setText(carro.getPlaca());
            modelo.setText(carro.getModelo());
            estado.setText(carro.getEstadoDescripcion());
            itemView.setOnClickListener(v -> listener.onItemClick(carro));
        }
    }
}
