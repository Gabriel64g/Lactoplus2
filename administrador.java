package com.example.lactoplus4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class administrador extends AppCompatActivity {
    private Button Bent;
    private TextView Tcriar;
    private EditText Tsenha, Temail;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);
        Tsenha = findViewById(R.id.admsenha);
        Temail = findViewById(R.id.admemail);
        Tcriar = findViewById(R.id.usuConta);
        Tcriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });
        Bent = findViewById(R.id.Bent);
        Bent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Temail.getText().toString().trim();
                String senha = Tsenha.getText().toString().trim();
                if(email.equals("") || senha.equals("")){
                    Toast.makeText(administrador.this, "preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
                else {
                    verificar();
                }
            }
        });
    }
    private void cadastrar() {
        // abrir activity
        Intent intent = new Intent(this, contaADM.class);
        startActivity(intent);
    }
    private void verificar(){
        String email = Temail.getText().toString().trim();
        String senha = Tsenha.getText().toString().trim();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(administrador.this, "login realizado com sucesso", Toast.LENGTH_SHORT).show();
                    entrar();
                }
                String erro;
                try {
                    throw task.getException();
                }catch (Exception e){
                    erro = "Erro ao logar usuario";
                    Toast.makeText(administrador.this, "erro", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioAtual != null){
            entrar();
        }
    }

    private void entrar(){
        Intent intent = new Intent(administrador.this, geral.class);
        startActivity(intent);
    }
}