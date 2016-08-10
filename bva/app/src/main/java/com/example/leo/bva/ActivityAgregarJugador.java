package com.example.leo.bva;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ActivityAgregarJugador extends AppCompatActivity {

    String PosicionJugador = " ";
    ProyectoFinalDB accesoBaseDatos;
    ArrayAdapter<CharSequence> adapter;
    EditText altura;
    SQLiteDatabase baseDatos;
    Button btnEnviar,volver;
    EditText direccion;
    EditText edad;
    EditText mail;
    EditText nombre;
    EditText peso;
    Spinner posicion;
    final String[] vec = {"Base","Escolta","Alero","Ala-Pivot","Pivot"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_agregar_jugador);
        ObtenerReferencias();
        CargarSpinner();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AgregarJugador(nombre.getText().toString(), edad.getText().toString(), altura.getText().toString(), PosicionJugador, mail.getText().toString(),
                        direccion.getText().toString(), peso.getText().toString());
                Intent intent = new Intent(ActivityAgregarJugador.this,ActivityEquipo.class);
                startActivity(intent);
            }

        });

        posicion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PosicionJugador = vec[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Volver = new Intent(ActivityAgregarJugador.this,ActivityEquipo.class);
                startActivity(Volver);
            }
        });

    }

    private void CargarSpinner()
    {

        adapter = ArrayAdapter.createFromResource(this,R.array.posiciones_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        posicion.setAdapter(this.adapter);
    }

    private boolean AbrirBaseDatos()
    {
        Boolean BaseAbierta=false;
        Log.d("SQLite", "Inicializo el accesor de la base dandole por nombre el nombre de la base en el segundo parametro");
        accesoBaseDatos = new ProyectoFinalDB(this, "BaseBVA", null, 1);

        Log.d("SQLite", "Abro el modo escritura");
        baseDatos = accesoBaseDatos.getWritableDatabase();

        Log.d("SQLite", "Verifico que se abrio la base");

        if (baseDatos != null) {
            BaseAbierta=true;
            Log.d("SQLite", "Base Datos abierta");
        }
        return BaseAbierta;
    }


    private void AgregarJugador(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
    {
        if (AbrirBaseDatos() == true)
        {
            Log.d("SQLite", "Creo a mi jugador");
            ContentValues localContentValues = new ContentValues();
            localContentValues.put("nombre", paramString1);
            localContentValues.put("edad", paramString2);
            localContentValues.put("altura", paramString3);
            localContentValues.put("posicion", paramString4);
            localContentValues.put("mail", paramString5);
            localContentValues.put("direccion", paramString6);
            localContentValues.put("peso", paramString7);
            this.baseDatos.insert("Jugadores", null, localContentValues);

            //Jugador MiJugador = TraerUnJugador(paramString1,paramString6);
            //String id = MiJugador.id;

            /*if(MisRegistros.moveToFirst()) {

                Integer Id = MisRegistros.getInt(0);

                ContentValues AgregarEstadisti = new ContentValues();

                AgregarEstadisti.put("idJug", Id);
                this.baseDatos.insert("Estadisticas", null, AgregarEstadisti);
            }
            */
        }
    }

    private Jugador TraerUnJugador(String Nombre, String direccion)
    {
        Jugador MiJugador = new Jugador();

        Log.d("SQLITE", "Ejecuto la lectura de todos");
        Cursor MisRegistros = this.baseDatos.rawQuery("SELECT * from Jugadores WHERE nombre=?"+ Nombre+"AND direccion=?"+direccion,null);
        Log.d("SQLITE", "Verifico si trajo registros");
        if (MisRegistros.moveToFirst()) {

            Log.d("sql", "Cargarjugador provisorio");


            MiJugador.id = MisRegistros.getString(0);

        }


        return MiJugador;
    }



    private void ObtenerReferencias()
    {
        btnEnviar=(Button) findViewById(R.id.angry_btn);
        nombre=(EditText) findViewById(R.id.txtVsAZO);
        edad=(EditText) findViewById(R.id.txtEdad);
        altura=(EditText) findViewById(R.id.txtAltura);
        posicion=(Spinner)findViewById(R.id.spinner);
        peso=(EditText) findViewById(R.id.txtPeso);
        mail=(EditText) findViewById(R.id.txtMail);
        direccion=(EditText) findViewById(R.id.txtDireccion);
        volver=(Button) findViewById(R.id.button6);

    }
}