package br.edu.ifrs.projetoenge3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DeficienciaAdapter extends RecyclerView.Adapter<DeficienciaAdapter.DeficienciaViewHolder> {

    private List<Deficiencia> deficienciaList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public DeficienciaAdapter(List<Deficiencia> deficienciaList) {
        this.deficienciaList = deficienciaList;
    }

    @NonNull
    @Override
    public DeficienciaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deficiencia, parent, false);
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

        // Quando o botão Aprovar é clicado, o status é atualizado para "validado"
        holder.buttonAprovar.setOnClickListener(v -> {
            db.collection("deficiencias").document(deficiencia.getDocumentId())
                    .update("status", "validado")
                    .addOnSuccessListener(aVoid -> {
                        deficienciaList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, deficienciaList.size());
                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                    });
        });

        // Quando o botão Negar é clicado, o status é atualizado para "negado"
        holder.buttonNegar.setOnClickListener(v -> {
            db.collection("deficiencias").document(deficiencia.getDocumentId())
                    .update("status", "negado")
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
        public Button buttonAprovar;
        public Button buttonNegar;

        public DeficienciaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMatricula = itemView.findViewById(R.id.textViewMatricula);
            textViewDeficiencia = itemView.findViewById(R.id.textViewDeficiencia);
            textViewExplica = itemView.findViewById(R.id.textViewExplica);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            buttonAprovar = itemView.findViewById(R.id.buttonAprovar);
            buttonNegar = itemView.findViewById(R.id.buttonNegar);
        }
    }
}
