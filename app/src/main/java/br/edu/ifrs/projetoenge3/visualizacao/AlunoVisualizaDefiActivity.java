package br.edu.ifrs.projetoenge3.visualizacao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifrs.projetoenge3.R;
import br.edu.ifrs.projetoenge3.usuarios.Deficiencia;

public class AlunoVisualizaDefiActivity extends AppCompatActivity {



    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private String userId;  // ID do usuário logado
    private String matricula;  // Matrícula do aluno

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_deficiencia_aluno);

        // Inicializar Firebase Auth e Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();



        // Obter matrícula do aluno
        obterDadosDoAluno();
        //lista informações somente deste aluno
        listarDeficienciasDoAluno();

    }

    // Método para obter os dados do aluno (como matrícula) da coleção "usuarios"
    private void obterDadosDoAluno() {

        db.collection("usuarios").document(userId).get()
                .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            matricula = documentSnapshot.getString("matricula");

                            if (matricula != null) {
                                Toast.makeText(AlunoVisualizaDefiActivity.this, "Matrícula: " + matricula, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AlunoVisualizaDefiActivity.this, "Matrícula não encontrada.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AlunoVisualizaDefiActivity.this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new com.google.android.gms.tasks.OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AlunoVisualizaDefiActivity.this, "Erro ao buscar matrícula: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



    // Método para listar as deficiências do aluno
    private void listarDeficienciasDoAluno() {

        db.collection("deficiencias")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Deficiencia> deficiencias = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Deficiencia deficiencia = documentSnapshot.toObject(Deficiencia.class);
                        deficiencia.setDocumentId(documentSnapshot.getId());  // Adicionar o documentId
                        deficiencias.add(deficiencia);
                    }

                    // Passar a lista de deficiências para a nova Activity para exibição
                    Intent intent = new Intent(AlunoVisualizaDefiActivity.this, ListaDeficienciasActivity.class);
                    intent.putParcelableArrayListExtra("deficiencias", new ArrayList<>(deficiencias));  // Agora funciona
                    startActivity(intent);

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AlunoVisualizaDefiActivity.this, "Erro ao buscar deficiências: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}
