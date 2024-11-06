package br.edu.ifrs.projetoenge3.usuarios;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import br.edu.ifrs.projetoenge3.insercao.AlunoInsereDefiActivity;
import br.edu.ifrs.projetoenge3.R;
import br.edu.ifrs.projetoenge3.visualizacao.ListaDeficienciasActivity;

public class AlunoActivity extends AppCompatActivity {


    private Button btnInsere;
    private Button btnLista;
    private Button btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aluno);
        btnInsere = findViewById(R.id.btnInsere);
        btnLista = findViewById(R.id.btnLista);
        btnSair = findViewById(R.id.btnSair);

        btnInsere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlunoActivity.this, AlunoInsereDefiActivity.class);
                startActivity(intent);
            }

        });

        //Para listar as deficiencias do aluno enviadas
        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlunoActivity.this, ListaDeficienciasActivity.class);
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


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
