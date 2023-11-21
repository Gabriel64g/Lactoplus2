package com.example.lactoplus4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class geral extends AppCompatActivity {
    private ImageView Imgperfil;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FloatingActionButton FbAdd;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListView listaG;
    ArrayList<String> teste = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geral);
        adicionar();
        FbAdd = findViewById(R.id.FbAdd);
        Imgperfil = findViewById(R.id.Imgperfil);
        FbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
        Imgperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                perfil();
            }
        });
        listaG = findViewById(R.id.listaGado);

        listaG.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = listaG.getItemAtPosition(position).toString();
                Toast.makeText(geral.this, item,Toast.LENGTH_SHORT).show();
                gado(item);
            }
        });
    }
    private void add(){
        Intent intent = new Intent(this, add.class);
        startActivity(intent);
    }
    private void perfil(){
        Intent intent = new Intent(this, perfil.class);
        db.collection("user").document(user.getEmail()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot usuario, @Nullable FirebaseFirestoreException error) {
                db.collection("gado")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int cont = 0;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(usuario.getString("adm") == document.getString("adm")){
                                            teste.add(document.getString("brinco"));
                                            cont++;
                                        }
                                    }
                                    intent.putExtra("qtd_gado", cont);

                                }
                            }
                        });
            }
        });
        startActivity(intent);
    }
    private void gado(String item){
        Intent intent = new Intent(this, Gado.class);
        intent.putExtra("brinco", item);
        startActivity(intent);
    }

    private void iniciar(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void adicionar(){
        db.collection("user").document(user.getEmail()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot usuario, @Nullable FirebaseFirestoreException error) {
                db.collection("gado")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                            if(usuario.getString("adm") == document.getString("adm")){
                                                teste.add(document.getString("brinco"));
                                            }
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                            getApplicationContext(),
                                            android.R.layout.simple_list_item_1,
                                            android.R.id.text1,
                                            teste
                                    );

                                    listaG.setAdapter(adapter);

                                } else {
                                }
                            }
                        });
            }
        });

    }


}