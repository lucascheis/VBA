package com.example.leo.bva;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ActivityModifcarJug extends AppCompatActivity {

    EditText Nombre,Peso,Edad,Altura,Direccion,Mail;
    Spinner Posicion;
    String strPosicion;
    ArrayAdapter<CharSequence> adapter;
    ProyectoFinalDB accesoBaseDatos;
    SQLiteDatabase baseDatos;
    Button Modificar,volver;
    Jugador MiJugador=new Jugador();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_modifcar_jug);
        Bundle datos = getIntent().getExtras();

        String iD = datos.getString("id");
        ObtenerReferencias();
        MiJugador = TraerUnJugador(iD);

        Nombre.setText(MiJugador.Nombre.toString());
        Peso.setText(MiJugador.Peso.toString());
        Edad.setText(MiJugador.Edad.toString());
        Altura.setText(MiJugador.Altura.toString());
        Direccion.setText(MiJugador.Direccion.toString());
        Mail.setText(MiJugador.Mail.toString());
        CargarSpinner(MiJugador.Posicion);


        Modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strPosicion=CargarPosicion(Posicion.getSelectedItemPosition());
                ModificarJugador(Nombre.getText().toString(),Peso.getText().toString(),Edad.getText().toString(),Altura.getText().toString(),Direccion.getText().toString(),Mail.getText().toString(),strPosicion,MiJugador.id);
                Intent intent = new Intent(ActivityModifcarJug.this,ActivityEquipo.class);
                startActivity(intent);
            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Volver= new Intent(ActivityModifcarJug.this,ActivityEquipo.class);
                startActivity(Volver);
            }
        });

    }

    private void ModificarJugador(String nom, String peso, String edad, String altura, String direc, String mail, String pos,String id)
    {
        if(AbrirBaseDatos()) {
        ContentValues valores = new ContentValues();
        valores.put("Nombre", nom);
        valores.put("Peso", peso);
        valores.put("Edad", edad);
        valores.put("Altura", altura);
        valores.put("Direccion", direc);
        valores.put("Mail", mail);
        valores.put("Posicion", pos);
            this.baseDatos.update("Jugadores",valores,"rowid=?",new String[] {id});
    }

    }
    private void ObtenerReferencias()
    {
        Nombre=(EditText) findViewById(R.id.txtVsAZO);
        Edad=(EditText) findViewById(R.id.txtEdad);
        Peso=(EditText) findViewById(R.id.txtPeso);
        Altura=(EditText) findViewById(R.id.txtAltura);
        Direccion=(EditText) findViewById(R.id.txtDireccion);
        Mail=(EditText) findViewById(R.id.txtMail);
        Posicion=(Spinner) findViewById(R.id.spinner2);
        Modificar=(Button) findViewById(R.id.btnModificar);
        volver=(Button) findViewById(R.id.button5);

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

    private Jugador TraerUnJugador(String id)
    {
        AbrirBaseDatos();

        Jugador MiJugador = new Jugador();
        Log.d("SQLITE", "Ejecuto la lectura de todos");
        String[] args = new String[] {id};
        Cursor MisRegistros = baseDatos.rawQuery("SELECT nombre,edad,altura,posicion,mail,direccion,peso,rowid FROM Jugadores WHERE rowid=?", args);
        //Log.d("SQLITE", "Verifico si trajo registros");
        if (MisRegistros.moveToFirst()) {

           Log.d("sql", "Cargarjugador provisorio");
            MiJugador.Nombre = MisRegistros.getString(0);
            MiJugador.Edad = MisRegistros.getString(1);
            MiJugador.Altura = MisRegistros.getString(2);
            MiJugador.Posicion = MisRegistros.getString(3);
            MiJugador.Mail = MisRegistros.getString(4);
            MiJugador.Direccion = MisRegistros.getString(5);
            MiJugador.Peso = MisRegistros.getString(6);
            MiJugador.id = MisRegistros.getString(7);

        }

        else
        {
            Toast.makeText(getApplicationContext(), "Null en mis registros", Toast.LENGTH_LONG).show();
        }

        return MiJugador;
    }

    private String CargarPosicion(int posicion) {
        final String[] vec = {"Base","Escolta","Alero","Ala-Pivot","Pivot"};
        String pos = vec[posicion];
        return pos;
    }
    private void CargarSpinner(String posicion)
    {
        adapter = ArrayAdapter.createFromResource(this,R.array.posiciones_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Posicion.setAdapter(this.adapter);

        switch (posicion){
            case "Base":
                Posicion.setSelection(0);
                break;
            case "Escolta":
                Posicion.setSelection(1);
                break;
            case "Alero":
                Posicion.setSelection(2);
                break;
            case "Ala-Pivot":
                Posicion.setSelection(3);
                break;
            case "Pivot":
                Posicion.setSelection(4);
                break;
        }


    }
}
