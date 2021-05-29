package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public EditText etEmail, etPass;

    //Se crea propiedad de tipo Textview (DIRECCION A REGISTRO):
    TextView text_registro;
    FirebaseFirestore db;

    //Se crea objeto de tipo FirebaseAuth (PARA EL LOGGIN CON CONTRASEÑA Y EMAIL ;D ):
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //(Instancia) Puente entre TextView y codigo de programacion (TEXTVIEW XML_ PAGINA LOGIN)
        text_registro = findViewById(R.id.textView_registro);
        //(Instancia) Puente entre EditText y codigo de programacion (EDITTEXTXML_ PAGINA LOGIN)
        etEmail = findViewById(R.id.etEmail);
        //(Instancia) Puente entre EditText y codigo de programacion (EDITTEXTXML_ PAGINA LOGIN)
        etPass = findViewById(R.id.etPassword);
        //(Instancia de FirebaseAuth):
        mAuth = FirebaseAuth.getInstance();



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
        /*db = FirebaseFirestore.getInstance();
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
                }); */

        //--------Aqui termina la prueba----------------------------


        //-----prueba code login con AUTH
        //AQUI VA METODO DE LOGIN
        //Se almacena el valor del edit Text dentro de una variable de tipo String;
        String email = etEmail.getText().toString();
        String clave = etPass.getText().toString();


        //Validación de campos vacios:
        if(!email.isEmpty() && !clave.isEmpty()){
            //validacion de correo:
            if(correo_valido(email)){
                //Con ayuda del metodo signInWithEmailAndPassword, se loguea la sesión, y en caso de existir en el Authenticathion,
                //permitirá acceder al feed.
                mAuth.signInWithEmailAndPassword(email,clave).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Si en dado caso se cumple con una contraseña y correo valido, significa que logueara a la pagina principal del sitio:
                        if (task.isSuccessful()) {
                            String id= mAuth.getCurrentUser().getUid();
                            Toast.makeText(MainActivity.this, "UID: "+ id, Toast.LENGTH_LONG).show();
                            ////Se crea intent que mandará a llamar activity de feed:
                            Intent activity_index = new Intent(MainActivity.this, IndexActivity.class);
                            //Se pasan datos de ID de usuario:
                            activity_index .putExtra("UID",id);
                            //Se llama Activity:
                            startActivity(activity_index);


                        }
                        else {
                            //Si en dado caso no se ejecuta la tarea con exito significa que el correo o contraseña son incorrectos:
                            Toast.makeText(MainActivity.this, "El email o contraseña que ingresaste no son correctas", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else{
                Toast.makeText(this, "Correo inválido!", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
        }



        //--- fin prueba login


        //--Borrar después
        Intent activity_index = new Intent(MainActivity.this, IndexActivity.class);
        startActivity(activity_index);
        //--

        /*Intent intent1 = new Intent(this, IndexActivity.class);
        intent1.putExtra("email",email);
        intent1.putExtra("pass",pass);
        startActivity(intent1);*/

    }

    public void CrearCuenta(View v){
        Intent intentReg = new Intent(this,RegisterActivity.class);
        startActivity(intentReg);
    }


//-------
    //METODOS DE VALIDACIONES:




    //METODO DE VALIDACIÓN DE CORREO: (REGEX)
    public boolean correo_valido(String email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    //----


}