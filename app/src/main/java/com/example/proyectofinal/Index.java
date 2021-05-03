package com.example.proyectofinal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Index extends AppCompatActivity {

    private String[] nombres = {"@alma","@hector","@jair","@erubey","@rair"};
    private String[] descr = {"En la playa <3","Cenando con mis amigos","En el gimnasio","Jugando League of Legends :)","Prgramando en python"};
    private ListView lvContent;
    private ArrayList<Post> listaPosts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ActionBar actionBar = getSupportActionBar(); actionBar.hide();
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
}