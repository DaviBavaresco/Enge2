package br.edu.ifrs.projetoenge3.usuarios;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import br.edu.ifrs.projetoenge3.R;
import br.edu.ifrs.projetoenge3.insercao.RegisterActivity;
import br.edu.ifrs.projetoenge3.visualizacao.VisualizarDeficienciasAprovadasActivity;
import br.edu.ifrs.projetoenge3.visualizacao.VisualizarDeficienciasNegadasActivity;
import br.edu.ifrs.projetoenge3.visualizacao.VisualizarDeficienciasPendentesActivity;
import br.edu.ifrs.projetoenge3.visualizacao.VisualizarTodasDeficienciasEnviadasActivity;

public class SinapActivity extends AppCompatActivity {

    private Button btnVisualizaPendentes;
    private Button btnVisualizaValidadas;
    private Button btnVisualizaNegadas;
    private Button btnVisualizaTodas;
    private Button btnCadastrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sinap);

        btnVisualizaPendentes = findViewById(R.id.button);
        btnVisualizaValidadas = findViewById(R.id.button2);
        btnVisualizaNegadas = findViewById(R.id.button3);
        btnVisualizaTodas = findViewById(R.id.button4);
        btnCadastrar = findViewById(R.id.btn);

        btnVisualizaPendentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SinapActivity.this, VisualizarDeficienciasPendentesActivity.class);
                startActivity(intent);
            }

        });

        btnVisualizaValidadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SinapActivity.this, VisualizarDeficienciasAprovadasActivity.class);
                startActivity(intent);
            }

        });

        btnVisualizaNegadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SinapActivity.this, VisualizarDeficienciasNegadasActivity.class);
                startActivity(intent);
            }

        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SinapActivity.this, RegisterActivity.class);
                startActivity(intent);
            }

        });

        btnVisualizaTodas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SinapActivity.this, VisualizarTodasDeficienciasEnviadasActivity.class);
                startActivity(intent);
            }

        });

    }
}
