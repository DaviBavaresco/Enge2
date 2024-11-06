package br.edu.ifrs.projetoenge3.usuarios;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import br.edu.ifrs.projetoenge3.R;
import br.edu.ifrs.projetoenge3.visualizacao.VisualizarDeficienciasAprovadasActivity;

public class ProfessorActivity extends AppCompatActivity {

    private Button btnList;
    private Button btnSair;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_professor);
        btnList = findViewById(R.id.btnList);
        btnSair = findViewById(R.id.btnSair);

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfessorActivity.this, VisualizarDeficienciasAprovadasActivity.class);
                startActivity(intent);
            }
        });

        //Para sair da aplicacao
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });

    }
}
