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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class configurarPartido extends AppCompatActivity {

    EditText edtVs;

    Button EmpezarDoparti,Convocar;
    ProyectoFinalDB accesoBaseDatos;
    SQLiteDatabase baseDatos;
    ListView lista1,lista2;
    ArrayList<Jugador> ListaNombreJugadores = new ArrayList<Jugador>();
    ArrayList<String> ListaConvocados = new ArrayList<String>();
    ArrayList<String> NombreConvocados = new ArrayList<>();
    ArrayAdapter<Jugador> localArrayAdapter1;
    Jugador jugadorSeleccionado=new Jugador();
    Integer IdPartido=0;
    DatePicker dpFecha;
    RadioButton rdbLocal,rdbVisita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_partido);
        ObtenerReferencias();

        EmpezarDoparti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Integer Dia = dpFecha.getDayOfMonth();
                Integer Mes = dpFecha.getMonth();

                Integer Año = dpFecha.getYear();


                String Condicion = "";
                if(rdbVisita.isChecked())
                {
                    Condicion="Visitante";
                }
                if(rdbLocal.isChecked())
                {
                    Condicion="Local";
                }
                if(Condicion!="") {
                    CrearPartido(edtVs.getText().toString(), Condicion, Año,Mes,Dia);
                    IdPartido = RecibirIdPartido();

                    Bundle bundle = new Bundle();

                    Intent intent = new Intent(configurarPartido.this, activityEmpezarPartido.class);

                    bundle.putStringArrayList("Convocados", ListaConvocados);
                    bundle.putInt("IdPartido", IdPartido);
                    bundle.putString("Vs", edtVs.getText().toString());
                    bundle.putString("Condicion",Condicion);
                    intent.putExtras(bundle);


                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(configurarPartido.this, "Seleccione local o visitante", Toast.LENGTH_SHORT).show();
                }
            }
        });
        AbrirBaseDatos();
        ListaNombreJugadores = TraerTodosJugadores();
        ArrayAdapter<Jugador> localArrayAdapter = new ArrayAdapter(this,  android.R.layout.simple_list_item_1, ListaNombreJugadores);
        this.lista1.setAdapter(localArrayAdapter);
        lista1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(view.isEnabled()) {
                    jugadorSeleccionado = ListaNombreJugadores.get(position);
                    ListaConvocados.add(jugadorSeleccionado.id);
                    view.setEnabled(false);
                }



            }
        });





    }

    private void ObtenerReferencias()
    {
        edtVs= (EditText) findViewById(R.id.edtVs);
        EmpezarDoparti= (Button) findViewById(R.id.button8);
        lista1=(ListView) findViewById(R.id.list1);
        dpFecha=(DatePicker) findViewById(R.id.datePicker);
        rdbLocal=(RadioButton) findViewById(R.id.rdbLocal);
        rdbVisita=(RadioButton) findViewById(R.id.rdbVisitante);
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
    private void CrearPartido(String VS,String Condicion, Integer año,Integer mes,Integer dia){
        if (AbrirBaseDatos() == true)
        {
            Log.d("SQLite", "Creo a mi partido");
            ContentValues localContentValues = new ContentValues();
            localContentValues.put("Vs", VS);
            localContentValues.put("Condicion",Condicion);
            localContentValues.put("Año", año.toString());
            localContentValues.put("Mes", mes.toString());
            localContentValues.put("Dia", dia.toString());
            this.baseDatos.insert("Partidos", null, localContentValues);


        }

    }

    private Integer RecibirIdPartido(){
        Integer idPartido=0;
        Cursor Registros=this.baseDatos.rawQuery("SELECT max(IdPartido) FROM Partidos",null);

        if(Registros.moveToFirst()){
            idPartido=Registros.getInt(0);
        }
        return  idPartido;
    }

    private ArrayList<Jugador> TraerTodosJugadores()
    {
        ArrayList localArrayList = new ArrayList();
        Log.d("SQLITE", "Ejecuto la lectura de todos");
        Cursor MisRegistros = this.baseDatos.rawQuery("select nombre,edad,altura,posicion,mail,direccion,peso,rowid from Jugadores", null);
        Log.d("SQLITE", "Verifico si trajo registros");
        if (MisRegistros.moveToFirst())
            do
            {
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
                localArrayList.add(MiJugador);
            }
            while (MisRegistros.moveToNext() == true);
        else
        {
            Toast.makeText(getApplicationContext(), "Null en mis registros", Toast.LENGTH_LONG).show();
        }
        return localArrayList;
    }

}

