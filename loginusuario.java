package com.example.lactoplus4;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class loginusuario extends AppCompatActivity {
    private Button Bentrar;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseAuth  auth = FirebaseAuth.getInstance();
    String usuarioID;
    private EditText Temail, Tsenha;
    private TextView Tcriar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginusuario);

        Temail = findViewById(R.id.usuemail);
        Tsenha = findViewById(R.id.ususenha);
        Tcriar = findViewById(R.id.usuConta);
        Tcriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });
        Bentrar = findViewById(R.id.Bentrar);
        Bentrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entrar();
//                verificar();
            }

//                String email = Temail.getText().toString().trim();
//                String senha = Tsenha.getText().toString().trim();
//                if(email.equals(") || senha.equals(")){
//                    Toast.makeText(loginusuario.this, "preencha todos os campos", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    verificar();
//                }
//            }
        });
    }
    private void calma(){
        Toast.makeText(this, "Pera ai que deu bosta!!", Toast.LENGTH_SHORT).show();
    }
    private void cadastrar() {
        // abrir activity
        Intent intent = new Intent(this, contaUser.class);
        startActivity(intent);
    }
    private void verificar(){
        String email = Temail.getText().toString().trim();
        String password = Tsenha.getText().toString().trim();

        Task<AuthResult> authResultTask = mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(loginusuario.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
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
        String email = Temail.getText().toString().trim();
        String senha = Tsenha.getText().toString().toString();
        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent intent = new Intent(loginusuario.this,geral.class);
                db.collection("usres").document(email).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        Toast.makeText(loginusuario.this, "seja bem vindo " + value.getString("nome"), Toast.LENGTH_SHORT).show();
                    }
                });

                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(loginusuario.this, "Senha ou Email Invalidos", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateUI(FirebaseUser user) {

    }
    private void iniciar(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}