package com.example.lactoplus4;

import static android.widget.Toast.LENGTH_SHORT;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.ActionCodeSettings;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class contaADM extends AppCompatActivity {
    private Button Bterminar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private EditText nome, email, senha, versenha, adm;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta_adm);
        Bterminar = findViewById(R.id.Bterminar);
        nome = findViewById(R.id.nome);
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        versenha = findViewById(R.id.verSenha);
        adm = findViewById(R.id.adm);
        Bterminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificar();
            }
        });
    }
    private void principal(){
        Intent intent = new Intent(this, geral.class);
        startActivity(intent);
    }
    private void verificar(){
        String no = nome.getText().toString().trim();
        String ema = email.getText().toString().trim();
        String senh = senha.getText().toString().trim();
        String ad = adm.getText().toString().trim();
        String vers = versenha.getText().toString().trim();
        if (!no.equals("")) {
            if (!ema.equals("")) {
                if (!senh.equals("") && senh.equals(vers)) {
                    if (!ad.equals("")) {
                        Map<String, Object> user = new HashMap<>();
                        user.put("nome", no);
                        user.put("email", ema);
                        user.put("senha", senh);
                        user.put("adm", ad);
                        user.put("ident", "adm");
                        db.collection("adms").document(ad)
                                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        salvar();
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Preencher a identificação do administrador", LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Preencher senha", LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Preencha o email", LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Preencha o nome", LENGTH_SHORT).show();
        }
    }
    private void salvar(){
        String ema = email.getText().toString().trim();
        String sen = senha.getText().toString().trim();
        if(!email.equals("")){
            if(senha.length() >= 6){
                auth.createUserWithEmailAndPassword(ema, sen).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<AuthResult> cadastro) {
                        if(cadastro.isSuccessful()){
                            Toast.makeText(contaADM.this, "Cadastro realizado com Sucesso", LENGTH_SHORT).show();
                            Continuar();
                        }
                        else{
                            String erroExcecao = "";
                            try{
                                throw cadastro.getException();


                            } catch (FirebaseAuthWeakPasswordException e) {
                                erroExcecao = "Digite uma senha mais forte, contendo letras e números! ";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erroExcecao = "O email é inválido, digite um novo e-mail";
                            } catch (FirebaseAuthUserCollisionException e) {
                                erroExcecao = "Email já em está uso!";
                            } catch (Exception e) {
                                erroExcecao = "Erro ao efetuar cadastro!";
                                e.printStackTrace();
                            }
                            Toast.makeText(contaADM.this, erroExcecao, LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else{
                Toast.makeText(this, "A senha tem que tem no minimo 6 caracteres", LENGTH_SHORT).show();
                senha.setText("");
            }
        }else{
            Toast.makeText(this, "Preencha o campo email", LENGTH_SHORT).show();
        }
    }
    private void Continuar(){
        Intent intent = new Intent(this, geral.class);
        startActivity(intent);
    }
}