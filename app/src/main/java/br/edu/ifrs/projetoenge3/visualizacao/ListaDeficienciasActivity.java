package br.edu.ifrs.projetoenge3.visualizacao;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifrs.projetoenge3.R;
import br.edu.ifrs.projetoenge3.adapter.DeficienciaAdapterExclui;
import br.edu.ifrs.projetoenge3.usuarios.Deficiencia;

public class ListaDeficienciasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DeficienciaAdapterExclui deficienciaAdapter;
    private List<Deficiencia> deficiencias;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private String userId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_deficiencia_aluno);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.recyclerViewDeficiencias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Obter a lista de deficiências passada pela intent
        deficiencias = new ArrayList<>();

        // Configurar o adapter com a lista de deficiências
        deficienciaAdapter = new DeficienciaAdapterExclui(this,deficiencias);
        recyclerView.setAdapter(deficienciaAdapter);

        listarDeficienciasDoAluno();

    }

    private void listarDeficienciasDoAluno() {

        db.collection("deficiencias")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Deficiencia deficiencia = documentSnapshot.toObject(Deficiencia.class);
                        deficiencia.setDocumentId(documentSnapshot.getId());  // Adicionar o documentId
                        deficiencias.add(deficiencia);
                    }
                    deficienciaAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ListaDeficienciasActivity.this, "Erro ao buscar deficiências: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}