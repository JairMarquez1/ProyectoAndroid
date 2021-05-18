package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    //Se crea propiedad de tipo TextInputEditText:
    TextInputEditText txt_usuario_r,txt_mail_r,txt_clave_r,txt_clave2_r;
    //Se crea propiedad de tipo Button:
    Button btn_registro;
    //Se crea objeto de tipo FirebaseAuth:
    FirebaseAuth mAuth;
    //Se crea objeto de tipo Firebasefirestore:
    FirebaseFirestore mFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        //(Instancia) Puente entre TextViews y codigo de programacion (TEXTVIEWS XML_ PAGINA LOGIN)
        txt_usuario_r = findViewById(R.id.txt_registro_usuario);
        txt_mail_r = findViewById(R.id.txt_registro_correo);
        txt_clave_r = findViewById(R.id.txt_registro_clave);
        txt_clave2_r = findViewById(R.id.txt_registro_clave2);

        //(Instancia) Puente entre button de registro y codigo de programacion (button_registro XML_ PAGINA LOGIN)
        btn_registro = findViewById(R.id.btn_registrarse);


        //(Instancia de FirebaseAuth):
        mAuth = FirebaseAuth.getInstance();
        //(Instancia de FirebaseFireStore):
        mFirestore = FirebaseFirestore.getInstance();


        //Acciones a ejecutar cuando se de click al boton- REGISTRAR:
        //Se llama a mandar a ejecutar la función "Registrar".
        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }


    private void register(){
        //AQUI VA METODO DE REGISTRO
        //Se almacena el valor de cada edit Text dentro de una variable de tipo String;
        String email= txt_mail_r.getText().toString();
        String clave= txt_clave_r.getText().toString();
        String user= txt_usuario_r.getText().toString();
        String clave2= txt_clave2_r.getText().toString();


        //CONDICIÓN PARA VALIDAR CAMPOS VACIOS:
        if(!user.isEmpty() || !email.isEmpty() || !clave.isEmpty() || !clave2.isEmpty()){
            //VALIDACION CORREO:
            if(correo_valido(email)){
                //Validación de contraseñas:
                if(clave.equals(clave2)){
                    //VALIDACION DE LONGITUD DE CONTRASEÑA:
                    if(clave.length()>6){
                        createUser(email,clave,user);//se llama Metodo para crear usuario
                    }else{
                        //CONTRASEÑAS MENOR A 6 DIGITOS: El modo de autenticación de firebase no permite contraseñas de menos de 6 caracteres:
                        Toast.makeText(this, "La contraseña debe contener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //CONTRASEÑAS INVALIDAS/DISTINTAS
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }

            }else{
                //FINES PRACTICOS, IMPRESION EN CONSOLA:
                //Log.d("CAMPO","email"+ email);
                //Log.d("CAMPO","clave"+ clave);
                //CORREO INVALIDO:
                Toast.makeText(this, "Introduce un correo válido", Toast.LENGTH_SHORT).show();

            }


        }else{
            //No se rellenaron todos los campos
            Toast.makeText(this, "Por favor rellena todos los datos :(", Toast.LENGTH_SHORT).show();
        }

    }

    //METODO DE CREACION DE USUARIO:
    private void createUser(String email, String password, String name){
        //Metodo createUserWithEmailandpassword: https://firebase.google.com/docs/auth/web/manage-users?hl=es
        //Se le añade un addCompleteListener : https://developer.android.com/reference/com/google/android/play/core/tasks/Task
        //Si la tarea es completada con exito, arrojará su  respectivo mensaje, de caso contrario, también alertará al usuario:
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Se almacena el UID de usuario generado por sesión, puesto que este se le asigna al ID del documento dentro de la BDD:
                    String id= mAuth.getCurrentUser().getUid();
                    //Ahora se procede a crear el documento en la BDD con el UID de la sesión del usuario, y la información pertinente:
                    //Nombre de usuario,correo y contraseña.
                    //DOCUMENTACIÓN MAP: https://jarroba.com/map-en-java-con-ejemplos/
                    Map<String, Object> map = new HashMap<>();
                    map.put("username",name); // Añade el elemento "name"  al Map
                    map.put("email",email); // Añade el elemento "email" al Map
                    map.put("password",password); // Añade el elemento "password"  al Map

                    Toast.makeText(RegisterActivity.this, "Registro de "+ name+","+email+","+password+"UID:"+id, Toast.LENGTH_SHORT).show();
                    mFirestore.collection("Usuarios").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //Si se completa exitosamente el registro a la BDD mandará un toast, de caso contrario indicará al usuario:
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(RegisterActivity.this, "El usuario no pudo registrarse en la BDD", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "No se pudo realizar registro", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //METODO DE VALIDACIÓN DE CORREO: (REGEX) (Solo es un metodo para poder validar que se introduzca un correo valido :))
    public boolean correo_valido(String email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}