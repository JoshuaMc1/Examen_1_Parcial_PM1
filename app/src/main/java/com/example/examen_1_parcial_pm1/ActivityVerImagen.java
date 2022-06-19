package com.example.examen_1_parcial_pm1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityVerImagen extends AppCompatActivity {
    Button btnAtras;
    ImageView imagen;
    TextView titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_imagen);
        init();

        btnAtras.setOnClickListener(this::onClickAtras);
        try {
            titulo.setText(getIntent().getExtras().getString("titulo"));
            byte[] img = getIntent().getByteArrayExtra("foto");
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(img,0,img.length);
            imagen.setImageBitmap(compressedBitmap);
        }catch (Exception ex){
            MainActivity.message(getApplicationContext(),ex.getMessage());
        }
    }

    private void onClickAtras(View view) {
        Intent atras = new Intent(getApplicationContext(), ActivityLista.class);
        startActivity(atras);
    }


    protected void init(){
        btnAtras = (Button) findViewById(R.id.btnIAtras);
        imagen = (ImageView) findViewById(R.id.imgProfile);
        titulo = (TextView) findViewById(R.id.txtTitulo);
    }
}