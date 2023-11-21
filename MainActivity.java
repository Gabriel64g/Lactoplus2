package com.example.lactoplus4;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button Badm, Busuario;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Badm = findViewById(R.id.Badm);
        Badm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginadm();
            }
        });
        Busuario = findViewById(R.id.Busuario);
        Busuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginuser();
            }
        });
    }
    private void loginadm(){
        // abrir activity em enviar dados
        Intent intent = new Intent(this, administrador.class);
        startActivity(intent);
    }
    private void loginuser(){
        // abrir activity em enviar dados
        Intent intent = new Intent(this, loginusuario.class);
        startActivity(intent);
    }
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioAtual != null){
            entrar();
        }
    }

    private void entrar(){
        Intent intent = new Intent(this, geral.class);
        startActivity(intent);
    }
}