package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class IndexActivity extends AppCompatActivity {

    private String[] nombres = {"@alma","@hector","@jair","@erubey","@rair"};
    private String[] descr = {"En la playa <3","Cenando con mis amigos","En el gimnasio","Jugando League of Legends :)","Prgramando en python"};
    private ListView lvContent;
    private ArrayList<Post> listaPosts;
    public ImageView imgV;
    FirebaseStorage storage;
    StorageReference storageReference;
    public Uri path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        String email = getIntent().getStringExtra("email");

        listaPosts = new ArrayList<Post>();
        for (int i = 0; i < nombres.length;i++)
            listaPosts.add(new Post(nombres[i],descr[i]));
        PostAdapter adaptador = new PostAdapter(this);
        lvContent = findViewById(R.id.lvContent);
        lvContent.setAdapter(adaptador);

    }

    class PostAdapter extends ArrayAdapter<Post> {

        AppCompatActivity appCompatActivity;

        PostAdapter(AppCompatActivity context) {
            super(context, R.layout.list_item, listaPosts);
            appCompatActivity = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.list_item, null);

            TextView nombre = item.findViewById(R.id.tvUsuario);
            nombre.setText(listaPosts.get(position).getNombre());
            TextView descripcion = item.findViewById(R.id.tvDescrip);
            descripcion.setText(listaPosts.get(position).getDescripcion());
            ImageView imageView1 = item.findViewById(R.id.imageView);
            imageView1.setImageResource(R.mipmap.ic_launcher);
            imageView1.getLayoutParams().height = 1000;
            imageView1.requestLayout();
            return(item);
        }
    }
    //https://www.tutorialesprogramacionya.com/javaya/androidya/androidstudioya/detalleconcepto.php?codigo=47&inicio=40


    public void CrearPublicacion(View v){
        //Se crea el constructor de nuestro cuadro de diálogo llamando al diseño de new_post.xml
        AlertDialog.Builder builder = new AlertDialog.Builder(IndexActivity.this);
        builder.setView(R.layout.new_post);
        //Se declaran los botones positivo (en null por que se reescribirá mas adelante) y el negativo con su correspondiente texto
        builder.setPositiveButton("Publicar",null);
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //Se instancía nuestro diálogo utilizando el constructor anteriormente creado y se muestra
        final AlertDialog dialogo = builder.create();
        dialogo.show();
        dialogo.getWindow().setBackgroundDrawableResource(R.drawable.round);
        dialogo.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialogo.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        imgV = dialogo.findViewById(R.id.uploadImage);

        //Del diálogo creado se obtienen los elementos necesarios para la validación
        //errorMsg = dialogo.findViewById(R.id.tvdialogmsg);
        //etNombre = dialogo.findViewById(R.id.etname);
        //etPhone = dialogo.findViewById(R.id.etphone);

        //Se sobreescribe la función del botón positivo para evitar que se cierre si no se cumple la validación
        dialogo.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subir();
            }
        });
    }

    public void ElegirImagen(View v){
        Intent intentImg = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intentImg.setType("image/");
        startActivityForResult(intentImg.createChooser(intentImg,"Seleccione la Aplicación"),10);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==RESULT_OK){
            path = data.getData();
            Toast.makeText(this,path.toString(),Toast.LENGTH_LONG).show();
            imgV.setImageURI(path);

        }
    }

    public void Subir(){
        //https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934   robado de aqui XD
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
        ref.putFile(path)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(IndexActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(IndexActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                });
    }
}