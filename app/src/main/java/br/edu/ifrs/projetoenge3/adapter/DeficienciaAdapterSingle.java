package br.edu.ifrs.projetoenge3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifrs.projetoenge3.usuarios.Deficiencia;
import br.edu.ifrs.projetoenge3.R;

public class DeficienciaAdapterSingle extends RecyclerView.Adapter<DeficienciaAdapterSingle.DeficienciaViewHolder>  {

     List<Deficiencia> deficienciaList;
    public List<Deficiencia> deficienciaListFull;

    Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public DeficienciaAdapterSingle(Context context,List<Deficiencia> deficienciaList) {
        this.context = context;
        this.deficienciaList = deficienciaList;
        this.deficienciaListFull = new ArrayList<>(deficienciaList);
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

        //altera a cor do campo para a pessoa compreender o estado do chamado mais rapido
        if (deficiencia.getStatus().toString().equals("negado")) {
            holder.textViewStatus.setTextColor(context.getResources().getColor(R.color.red));
        } else if (deficiencia.getStatus().toString().equals("validado")) {
            holder.textViewStatus.setTextColor(context.getResources().getColor(R.color.green));
        } else if (deficiencia.getStatus().toString().equals("pendente")) {
            holder.textViewStatus.setTextColor(context.getResources().getColor(R.color.yellow));
        }

        if (deficiencia.getDocumentId() == null) {
            holder.textViewMatricula.setText("Erro: documentId não encontrado");
        }



    }
    // Metodo para filtrar a lista por matrícula
    public void filterByMatricula(String matricula) {
        if (matricula.isEmpty()) {
            deficienciaList.clear();
            deficienciaList.addAll(deficienciaListFull);
        } else {
            List<Deficiencia> filteredList = new ArrayList<>();
            for (Deficiencia deficiencia : deficienciaListFull) {
                if (deficiencia.getMatricula().toLowerCase().contains(matricula.toLowerCase())) {
                    filteredList.add(deficiencia);
                }
            }
            deficienciaList.clear();
            deficienciaList.addAll(filteredList);
        }
        notifyDataSetChanged(); // Atualizar a RecyclerView com a lista filtrada
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
    }

