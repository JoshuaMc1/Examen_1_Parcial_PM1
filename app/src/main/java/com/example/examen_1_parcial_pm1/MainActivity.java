package com.example.examen_1_parcial_pm1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.examen_1_parcial_pm1.clases.Contactos;
import com.example.examen_1_parcial_pm1.clases.Pais;
import com.example.examen_1_parcial_pm1.clases.SQLiteConexion;
import com.example.examen_1_parcial_pm1.clases.Transacciones;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static  final int REQUESTCODECAMARA = 100;
    static  final int REQUESTTAKEPHOTO = 101;
    ArrayList<Pais> listaPaies;
    ArrayList<String> argPaises;
    Contactos contact = new Contactos();
    Pais paises;

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
        btnConsultar.setOnClickListener(this::onClickMostrarContacto);
        btnActivityPaises.setOnClickListener(this::onClickActivityPaises);
        btnConsultar.setOnClickListener(this::onClickConsulta);
    }

    private void onClickConsulta(View view) {
        Intent lista = new Intent(getApplicationContext(), ActivityLista.class);
        startActivity(lista);
    }

    private void onClickActivityPaises(View view) {
        Intent paises = new Intent(getApplicationContext(), ActivityAgregar_Paises.class);
        startActivity(paises);
    }

    protected void onClickTakePhoto(View view){
        assignPermissions();
    }

    protected void onClickAgregar(View view){
        if(!emptyField()){
            if(imgFoto.getDrawable() != null){
                agregar();
            }else message(this,"Debe agregar una imagen");
        }else message(this,"Hay campos vacíos");
    }

    protected void onClickMostrarContacto(View view){
        Intent intent = new Intent(getApplicationContext(), ActivityLista.class);
        startActivity(intent);
    }

    protected void agregar(){
        try {
            SQLiteConexion conexion = new SQLiteConexion(getApplicationContext(), Transacciones.nameDatabase, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Transacciones.pais, spPais.getSelectedItem().toString());
            values.put(Transacciones.nombre, txtNombre.getText().toString());
            values.put(Transacciones.telefono, txtTelefono.getText().toString());
            values.put(Transacciones.nota, txtNota.getText().toString());
            values.put(Transacciones.imagen, contact.getImagen());
            Long result = db.insert(Transacciones.tablaContactos, Transacciones.id, values);
            message(this,"Datos guardados exitosamente ");
            db.close();
            cleanFields();
        }catch (Exception ex){
            message(this,ex.getMessage());
        }
    }

    public void cleanFields(){
        txtNombre.setText("");
        txtNota.setText("");
        txtTelefono.setText("");
        spPais.setSelection(0);
        imgFoto.setImageDrawable(null);
    }

    private void assignPermissions() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.CAMERA }, REQUESTCODECAMARA);
        }else takePhoto();
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUESTTAKEPHOTO);
        }
    }

        @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUESTCODECAMARA) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) takePhoto();
            else message(this,"Permiso denegado");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTTAKEPHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgFoto.setImageBitmap(imageBitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream(20480);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 0 , baos);
            byte[] blob = baos.toByteArray();
            contact.setImagen(blob);
        }
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
        try {
            cargarSpinner();
            ArrayAdapter<CharSequence> adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, argPaises);
            spPais.setAdapter(adp);
        }catch (Exception ex){
            message(this, ex.getMessage());
        }
    }

    private boolean emptyField(){
        if(txtNombre.getText().toString().length() < 2 ||
           txtTelefono.getText().toString().length() < 2 ||
           txtNota.getText().toString().length() < 2)return true;
        else return false;
    }

    public static void message(Context c, String msg){
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }

    protected void cargarSpinner(){
        try {
            SQLiteConexion conexion = new SQLiteConexion(getApplicationContext(), Transacciones.nameDatabase, null, 1);
            SQLiteDatabase db = conexion.getReadableDatabase();
            listaPaies = new ArrayList<Pais>();
            Cursor cursor = db.rawQuery("SELECT * FROM "+Transacciones.tablaPais, null);

            while(cursor.moveToNext()){
                paises = new Pais();
                paises.setId(cursor.getInt(0));
                paises.setNombrePais(cursor.getString(1));
                listaPaies.add(paises);
            }
            fillList();
        }catch (Exception ex){
            message(this, ex.getMessage());
        }
    }

    protected void fillList() {
        argPaises = new ArrayList<String>();
        for(int i = 0; i<listaPaies.size(); i ++){
            argPaises.add(listaPaies.get(i).getNombrePais());
        }
    }
}