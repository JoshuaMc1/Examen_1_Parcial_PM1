package com.example.examen_1_parcial_pm1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.examen_1_parcial_pm1.clases.Contactos;
import com.example.examen_1_parcial_pm1.clases.SQLiteConexion;
import com.example.examen_1_parcial_pm1.clases.Transacciones;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ActivityLista extends AppCompatActivity {
    SQLiteConexion conexion;
    ArrayList<Contactos> listaContactos;
    ArrayList<String> argContactos;
    Contactos con;

    Button btnAtras;
    SearchView buscar;
    ListView lista;
    Button btnCompartir;
    Button btnVerImagen;
    Button btnEliminar;
    Button btnActualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        conexion = new SQLiteConexion(this, Transacciones.nameDatabase, null, 1);
        init();
        btnAtras.setOnClickListener(this::onClickAtras);
        cargarLista();
        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, argContactos);
        lista.setAdapter(adp);
        lista.setOnItemClickListener(this::onClickLista);
    }

    private void onClickLista(AdapterView<?> adapterView, View view, int i, long l) {
        AlertDialog dialogo = new AlertDialog.Builder(ActivityLista.this).setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent llamar = new Intent(getApplicationContext(), ActivityLlamar.class);
//                llamar.putExtra("numeroTelefono", listaContactos.get(i).getTelefono());
//                startActivity(llamar);
                MainActivity.message(getApplicationContext(),"Numero: "+ listaContactos.get(i).getTelefono());
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setTitle("Acción").setMessage("¿Desea llamar a "+listaContactos.get(i).getNombre()+"?").create();
        dialogo.show();

        btnVerImagen.setOnClickListener(v -> {
            try {
                Intent verVentana = new Intent(v.getContext(), ActivityVerImagen.class);
                Bundle bnd = new Bundle();
                bnd.putString("titulo", listaContactos.get(i).getNombre());
                bnd.putByteArray("foto", listaContactos.get(i).getImagen());
                verVentana.putExtras(bnd);
                startActivity(verVentana);
            }catch (Exception ex){
                MainActivity.message(getApplicationContext(), ex.getMessage());
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    conexion = new SQLiteConexion(ActivityLista.this.getApplicationContext(), Transacciones.nameDatabase, null, 1);
                    SQLiteDatabase db = conexion.getWritableDatabase();
                    AlertDialog dialogo1 = new AlertDialog.Builder(ActivityLista.this).setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int result = db.delete(Transacciones.tablaContactos, Transacciones.id + "=" + listaContactos.get(i).getId(), null);
                            MainActivity.message(getApplicationContext(), "Registro eliminar exitosamente");
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setTitle("Eliminar").setMessage("¿Esta seguro que desea eliminar a: " + listaContactos.get(i).getNombre() + "?").create();
                    dialogo1.show();
                } catch (Exception ex) {
                    MainActivity.message(ActivityLista.this.getApplicationContext(), ex.getMessage());
                }
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void onClickAtras(View view) {
        Intent atras = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(atras);
    }

    protected void init(){
        buscar = (SearchView) findViewById(R.id.txtBuscar);
        lista = (ListView) findViewById(R.id.txtLista);
        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        btnCompartir = (Button) findViewById(R.id.btnCompartir);
        btnAtras = (Button) findViewById(R.id.btnAtras);
        btnVerImagen = (Button) findViewById(R.id.btnVerImagen);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
    }

    protected void cargarLista(){
        SQLiteDatabase db = conexion.getReadableDatabase();
        listaContactos = new ArrayList<Contactos>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.tablaContactos, null);
        while(cursor.moveToNext()) {
            con = new Contactos();
            con.setId(cursor.getInt(0));
            con.setPais(cursor.getString(1));
            con.setNombre(cursor.getString(2));
            con.setTelefono(cursor.getString(3));
            con.setNota(cursor.getString(4));
            con.setImagen(cursor.getBlob(5));
            listaContactos.add(con);
        }
        cursor.close();
        fillList();
    }

    private void fillList() {
        argContactos = new ArrayList<String>();
        for(int i = 0; i<listaContactos.size(); i ++) {
            argContactos.add(listaContactos.get(i).getId()  + " | " + listaContactos.get(i).getNombre() + " | " + listaContactos.get(i).getTelefono());
        }
    }
}