package br.edu.ifrs.projetoenge3.insercao;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import br.edu.ifrs.projetoenge3.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailField, passwordField, nomeField, cpfField, matriculaField;
    private Spinner userTypeSpinner;
    private Button registerButton;
    private LinearLayout layoutAluno, layoutSINAP, layoutProfessor;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializando Firebase Auth e Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Inicializando campos e layouts
        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);
        nomeField = findViewById(R.id.editTextNome);
        cpfField = findViewById(R.id.editTextCPF);
        matriculaField = findViewById(R.id.editTextMatricula);
        userTypeSpinner = findViewById(R.id.spinnerUserType);
        registerButton = findViewById(R.id.buttonRegister);
        layoutAluno = findViewById(R.id.layoutAluno);
        layoutSINAP = findViewById(R.id.layoutSINAP);
        layoutProfessor = findViewById(R.id.layoutProfessor);

        // Configurando o Spinner com os tipos de usuário
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.user_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter);

        // Mostrar os campos corretos com base no tipo de usuário selecionado
        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String userType = parentView.getItemAtPosition(position).toString();
                if (userType.equals("Aluno")) {
                    layoutAluno.setVisibility(View.VISIBLE);
                    layoutSINAP.setVisibility(View.GONE);
                    layoutProfessor.setVisibility(View.GONE);
                } else if (userType.equals("SINAP")) {
                    layoutAluno.setVisibility(View.GONE);
                    layoutSINAP.setVisibility(View.VISIBLE);
                    layoutProfessor.setVisibility(View.GONE);
                } else if (userType.equals("Professor")) {
                    layoutAluno.setVisibility(View.GONE);
                    layoutSINAP.setVisibility(View.GONE);
                    layoutProfessor.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        // Logica de cadastro ao clicar no botão
        registerButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String userType = userTypeSpinner.getSelectedItem().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(RegisterActivity.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Criar conta com Firebase Auth
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String userID = auth.getCurrentUser().getUid();
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("tipoUsuario", userType);
                            userMap.put("email", email);

                            // Adicionar campos específicos com base no tipo de usuário
                            if (userType.equals("Aluno")) {
                                String nome = nomeField.getText().toString().trim();
                                String cpf = cpfField.getText().toString().trim();
                                String matricula = matriculaField.getText().toString().trim();

                                userMap.put("nome", nome);
                                userMap.put("CPF", cpf);
                                userMap.put("matricula", matricula);

                            } // SINAP e Professor podem ter campos adicionais aqui

                            // Salvar no Firestore
                            db.collection("usuarios").document(userID).set(userMap)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(RegisterActivity.this, "Usuário registrado com sucesso!", Toast.LENGTH_SHORT).show();
                                        // Redirecionar ou finalizar
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(RegisterActivity.this, "Erro ao registrar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });

                        } else {
                            Toast.makeText(RegisterActivity.this, "Falha no cadastro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
