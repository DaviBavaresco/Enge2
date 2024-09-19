package br.edu.ifrs.projetoenge3;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.security.AccessController;
import java.util.List;

public class ListaDeficienciasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DeficienciaAdapterSingle deficienciaAdapter;
    private List<Deficiencia> deficiencias;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_deficiencias);

        recyclerView = findViewById(R.id.recyclerViewDeficiencias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SearchView searchView = findViewById(R.id.searchViewList);
        // Obter a lista de deficiências passada pela intent
        deficiencias = getIntent().getParcelableArrayListExtra("deficiencias");
        searchView.clearFocus();
        searchView.setActivated(true);

        // Configurar o adapter com a lista de deficiências

        deficienciaAdapter = new DeficienciaAdapterSingle(this.getApplicationContext(),deficiencias);
        recyclerView.setAdapter(deficienciaAdapter);

        //nao esta funcionando
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String filtro) {
                deficienciaAdapter.getFilter().filter(filtro);
                return true;
            }
        });
    }
}