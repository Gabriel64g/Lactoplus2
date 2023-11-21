package com.example.lactoplus4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class add extends AppCompatActivity {

    private Button Bterminar, Bcancelar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private EditText Gnome, Gidade, Gleite, Gest, Gbrinco;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Gnome = findViewById(R.id.Gnome);
        Gidade = findViewById(R.id.Gidade);
        Gleite = findViewById(R.id.Gleite);
        Gest = findViewById(R.id.Gestad);
        Gbrinco = findViewById(R.id.Gbrinco);
        Bterminar = findViewById(R.id.Bsalvar);
        Bterminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
            }
        });
        Bcancelar = findViewById(R.id.Bcancel);
        Bcancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void principal(){
        Intent intent = new Intent(this, geral.class);
        startActivity(intent);
        Toast.makeText(this, "Gado salvo com Sucesso", Toast.LENGTH_SHORT).show();
    }
    private void salvar(){
        db.collection("user").document(user.getEmail()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String nome = Gnome.getText().toString().trim();
                String idade = Gidade.getText().toString().trim();
                String estado = Gest.getText().toString().trim();
                String brinco = Gbrinco.getText().toString().trim();
                String leite = Gleite.getText().toString().trim();
                String adm = value.getString("adm");
                System.out.println(adm);

                if (!nome.equals("") || !idade.equals("") || !estado.equals("") || !brinco.equals("") || !leite.equals("")) {
                    Map<String, Object> gado = new HashMap<>();
                    gado.put("nome", nome);
                    gado.put("idade", idade);
                    gado.put("estado", estado);
                    gado.put("brinco", brinco);
                    gado.put("leite", leite);
                    gado.put("adm", adm);
                    db.collection("gado").document(brinco)
                            .set(gado).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    principal();
                                }
                            });
                }
                else{
                    alerta();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void alerta(){
        Toast.makeText(this, "Preencher todos os campos", Toast.LENGTH_SHORT).show();
    }
}