package br.edu.ifrs.projetoenge3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlunoInsereDefiActivity extends AppCompatActivity {

    private EditText editTextDeficiencia;
    private EditText editTextExplica;
    private Button buttonEnviarDeficiencia;
    private Button buttonListarDeficiencias;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private String userId;  // ID do usuário logado
    private String matricula;  // Matrícula do aluno

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insere_deficiencia);

        // Inicializar Firebase Auth e Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();

        // Inicializar os componentes da interface
        editTextDeficiencia = findViewById(R.id.editTextDeficiencia);
        editTextExplica = findViewById(R.id.editTextExplica);
        buttonEnviarDeficiencia = findViewById(R.id.buttonEnviarDeficiencia);
        buttonListarDeficiencias = findViewById(R.id.buttonListarDeficiencias); // Botão para listar as deficiências
        progressBar = findViewById(R.id.progressBar);

        // Obter matrícula do aluno
        obterDadosDoAluno();

        // Configurar o botão para enviar a deficiência
        buttonEnviarDeficiencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deficiencia = editTextDeficiencia.getText().toString().trim();
                String explica = editTextExplica.getText().toString().trim();

                if (deficiencia.isEmpty()) {
                    editTextDeficiencia.setError("Insira a deficiência");
                    return;
                }

                enviarDeficienciaAoSinap(deficiencia, explica);
            }
        });
        // Configurar o botão para listar as deficiências
        buttonListarDeficiencias.setOnClickListener(v -> {
            listarDeficienciasDoAluno();
        });
    }

    // Método para obter os dados do aluno (como matrícula) da coleção "usuarios"
    private void obterDadosDoAluno() {
        progressBar.setVisibility(View.VISIBLE);

        db.collection("usuarios").document(userId).get()
                .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            matricula = documentSnapshot.getString("matricula");

                            if (matricula != null) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(AlunoInsereDefiActivity.this, "Matrícula: " + matricula, Toast.LENGTH_SHORT).show();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(AlunoInsereDefiActivity.this, "Matrícula não encontrada.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(AlunoInsereDefiActivity.this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new com.google.android.gms.tasks.OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AlunoInsereDefiActivity.this, "Erro ao buscar matrícula: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Método para enviar a deficiência ao SINAP, salvando no Firestore
    private void enviarDeficienciaAoSinap(String deficiencia, String explica) {
        progressBar.setVisibility(View.VISIBLE);

        // Criar um mapa com as informações da deficiência
        Map<String, Object> dadosDeficiencia = new HashMap<>();
        dadosDeficiencia.put("userId", userId);
        dadosDeficiencia.put("matricula", matricula);
        dadosDeficiencia.put("deficiencia", deficiencia);
        dadosDeficiencia.put("explica", explica);
        dadosDeficiencia.put("status", "pendente");  // Status inicial

        // Salvar a deficiência na coleção "deficiencias"
        db.collection("deficiencias").add(dadosDeficiencia)
                .addOnSuccessListener(documentReference -> {
                    String documentId = documentReference.getId();  // Aqui você obtém o ID do documento

                    // Atualize o documento com o documentId se precisar, ou use esse ID no futuro
                    db.collection("deficiencias").document(documentId)
                            .update("documentId", documentId)  // Atualizar com o próprio ID gerado
                            .addOnSuccessListener(aVoid -> {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(AlunoInsereDefiActivity.this, "Deficiência enviada com sucesso!", Toast.LENGTH_SHORT).show();
                                editTextDeficiencia.setText("");  // Limpar o campo de texto
                            })
                            .addOnFailureListener(e -> {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(AlunoInsereDefiActivity.this, "Erro ao salvar o documentId: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AlunoInsereDefiActivity.this, "Erro ao enviar deficiência: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Método para listar as deficiências do aluno
    private void listarDeficienciasDoAluno() {
        progressBar.setVisibility(View.VISIBLE);

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
                    Intent intent = new Intent(AlunoInsereDefiActivity.this, ListaDeficienciasActivity.class);
                    intent.putParcelableArrayListExtra("deficiencias", new ArrayList<>(deficiencias));  // Agora funciona
                    startActivity(intent);

                    progressBar.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AlunoInsereDefiActivity.this, "Erro ao buscar deficiências: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}
