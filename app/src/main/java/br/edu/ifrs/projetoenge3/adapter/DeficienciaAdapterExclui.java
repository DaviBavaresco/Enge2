package br.edu.ifrs.projetoenge3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import br.edu.ifrs.projetoenge3.R;
import br.edu.ifrs.projetoenge3.usuarios.Deficiencia;

public class DeficienciaAdapterExclui extends RecyclerView.Adapter<DeficienciaAdapterExclui.DeficienciaViewHolder> {

     List<Deficiencia> deficienciaList;
    Deficiencia deficiencia;

    Context context;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public DeficienciaAdapterExclui(Context context,List<Deficiencia> deficienciaList) {
        this.deficienciaList = deficienciaList;
        this.context = context;
    }

    @NonNull
    @Override
    public DeficienciaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deficiencia_negado_aluno, parent, false);
        return new DeficienciaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeficienciaViewHolder holder, int position) {
         deficiencia = deficienciaList.get(position);

        holder.textViewMatricula.setText("Matrícula: " + deficiencia.getMatricula());
        holder.textViewDeficiencia.setText("Deficiência: " + deficiencia.getDeficiencia());
        holder.textViewExplica.setText("explicacao: " + deficiencia.getExplica());
        holder.textViewStatus.setText("Status: " + deficiencia.getStatus());

        if (deficiencia.getDocumentId() == null) {
            holder.textViewMatricula.setText("Erro: documentId não encontrado");
            return;
        }

        // Quando o botão excluir é clicado
        holder.buttonExclu.setOnClickListener(v -> {
            db.collection("deficiencias").document(deficiencia.getDocumentId()).delete()
                    .addOnSuccessListener(aVoid -> {
                        deficienciaList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, deficienciaList.size());
                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                    });
        });

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
        public Button buttonExclu;

        public DeficienciaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMatricula = itemView.findViewById(R.id.textViewMatricula);
            textViewDeficiencia = itemView.findViewById(R.id.textViewDeficiencia);
            textViewExplica = itemView.findViewById(R.id.textViewExplica);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            buttonExclu = itemView.findViewById(R.id.buttonExclu);
        }
    }
}
