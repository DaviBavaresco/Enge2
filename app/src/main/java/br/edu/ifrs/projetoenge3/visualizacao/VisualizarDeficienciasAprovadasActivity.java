package br.edu.ifrs.projetoenge3.visualizacao;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import br.edu.ifrs.projetoenge3.R;
import br.edu.ifrs.projetoenge3.adapter.DeficienciaAdapterSingle;
import br.edu.ifrs.projetoenge3.usuarios.Deficiencia;

public class VisualizarDeficienciasAprovadasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DeficienciaAdapterSingle adapter;
    private List<Deficiencia> deficienciaList;
    private FirebaseFirestore db;
    private SearchView searchViewMatricula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_deficiencias);

        recyclerView = findViewById(R.id.recyclerViewDeficiencias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchViewMatricula = findViewById(R.id.searchViewMatricula);

        deficienciaList = new ArrayList<>();
        adapter = new DeficienciaAdapterSingle(this,deficienciaList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Buscar deficiências com status "pendente"
        buscarDeficienciasAprovadas();

        //pesquisa por matricula
        searchViewMatricula.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filterByMatricula(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filtrar em tempo real conforme o usuário digita
                adapter.filterByMatricula(newText);
                return false;
            }
        });
    }




    private void buscarDeficienciasAprovadas() {
        db.collection("deficiencias")
                .whereEqualTo("status", "validado")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    deficienciaList.clear();  // Limpar a lista antes de adicionar os dados
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Deficiencia deficiencia = document.toObject(Deficiencia.class);
                        deficienciaList.add(deficiencia);
                    }
                    adapter.notifyDataSetChanged();  // Atualizar o RecyclerView com os novos dados

                    // Atualizar a lista completa usada para o filtro
                    adapter.deficienciaListFull.clear();
                    adapter.deficienciaListFull.addAll(deficienciaList);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Erro ao buscar deficiências", e);
                    Toast.makeText(VisualizarDeficienciasAprovadasActivity.this, "Erro ao carregar dados.", Toast.LENGTH_SHORT).show();
                });
    }


}
