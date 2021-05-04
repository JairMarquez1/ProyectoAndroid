package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public EditText etEmail, etPass;

    //Se crea propiedad de tipo Textview:
    TextView text_registro;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //(Instancia) Puente entre TextView y codigo de programacion (TEXTVIEW XML_ PAGINA LOGIN)
        text_registro = findViewById(R.id.textView_registro);


        //Acciones a ejecutar cuando se de click al textview- REGISTRATE:
        //Se llama a mandar al activity "Registrar".
        text_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se instancia una propiedad de tipo Intent, en la cual se manda
                // a llamar a un nuevo activity, en este caso el activity de "Registro"
                Intent intent_registro = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent_registro);
            }
        });


    }


    public void IniciarSesion(View v){
        db = FirebaseFirestore.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPassword);
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();


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


        Intent intent1 = new Intent(this, IndexActivity.class);
        intent1.putExtra("email",email);
        intent1.putExtra("pass",pass);
        startActivity(intent1);

    }

    public void CrearCuenta(View v){
        Intent intentReg = new Intent(this,RegisterActivity.class);
        startActivity(intentReg);
    }



}