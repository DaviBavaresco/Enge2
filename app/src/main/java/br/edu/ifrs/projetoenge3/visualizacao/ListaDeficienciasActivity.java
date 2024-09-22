package br.edu.ifrs.projetoenge3.visualizacao;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.ifrs.projetoenge3.R;
import br.edu.ifrs.projetoenge3.adapter.DeficienciaAdapterExclui;
import br.edu.ifrs.projetoenge3.usuarios.Deficiencia;

public class ListaDeficienciasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DeficienciaAdapterExclui deficienciaAdapter;
    private List<Deficiencia> deficiencias;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_deficiencia_aluno);

        recyclerView = findViewById(R.id.recyclerViewDeficiencias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Obter a lista de deficiências passada pela intent
        deficiencias = getIntent().getParcelableArrayListExtra("deficiencias");

        // Configurar o adapter com a lista de deficiências
        deficienciaAdapter = new DeficienciaAdapterExclui(this,deficiencias);
        recyclerView.setAdapter(deficienciaAdapter);



    }
}