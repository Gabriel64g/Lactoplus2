package com.example.lactoplus4;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class perfil extends AppCompatActivity {
    private Button Blogout, bUpload, altSalv;
    private ImageView Iperfil;
    private Uri imagem_uri;
    private EditText altusunome, altUsuSenha, altususemail;
    String usuarioID;
    private TextView Tnome, Temail, Tprodutor, qtd_gado;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iniciar();
        setContentView(R.layout.activity_perfil);
        altusunome = findViewById(R.id.altusunome);
        Tprodutor = findViewById(R.id.Tprodutor);
        qtd_gado = findViewById(R.id.qtd_gado);
        altUsuSenha = findViewById(R.id.altUsuSenha);
        altususemail = findViewById(R.id.altususemail);
        Tnome = findViewById(R.id.Unome);
        Temail = findViewById(R.id.Uemail);
        Blogout = findViewById(R.id.Bsair);
        Blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                comeco();
                Toast.makeText(perfil.this, "Ate logo", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        bUpload = findViewById(R.id.bUpload);
        bUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                altusunome.setVisibility(View.VISIBLE);
                altususemail.setVisibility(View.VISIBLE);
                altUsuSenha.setVisibility(View.VISIBLE);
                bUpload.setVisibility(View.INVISIBLE);
                Blogout.setVisibility(View.INVISIBLE);
                altSalv.setVisibility(View.VISIBLE);
            }
        });
        altSalv = findViewById(R.id.altSalvar);
        altSalv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atualizar();
                altusunome.setVisibility(View.INVISIBLE);
                altususemail.setVisibility(View.INVISIBLE);
                altUsuSenha.setVisibility(View.INVISIBLE);
                altSalv.setVisibility(View.INVISIBLE);
                bUpload.setVisibility(View.VISIBLE);
                Blogout.setVisibility(View.VISIBLE);
            }
        });
    }
    private void comeco(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void iniciar(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            emailusu();
        }
    }
    private void atualizar(){
        String nome = altusunome .getText().toString().trim();
        String senha = altUsuSenha.getText().toString().trim();
        String email = altususemail.getText().toString().trim();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("users").document(user.getEmail()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                db.collection("adms").document(value.getString("adm")).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(!nome.equals("")){
                            if (value.getString("ident").equals("user")){
                                db.collection("users").document(String.valueOf(user.getEmail().toString()))
                                        .update("nome", nome);
                            }
                            else{
                                db.collection("adms").document(value.getString("adm"))
                                        .update("nome", nome);
                            }
                        }
                        else if(!senha.equals("")){
                            if (value.getString("ident").equals("user")){
                                db.collection("users").document(String.valueOf(user.getEmail().toString()))
                                        .update("email", email);
                            }
                            else{
                                db.collection("adms").document(value.getString("adm"))
                                        .update("nome", nome, "senha", senha, "email", email);
                            }
                        }else if (!email.equals("")){
                            if (value.getString("ident").equals("user")){
                                db.collection("users").document(String.valueOf(user.getEmail().toString()))
                                        .update("senha", senha);
                            }
                            else{
                                db.collection("adms").document(value.getString("adm"))
                                        .update("email", email);
                            }
                        }
                    }
                });
            }
        });
    }
    private void emailusu(){
        Toast.makeText(this, user.getUid().toString(), LENGTH_SHORT).show();
        db.collection("users").document(user.getEmail().toString()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot usuario, @Nullable FirebaseFirestoreException error) {

                Temail.setText(usuario.getString("email"));
                Tnome.setText(usuario.getString("nome"));
                String adm = usuario.getString("adm");
            db.collection("adms").document(adm).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    Tprodutor.setText(value.getString("nome"));
                    String dados = getIntent().getStringExtra("qtd_gado");
                    qtd_gado.setText(dados);
                }
            });
            }
        });
    }
}