package br.edu.ifrs.projetoenge3.edicao;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import br.edu.ifrs.projetoenge3.R;
import br.edu.ifrs.projetoenge3.usuarios.AlunoActivity;
import br.edu.ifrs.projetoenge3.usuarios.Deficiencia;
import br.edu.ifrs.projetoenge3.visualizacao.ListaDeficienciasActivity;

public class EditDeficienciaActivity extends AppCompatActivity {

    private EditText editTextDeficiencia, editTextExplica;
    private TextView textViewMatricula;

    private Button buttonSave;
    private FirebaseFirestore db;
    private String documentId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_deficiencia);

        // Inicializando views
        textViewMatricula = findViewById(R.id.textViewMatricula);
        editTextDeficiencia = findViewById(R.id.editTextDeficiencia);
        editTextExplica = findViewById(R.id.editTextExplica);
        buttonSave = findViewById(R.id.buttonSave);
        db = FirebaseFirestore.getInstance();

        // Recupera a deficiência que foi passada da lista
        Deficiencia deficiencia = getIntent().getParcelableExtra("deficiencia");
        if (deficiencia != null) {
            documentId = deficiencia.getDocumentId();
            textViewMatricula.setText(deficiencia.getMatricula());
            editTextDeficiencia.setText(deficiencia.getDeficiencia());
            editTextExplica.setText(deficiencia.getExplica());
        }

        // Salva as edições feitas pelo usuário
        buttonSave.setOnClickListener(v -> salvarAlteracoes());
    }

    private void salvarAlteracoes() {
        String novaMatricula = textViewMatricula.getText().toString();
        String novaDeficiencia = editTextDeficiencia.getText().toString();
        String novaExplicacao = editTextExplica.getText().toString();

        // Atualizando a deficiência no Firestore
        db.collection("deficiencias").document(documentId)
                .update("matricula", novaMatricula, "deficiencia", novaDeficiencia, "explica", novaExplicacao)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditDeficienciaActivity.this, "Deficiência atualizada com sucesso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditDeficienciaActivity.this, ListaDeficienciasActivity.class);
                    startActivity(intent);
                    //finish(); // Fecha a activity após salvar
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditDeficienciaActivity.this, "Erro ao atualizar deficiência", Toast.LENGTH_SHORT).show();
                });
    }
}
