package com.example.leo.bva;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class activityDatosJugador extends AppCompatActivity {

    ProyectoFinalDB accesoBaseDatos;
    SQLiteDatabase baseDatos;
    TextView Nombre,Edad,Posicion,Peso,Altura,Mail,Direccion;
    Button Borrar,Modificar,volver;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_datos_jugador);
        Bundle datos = this.getIntent().getExtras();
          id = datos.getString("id");
        AbrirBaseDatos();
        Jugador MiJugador = TraerUnJugador(id);
        ObtenerReferencias();
        Nombre.setText("Nombre: " + MiJugador.Nombre.toString());
        Edad.setText("Edad: " + MiJugador.Edad.toString());
        Posicion.setText("Posicion: " + MiJugador.Posicion.toString());
        Peso.setText("Peso: " + MiJugador.Peso.toString());
        Altura.setText("Altura: " + MiJugador.Altura.toString());
        Mail.setText("Mail: " + MiJugador.Mail.toString());
        Direccion.setText("Direccion: " + MiJugador.Direccion.toString());

        Borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BorrarJugador(id);
            }
        });

        Modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent PasarAct = new Intent(activityDatosJugador.this,ActivityModifcarJug.class);
                Bundle datos = new Bundle();
                datos.putString("id",id);
                PasarAct.putExtras(datos);
                startActivity(PasarAct);
            }
        });
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Volver= new Intent(activityDatosJugador.this,ActivityEquipo.class);
                startActivity(Volver);
            }
        });

    }

    private void BorrarJugador(String idjug)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirmar");

        builder.setMessage("¿Desea borrar este jugador?");
        final String ID = idjug;
        builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {


                baseDatos.execSQL("DELETE FROM Jugadores WHERE rowid=?", new String[] {ID});
                Log.i("Diálogos", "Jugador borrado");
                dialog.cancel();

                Intent Volver = new Intent(activityDatosJugador.this,ActivityEquipo.class);
                startActivity(Volver);
            }


        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Log.i("Diálogos", " Accion cancelada");
                dialog.cancel();

            }

        });

        builder.create();
        builder.show();


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
        Jugador MiJugador = new Jugador();

        Log.d("SQLITE", "Ejecuto la lectura de todos");
        Cursor MisRegistros = this.baseDatos.rawQuery("select nombre,edad,altura,posicion,mail,direccion,peso,rowid from Jugadores WHERE rowid=?", new String[] {id});
        Log.d("SQLITE", "Verifico si trajo registros");
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
    private void ObtenerReferencias()
    {
        Nombre=(TextView) findViewById(R.id.txtVsAZO);
        Altura=(TextView) findViewById(R.id.txtAltura);
        Edad=(TextView) findViewById(R.id.txtEdad);
        Posicion=(TextView) findViewById(R.id.txtFechazo);
        Peso=(TextView) findViewById(R.id.txtPeso);
        Mail = (TextView) findViewById(R.id.txtMail);
        Borrar=(Button) findViewById(R.id.btnBorrar);
        Modificar=(Button) findViewById(R.id.btnModificar);
        Direccion=(TextView) findViewById(R.id.txtDireccion);
        volver=(Button) findViewById(R.id.button7);
    }
}
