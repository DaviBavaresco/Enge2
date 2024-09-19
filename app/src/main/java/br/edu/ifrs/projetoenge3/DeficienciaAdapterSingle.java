package br.edu.ifrs.projetoenge3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DeficienciaAdapterSingle extends RecyclerView.Adapter<DeficienciaAdapterSingle.DeficienciaViewHolder>  implements Filterable {

   List<Deficiencia> deficienciaList;
    private List<Deficiencia> listaValoresFiltrados;
    private ValueFilter valorFiltrado;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context;
    public DeficienciaAdapterSingle(Context context,List<Deficiencia> deficienciaList) {
        this.context = context;
        this.deficienciaList = deficienciaList;
        this.listaValoresFiltrados = deficienciaList;
    }

    @NonNull
    @Override
    public DeficienciaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deficiencia_visualizar, parent, false);
        return new DeficienciaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeficienciaViewHolder holder, int position) {
        Deficiencia deficiencia = deficienciaList.get(position);

        holder.textViewMatricula.setText("Matrícula: " + deficiencia.getMatricula());
        holder.textViewDeficiencia.setText("Deficiência: " + deficiencia.getDeficiencia());
        holder.textViewExplica.setText("explicacao: " + deficiencia.getExplica());
        holder.textViewStatus.setText("Status: " + deficiencia.getStatus());

        if (deficiencia.getDocumentId() == null) {
            holder.textViewMatricula.setText("Erro: documentId não encontrado");
            return;
        }


;
    }


    @Override
    public int getItemCount() {
        return deficienciaList.size();
    }

    // Classe ViewHolder
    public static class DeficienciaViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewMatricula;
        public TextView textViewDeficiencia;
        public TextView textViewExplica;
        public TextView textViewStatus;

        public DeficienciaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMatricula = itemView.findViewById(R.id.textViewMatricula);
            textViewDeficiencia = itemView.findViewById(R.id.textViewDeficiencia);
            textViewExplica = itemView.findViewById(R.id.textViewExplica);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
        }
    }

    @Override
    public Filter getFilter() {
        try {
            if (valorFiltrado == null) {
                valorFiltrado = new ValueFilter();
            }
        }
        catch (NullPointerException e){
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        return valorFiltrado;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                List<Deficiencia> filterList = new ArrayList<>();
                for (int i = 0; i < listaValoresFiltrados.size(); i++) {
                    if ((listaValoresFiltrados.get(i).getMatricula().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(listaValoresFiltrados.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = listaValoresFiltrados.size();
                results.values = listaValoresFiltrados;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            deficienciaList = (List<Deficiencia>) results.values;
            notifyDataSetChanged();
        }

    }
}
