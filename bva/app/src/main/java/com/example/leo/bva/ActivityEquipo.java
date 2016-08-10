package com.example.leo.bva;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityEquipo extends AppCompatActivity {

    ListView ListaMostrar;
    ArrayList<Jugador> ListaNombreJugadores = new ArrayList<Jugador>();
    ProyectoFinalDB accesoBaseDatos;
    SQLiteDatabase baseDatos;
    Button btnAgregar, volver;
    ArrayList<ListaJugador> ListarJugadores = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_equipo);
        Log.d("SQL", "OBTENER REFERENCIAS");
        ObtenerReferencias();
        AbrirBaseDatos();
        Log.d("SQL", "CARGAR NOMBRE EN LISTA");
        ListaNombreJugadores = TraerTodosJugadores();
        // ArrayAdapter<Jugador> localArrayAdapter = new ArrayAdapter(this,  android.R.layout.simple_list_item_1, ListaNombreJugadores);
        //this.ListaMostrar.setAdapter(localArrayAdapter);

        jugadoresView();

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent PasarAct = new Intent(ActivityEquipo.this, ActivityAgregarJugador.class);
                startActivity(PasarAct);

            }
        });

        ListaMostrar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent PasarAct = new Intent(ActivityEquipo.this, activityDatosJugador.class);
                Jugador jugadorSeleccionado = ListaNombreJugadores.get(i);
                Bundle bundle = new Bundle();
                bundle.putString("id", jugadorSeleccionado.id);
                PasarAct.putExtras(bundle);
                startActivity(PasarAct);
            }
        });
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Volver = new Intent(ActivityEquipo.this, MainActivity.class);
                startActivity(Volver);
            }
        });


    }


    private boolean AbrirBaseDatos() {

        Boolean BaseAbierta = false;
        Log.d("SQLite", "Inicializo el accesor de la base dandole por nombre el nombre de la base en el segundo parametro");
        accesoBaseDatos = new ProyectoFinalDB(this, "BaseBVA", null, 1);

        Log.d("SQLite", "Abro el modo escritura");
        baseDatos = accesoBaseDatos.getWritableDatabase();

        Log.d("SQLite", "Verifico que se abrio la base");

        if (baseDatos != null) {
            BaseAbierta = true;
            Log.d("SQLite", "Base Datos abierta");
        }
        return BaseAbierta;
    }

    private ArrayList<Jugador> TraerTodosJugadores() {
        ArrayList localArrayList = new ArrayList();
        Log.d("SQLITE", "Ejecuto la lectura de todos");
        Cursor MisRegistros = this.baseDatos.rawQuery("select nombre,edad,altura,posicion,mail,direccion,peso,rowid from Jugadores", null);
        Log.d("SQLITE", "Verifico si trajo registros");
        if (MisRegistros.moveToFirst())
            do {
                Jugador MiJugador = new Jugador();
                Log.d("sql", "Cargarjugador provisorio");
                MiJugador.Nombre = MisRegistros.getString(0);
                MiJugador.Edad = MisRegistros.getString(1);
                MiJugador.Altura = MisRegistros.getString(2);
                MiJugador.Posicion = MisRegistros.getString(3);
                MiJugador.Mail = MisRegistros.getString(4);
                MiJugador.Direccion = MisRegistros.getString(5);
                MiJugador.Peso = MisRegistros.getString(6);
                MiJugador.id = MisRegistros.getString(7);

                ListarJugadores.add((new ListaJugador(MiJugador.Nombre.toString(), MiJugador.Posicion.toString(), R.drawable.pelota)));
                localArrayList.add(MiJugador);
            }
            while (MisRegistros.moveToNext() == true);
        else {
            Toast.makeText(getApplicationContext(), "Null en mis registros", Toast.LENGTH_LONG).show();
        }
        return localArrayList;
    }

    private void ObtenerReferencias() {
        this.btnAgregar = ((Button) findViewById(R.id.angry_btn));
        this.ListaMostrar = ((ListView) findViewById(R.id.lstListaJug));
        volver = (Button) findViewById(R.id.button90);

    }


    public void jugadoresView() {

        ArrayAdapter<ListaJugador> adapter = new MyListAdapter();
        ListaMostrar.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<ListaJugador> {

        public MyListAdapter() {
            super(ActivityEquipo.this, R.layout.itemview, ListarJugadores);
        }

        public View getView(int Position, View convertView, ViewGroup parent) {


            View itemView = convertView;

            if(itemView==null) {
                itemView = getLayoutInflater().inflate(R.layout.itemview, parent, false);

               ListaJugador MiJugador = ListarJugadores.get(Position);
                ImageView imagen = (ImageView) itemView.findViewById(R.id.imageViewPelota);

                imagen.setImageResource(MiJugador.getPelota());

                TextView Nombre = (TextView) itemView.findViewById(R.id.txtNombrazo);
                String Name = MiJugador.getNombre();
                Nombre.setText(MiJugador.getNombre());

                TextView Posicione = (TextView) itemView.findViewById(R.id.txtPosicionazo);
                Posicione.setText(MiJugador.getPosicion());

            }
            return itemView;

        }
    }
}