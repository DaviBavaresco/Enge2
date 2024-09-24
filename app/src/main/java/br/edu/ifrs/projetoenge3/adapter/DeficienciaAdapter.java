package br.edu.ifrs.projetoenge3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifrs.projetoenge3.usuarios.Deficiencia;
import br.edu.ifrs.projetoenge3.R;

public class DeficienciaAdapter extends RecyclerView.Adapter<DeficienciaAdapter.DeficienciaViewHolder> {

     List<Deficiencia> deficienciaList;
    public List<Deficiencia> deficienciaListFull;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    Context context;

    public DeficienciaAdapter(Context context, List<Deficiencia> deficienciaList) {
        this.deficienciaList = deficienciaList;
        this.deficienciaListFull = new ArrayList<>(deficienciaList);
        this.context = context;
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
        holder.textViewExplica.setText("Explicacao: " + deficiencia.getExplica());
        holder.textViewStatus.setText("Status: " + deficiencia.getStatus());


        // Quando o botao Aprovar e clicado o status e atualizado para "validado"
        holder.buttonAprovar.setOnClickListener(v -> {
            db.collection("deficiencias").document(deficiencia.getDocumentId())
                    .update("status", "validado")
                    .addOnSuccessListener(aVoid -> {
                        deficienciaList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, deficienciaList.size());
                        Toast.makeText(context, "Deficiencia validada com sucesso", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                    });
        });

        // Quando o botao Negar e clicado o status e atualizado para "negado"
        holder.buttonNegar.setOnClickListener(v -> {
            db.collection("deficiencias").document(deficiencia.getDocumentId())
                    .update("status", "negado")
                    .addOnSuccessListener(aVoid -> {
                        deficienciaList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, deficienciaList.size());
                        Toast.makeText(context, "Deficiencia negada com sucesso", Toast.LENGTH_SHORT).show();
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
