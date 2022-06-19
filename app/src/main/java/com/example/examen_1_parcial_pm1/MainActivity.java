package com.example.examen_1_parcial_pm1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.examen_1_parcial_pm1.clases.Contactos;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    static  final int REQUESTCODECAMARA = 100;
    static  final int REQUESTTAKEPHOTO = 101;
    Contactos contact = new Contactos();

    EditText txtNombre;
    EditText txtTelefono;
    EditText txtNota;
    Spinner spPais;
    Button btnAgregar;
    Button btnConsultar;
    Button btnActivityPaises;
    Button btnTomarFoto;
    ImageView imgFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        btnAgregar.setOnClickListener(this::onClickAgregar);
        btnTomarFoto.setOnClickListener(this::onClickTakePhoto);
    }

    protected void onClickTakePhoto(View view){
        assignPermissions();
    }

    protected void onClickAgregar(View view){
        if(!emptyField()){
            if(imgFoto.getDrawable() != null){
                agregar();
            }else message("Debe agregar una imagen");
        }else message("Hay campos vacíos");
    }

    protected void agregar(){
        try {

        }catch (Exception ex){
            message(ex.getMessage());
        }
    }

    private void assignPermissions() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.CAMERA }, REQUESTCODECAMARA);
        }else takePhoto();
    }

    private void takePhoto() {
        Intent tomarfoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(tomarfoto,REQUESTTAKEPHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUESTCODECAMARA) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) takePhoto();
            else message("Permiso denegado");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init(){
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        txtNota = (EditText) findViewById(R.id.txtNota);
        imgFoto = (ImageView)findViewById(R.id.imgFoto);
        spPais = (Spinner) findViewById(R.id.spPais);
        btnAgregar = (Button) findViewById(R.id.btnGuardarDatos);
        btnConsultar = (Button) findViewById(R.id.btnConsultarDatos);
        btnActivityPaises = (Button) findViewById(R.id.btnActivityPaises);
        btnTomarFoto = (Button) findViewById(R.id.btnTomarFoto);
    }

    private boolean emptyField(){
        if(txtNombre.getText().toString().length() < 2 ||
           txtTelefono.getText().toString().length() < 2 ||
           txtNota.getText().toString().length() < 2)return true;
        else return false;
    }

    public void message(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}