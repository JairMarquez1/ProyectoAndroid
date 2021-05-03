package com.example.proyectofinal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public EditText etEmail, etPass;

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

        startActivity(intent1);

    }

    public void CrearCuenta(View v){

    }
}