package br.edu.ifrs.projetoenge3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SinapActivity extends AppCompatActivity {

    private Button btnVisualizaPendentes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sinap);

        btnVisualizaPendentes = findViewById(R.id.button);

        btnVisualizaPendentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SinapActivity.this, VisualizarDeficienciasPendentesActivity.class);
                startActivity(intent);
            }

        });

    }
}
