package br.edu.ifrs.projetoenge3;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import br.edu.ifrs.projetoenge3.usuarios.AlunoActivity;
import br.edu.ifrs.projetoenge3.usuarios.ProfessorActivity;
import br.edu.ifrs.projetoenge3.usuarios.SinapActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar Firebase Auth e Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Inicializar os campos e botão
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Configurar clique no botão "Login"
        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Por favor, insira email e senha.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Fazer login com Firebase Auth
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Obter ID do usuário logado
                            String userId = auth.getCurrentUser().getUid();

                            // Verificar o tipo de usuário no Firestore
                            db.collection("usuarios").document(userId).get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                                            String tipoUsuario = documentSnapshot.getString("tipoUsuario");

                                            if (tipoUsuario != null) {
                                                redirecionarUsuario(tipoUsuario);
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Tipo de usuário não encontrado.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Usuário não encontrado no banco de dados.", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(LoginActivity.this, "Erro ao verificar usuário: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });} else {
                            Toast.makeText(LoginActivity.this, "Email ou senha incorreto", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    // Método para redirecionar o usuário para a tela correta
    private void redirecionarUsuario(String tipoUsuario) {
        if (tipoUsuario.equals("Aluno")) {
            startActivity(new Intent(LoginActivity.this, AlunoActivity.class));
        } else if (tipoUsuario.equals("SINAP")) {
            startActivity(new Intent(LoginActivity.this, SinapActivity.class));
        } else if (tipoUsuario.equals("Professor")) {
            startActivity(new Intent(LoginActivity.this, ProfessorActivity.class));
        }
        finish(); // Fecha a tela de login
    }
}
