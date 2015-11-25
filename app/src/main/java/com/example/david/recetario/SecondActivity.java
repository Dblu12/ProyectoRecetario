package com.example.david.recetario;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private TextView tvNombre, tvIngredientes, tvInstrucciones;
    private ImageView imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        tvNombre = (TextView) findViewById(R.id.tvNombre);
        tvIngredientes = (TextView) findViewById(R.id.tvIngredientes);
        tvInstrucciones = (TextView) findViewById(R.id.tvInstrucciones);

        imagen = (ImageView) findViewById(R.id.imageView);

        init();
    }

    private void init(){
        Log.v("URI", this.getIntent().getExtras().getString("foto"));
        tvIngredientes.setText("");
        tvNombre.setText(this.getIntent().getExtras().getString("nombre"));
        imagen.setTag(this.getIntent().getExtras().getString("nombre"));
        imagen.setImageURI(Uri.parse(this.getIntent().getExtras().getString("foto")));
        for(int cont=0; cont < this.getIntent().getExtras().getInt("totalingredientes"); cont++){
            tvIngredientes.append(this.getIntent().getExtras().getString("ingrediente"+cont)+ " ");
            tvIngredientes.append("("+this.getIntent().getExtras().getInt("cantidad"+cont)+") ");
        }
        tvInstrucciones.setText(this.getIntent().getExtras().getString("instrucciones"));
    }
}
