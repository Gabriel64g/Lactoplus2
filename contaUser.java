package com.example.lactoplus4;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class contaUser extends AppCompatActivity {
    private EditText Tnome, Temail, Tsenha, Tadm, versenha;
    private Button bsalvar;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta_user);
        Tnome = findViewById(R.id.nome);
        Temail = findViewById(R.id.email);
        Tsenha = findViewById(R.id.senha);
        Tadm = findViewById(R.id.adm);
        versenha = findViewById(R.id.versenha);
        bsalvar = findViewById(R.id.gsalvar);
        bsalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cadastrar();
            }
        });
    }
    private void Cadastrar() {
        String no = Tnome.getText().toString().trim();
        String ema = Temail.getText().toString().trim();
        String senh = Tsenha.getText().toString().trim();
        String adm = Tadm.getText().toString().trim();
        String vers = versenha.getText().toString().trim();
        if (!no.equals("")) {
            if (!ema.equals("")) {
                if (!senh.equals("") && senh != vers) {
                    if (!adm.equals("")) {
                        Map<String, Object> user = new HashMap<>();
                        user.put("nome", no);
                        user.put("email", ema);
                        user.put("senha", senh);
                        user.put("adm", adm);
                        user.put("ident", "user");
                        db.collection("users").document(ema)
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private void Continuar(){
        Intent intent = new Intent(this, geral.class);
        startActivity(intent);
    }
    private void salvar(){
        String email = Temail.getText().toString().trim();
        String senha = Tsenha.getText().toString().trim();
        if(!email.equals("")){
            if(senha.length() >= 6){
                auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> cadastro) {
                        if(cadastro.isSuccessful()){
                            Toast.makeText(contaUser.this, "Cadastro realizado com Sucesso", LENGTH_SHORT).show();
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
                            Toast.makeText(contaUser.this, erroExcecao, LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else{
                Toast.makeText(this, "A senha tem que tem no minimo 6 caracteres", LENGTH_SHORT).show();
                Tsenha.setText("");
            }
        }else{
            Toast.makeText(this, "Preencha o campo email", LENGTH_SHORT).show();
        }
    }
}