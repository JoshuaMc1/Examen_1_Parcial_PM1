package com.example.examen_1_parcial_pm1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.examen_1_parcial_pm1.clases.SQLiteConexion;
import com.example.examen_1_parcial_pm1.clases.Transacciones;

public class ActivityAgregar_Paises extends AppCompatActivity {
    EditText txtPPais;
    Button btnPGuardar;
    Button btnPatras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_paises);
        init();

        btnPatras.setOnClickListener(this::onClickAtras);
        btnPGuardar.setOnClickListener(this::onClickGuardar);
    }

    protected void onClickAtras(View view){
        Intent atras = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(atras);
    }

    protected void onClickGuardar(View view){
        if(txtPPais.getText().toString().length() > 2){
            SQLiteConexion conexion = new SQLiteConexion(getApplicationContext(), Transacciones.nameDatabase, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Transacciones.cPais, txtPPais.getText().toString());
            Long result = db.insert(Transacciones.tablaPais,Transacciones.cId, values);
            MainActivity.message(this, "Registro guardado exitosamente");
            cleanFields();
            db.close();
        }else MainActivity.message(this, "Debe ingresar el pais");
    }

    public void cleanFields(){
        txtPPais.setText("");
    }

    private void init(){
        txtPPais = (EditText) findViewById(R.id.txtPPais);
        btnPatras = (Button) findViewById(R.id.btnPAtras);
        btnPGuardar = (Button) findViewById(R.id.btnPGuardar);
    }
}