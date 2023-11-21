package com.example.lactoplus4;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class Gado extends AppCompatActivity {
    private EditText gidade, gestad, gleite, gbrinco, gnome;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private Button gatualizar, balt, bnao, bdelet;
    private TextView altgnome, altgidade, altgest, altgleite;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gado);
        String dados = getIntent().getStringExtra("brinco");

        gidade = findViewById(R.id.altIdade);
        gestad = findViewById(R.id.altEst);
        gleite = findViewById(R.id.altLeit);
        gbrinco = findViewById(R.id.altBrinc);
        gnome = findViewById(R.id.altNome);
        balt = findViewById(R.id.balt);
        bnao = findViewById(R.id.bnao);

        altgnome = findViewById(R.id.altgnome);
        altgidade = findViewById(R.id.altgidade);
        altgest= findViewById(R.id.altgest);
        altgleite= findViewById(R.id.altgleite);

        dados(dados);

        gatualizar = findViewById(R.id.gatualizar);
        gatualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gidade.setVisibility(View.VISIBLE);
                gestad.setVisibility(View.VISIBLE);
                gleite.setVisibility(View.VISIBLE);
                gbrinco.setVisibility(View.VISIBLE);
                gnome.setVisibility(View.VISIBLE);
                balt.setVisibility(View.VISIBLE);
                bnao.setVisibility(View.VISIBLE);
                gatualizar.setVisibility(View.INVISIBLE);
            }
        });

        bnao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gidade.setVisibility(View.INVISIBLE);
                gestad.setVisibility(View.INVISIBLE);
                gleite.setVisibility(View.INVISIBLE);
                gbrinco.setVisibility(View.INVISIBLE);
                gnome.setVisibility(View.INVISIBLE);
                balt.setVisibility(View.INVISIBLE);
                bnao.setVisibility(View.INVISIBLE);
                gatualizar.setVisibility(View.VISIBLE);
            }
        });

        balt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atualizar();
            }
        });
        bdelet = findViewById(R.id.bdelet);
        bdelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletar(dados);
            }
        });
    }
    private void atualizar(){
        String idade = gidade.getText().toString().trim();
        String brinco = gbrinco.getText().toString().trim();
        String nome = gnome.getText().toString().trim();
        String estado = gestad.getText().toString().trim();
        String leite = gleite.getText().toString().trim();
        db.collection("gado").document("mimosa")
                .update("idade", idade, "brinco", brinco, "nome", nome, "estado", estado, "leite", leite);
        Toast.makeText(this, "Alterado com Sucesso", LENGTH_SHORT).show();
        salvo();
    }
    private void salvo(){
        Intent intent = new Intent(this,geral.class);
        startActivity(intent);
    }
    private void deletar(String brinco){
        db.collection("gado").document(brinco)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Gado.this, "deletado com Sucesso", LENGTH_SHORT).show();
                    }
                });
        Intent intent = new Intent(this, geral.class);
        startActivity(intent);
    }
    private void dados(String brinco){
        db.collection("gado").document(brinco).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                altgnome.setText(value.getString("nome"));
                altgidade.setText(value.getString("idade"));
                altgest.setText(value.getString("estado"));
                altgleite.setText(value.getString("leite"));
            }
        });
    }
}