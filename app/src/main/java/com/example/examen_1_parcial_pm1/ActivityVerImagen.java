package com.example.examen_1_parcial_pm1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.examen_1_parcial_pm1.clases.SQLiteConexion;
import com.example.examen_1_parcial_pm1.clases.Transacciones;

import java.io.ByteArrayInputStream;

public class ActivityVerImagen extends AppCompatActivity {
    Button btnAtras;
    ImageView imagen;
    TextView titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_imagen);

        init();
        asignarDatos();
    }

    protected void init(){
        btnAtras = (Button) findViewById(R.id.btnIAtras);
        imagen = (ImageView) findViewById(R.id.imgProfile);
        titulo = (TextView) findViewById(R.id.txtTitulo);
    }

    protected void asignarDatos(){
        try {
            String idContact = getIntent().getExtras().getString("id");
            byte[] datos = buscarFoto(idContact);
            ByteArrayInputStream imageStream = new ByteArrayInputStream(datos);
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            imagen.setImageBitmap(theImage);
        }catch (Exception ex){
            MainActivity.message(getApplicationContext(),ex.getMessage());
        }
    }

    protected byte[] buscarFoto(String id){
        byte[] data = null;
        try {
            SQLiteConexion conexion = new SQLiteConexion(getApplicationContext(), Transacciones.nameDatabase, null, 1);
            SQLiteDatabase db =conexion.getWritableDatabase();
            String [] params = {Transacciones.id};
            String [] fields = {Transacciones.imagen, Transacciones.nombre};
            String WhereCondition = Transacciones.id + "="+id;
            Cursor cdata = db.query(Transacciones.tablaContactos, fields, WhereCondition,params,null,null,null);
            cdata.moveToFirst();
            if(cdata.getCount() > 0){
                data = cdata.getBlob(0);
                titulo.setText(cdata.getString(1));
            }
        }catch (Exception ex){
            MainActivity.message(getApplicationContext(),ex.getMessage());
        }
        return data;
    }
}