package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public EditText etEmail, etPass;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar  actionBar = getSupportActionBar(); actionBar.hide();
    }


    public void IniciarSesion(View v){
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPassword);
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();

        Intent intent1 = new Intent(this, Index.class);
        intent1.putExtra("email",email);
        intent1.putExtra("pass",pass);

        //Prueba de base de datos--------------------------
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("pass", pass);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this,"Bien",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Mal",Toast.LENGTH_LONG).show();
                    }
                });

        //--------Aqui termina la prueba----------------------------


        startActivity(intent1);

    }

    public void CrearCuenta(View v){

    }
}