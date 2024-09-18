package br.edu.ifrs.projetoenge3;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class VisualizarTodasDeficienciasEnviadasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DeficienciaAdapter adapter;
    private List<Deficiencia> deficienciaList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_deficiencias);

        recyclerView = findViewById(R.id.recyclerViewDeficiencias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        deficienciaList = new ArrayList<>();
        adapter = new DeficienciaAdapter(deficienciaList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Buscar deficiências com status "pendente"
        buscarDeficiencias();
    }

    private void buscarDeficiencias() {
        db.collection("deficiencias")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    deficienciaList.clear();  // Limpar a lista antes de adicionar os dados
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Deficiencia deficiencia = document.toObject(Deficiencia.class);
                        deficienciaList.add(deficiencia);
                    }
                    adapter.notifyDataSetChanged();  // Atualizar o RecyclerView com os novos dados
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Erro ao buscar deficiências", e);
                    Toast.makeText(VisualizarTodasDeficienciasEnviadasActivity.this, "Erro ao carregar dados.", Toast.LENGTH_SHORT).show();
                });
    }
}
