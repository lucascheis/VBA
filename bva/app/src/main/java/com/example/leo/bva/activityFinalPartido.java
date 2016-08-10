package com.example.leo.bva;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class activityFinalPartido extends AppCompatActivity {

    Integer puntos,RebotesOff,RebotesDeff,Tapones,Faltas,Perdidas,Asistencias,Robos,TLErrados,DoblesErrados,TriplesErrados,TLMetidos,DoblesMetidos,TriplesMetidos;
    TextView porcTl,txtPuntos,txtAsitencias,txtRebOff,txtRebDef,txtRobos,txtTapones,txtPerdidas,txtFaltas,txtPorDobles,txtPorTriples;
    ProyectoFinalDB accesoBaseDatos;
    SQLiteDatabase baseDatos;
    View vwAnterior;
    TextView txtTableroLocal;
    Integer puntosTotalesLocal;
    Button btnVolver;
    ListView ListaMostrar;
    EditText puntosTotalesVisitante;
    ArrayList<String> ConvocadosString;
    Integer IdPartido=-1;
    ArrayList<Jugador> ListaNombreJugadores = new ArrayList<Jugador>();
    Jugador jugadorSeleccionado;
    String Condicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_final_partido);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ObtenerReferencias();
        puntos= bundle.getInt("Puntos");
        puntosTotalesLocal=puntos;
        Condicion= bundle.getString("Condicion");
        RebotesOff = bundle.getInt("RebotesOff");
        RebotesDeff=bundle.getInt("RebotesDef");
        Tapones = bundle.getInt("Tapones");
        Faltas=bundle.getInt("Faltas");
        Perdidas=bundle.getInt("Perdidas");
        Asistencias=bundle.getInt("Asistencias");
        Robos=bundle.getInt("Robos");
        TLErrados=bundle.getInt("TLErrados");
        DoblesErrados=bundle.getInt("DoblesErrados");
        TriplesErrados=bundle.getInt("TriplesErrados");
        TLMetidos=bundle.getInt("TLMetidos");
        DoblesMetidos=bundle.getInt("DoblesMetidos");
        TriplesMetidos=bundle.getInt("TriplesMetidos");
        IdPartido= bundle.getInt("IdPartido");
        ConvocadosString = bundle.getStringArrayList("Convocados");



        Integer TLTirados = TLErrados+TLMetidos;
        Double tltirados = Double.parseDouble(TLTirados.toString());
        Double tlmetidos = Double.parseDouble(TLMetidos.toString());
        Double porTL = (tlmetidos*100)/tltirados;
        Integer DoblesTirados=DoblesErrados+DoblesMetidos;
        Double doblestirados=Double.parseDouble(DoblesTirados.toString());
        Double doblesmetidos = Double.parseDouble(DoblesMetidos.toString());
        Double porDoble=(doblesmetidos*100)/doblestirados;
        Integer TriplesTirados=TriplesErrados+TLMetidos;
        Double triplestirados=Double.parseDouble(TriplesTirados.toString());
        Double triplesmetidos = Double.parseDouble(TriplesMetidos.toString());
        Double porTriples=(triplesmetidos*100)/triplestirados;



        porcTl.setText("TL: %"+ porTL.toString());
        txtPuntos.setText("Puntos: " + puntos.toString());
        txtPorTriples.setText("3PT: %" + porTriples.toString());
        txtPorDobles.setText("2PT: %" + porDoble.toString());
        txtAsitencias.setText("Asistencias: "+Asistencias.toString());
        txtTapones.setText("Tapones: "+Tapones.toString());
        txtRebOff.setText("Rebotes Off: "+RebotesOff.toString());
        txtRebDef.setText("Rebotes Def: "+RebotesDeff.toString());
        txtRobos.setText("Robos: "+Robos.toString());
        txtPerdidas.setText("Perdidas: "+Perdidas.toString());
        txtFaltas.setText("Faltas: "+Faltas.toString());
        txtTableroLocal.setText(puntosTotalesLocal.toString());

        btnVolver.setOnClickListener(btnVolver_Click);

        Log.d("SQL", "CARGAR NOMBRE EN LISTA");
        ListaNombreJugadores = TraerConvocados(ConvocadosString);
        ArrayAdapter<Jugador> localArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, ListaNombreJugadores);
        this.ListaMostrar.setAdapter(localArrayAdapter);


        ListaMostrar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jugadorSeleccionado = ListaNombreJugadores.get(position);
                ListaMostrar.setItemChecked(position, true);
                if (vwAnterior!=null){
                    vwAnterior.setBackgroundColor(Color.TRANSPARENT);
                }
                view.setBackgroundColor(Color.LTGRAY);
                vwAnterior = view;
                EstadisticasXJugador MisEstadisticas=TraerEstadisticasJugador(IdPartido,jugadorSeleccionado.id);

                Integer TLTirados = MisEstadisticas.TirosLibErrados+MisEstadisticas.TirosLibMetidos;
                Double tltirados = Double.parseDouble(TLTirados.toString());
                Double tlmetidos = Double.parseDouble(MisEstadisticas.TirosLibMetidos.toString());
                Double porTL = (tlmetidos*100)/tltirados;
                Integer DoblesTirados=MisEstadisticas.DoblesErrados+MisEstadisticas.DoblesMetidos;
                Double doblestirados=Double.parseDouble(DoblesTirados.toString());
                Double doblesmetidos = Double.parseDouble(MisEstadisticas.DoblesMetidos.toString());
                Double porDoble=(doblesmetidos*100)/doblestirados;
                Integer TriplesTirados=MisEstadisticas.TriplesErrados+MisEstadisticas.TriplesMetidos;
                Double triplestirados=Double.parseDouble(TriplesTirados.toString());
                Double triplesmetidos = Double.parseDouble(MisEstadisticas.TriplesMetidos.toString());
                Double porTriples=(triplesmetidos*100)/triplestirados;
                Integer PuntosJugador= MisEstadisticas.Puntos;



                porcTl.setText("TL: %"+ porTL.toString());
                txtPuntos.setText("Puntos: " + PuntosJugador.toString());
                txtPorTriples.setText("3PT: %" + porTriples.toString());
                txtPorDobles.setText("2PT: %" + porDoble.toString());
                txtAsitencias.setText("Asistencias: "+MisEstadisticas.Asistencias.toString());
                txtTapones.setText("Tapones: "+MisEstadisticas.Tapones.toString());
                txtRebOff.setText("Rebotes Off: "+MisEstadisticas.RebOff.toString());
                txtRebDef.setText("Rebotes Def: "+MisEstadisticas.RebDef.toString());
                txtRobos.setText("Robos: "+MisEstadisticas.Robos.toString());
                txtPerdidas.setText("Perdidas: "+MisEstadisticas.Perdidas.toString());
                txtFaltas.setText("Faltas: "+MisEstadisticas.Faltas.toString());
            }
        });


    }
    private View.OnClickListener btnVolver_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            GuardarPuntajes(puntosTotalesLocal.toString(),puntosTotalesVisitante.getText().toString(),IdPartido,Condicion);

            Intent intent = new Intent(activityFinalPartido.this,MainActivity.class);
            startActivity(intent);

        }
    };
    private void GuardarPuntajes(String PuntosLocal, String PuntosVisitante,Integer IdPartidin,String Condicion){

        AbrirBaseDatos();
        ContentValues valores = new ContentValues();

        if(Condicion=="Visitante") {
            valores.put("PuntosLocal", PuntosLocal);
            valores.put("PuntosVisitante", PuntosVisitante);
        }
        else
        {
            valores.put("PuntosLocal", PuntosVisitante);
            valores.put("PuntosVisitante", PuntosLocal);
        }

        this.baseDatos.update("Partidos",valores,"idPartido=?",new String[] {IdPartidin.toString()});

    }

    private EstadisticasXJugador TraerEstadisticasJugador(Integer idPartido,String idJugador) {

        EstadisticasXJugador MisEstadisticas = new EstadisticasXJugador();
        if (AbrirBaseDatos() == true) {

            Log.d("SQLITE", "Ejecuto la lectura de todos");
            Cursor MisRegistros = this.baseDatos.rawQuery("SELECT Puntos,Asistencias,Robos,Faltas,Tapones,RebOff,RebDef,Perdidas,DoblesErrados,DoblesMetidos,TirosLibErrados,TirosLibMetidos,TriplesErrados,TriplesMetidos from EstadisticasXJugador WHERE IdJugador =" + idJugador + " AND IdPartido=" + idPartido.toString(), null);
            Log.d("SQLITE", "Verifico si trajo registros");
            if (MisRegistros.moveToFirst()) {
                do {
                    Log.d("sql", "Cargarjugador provisorio");
                    MisEstadisticas.Puntos = MisRegistros.getInt(0);
                    MisEstadisticas.Asistencias = MisRegistros.getInt(1);
                    MisEstadisticas.Robos = MisRegistros.getInt(2);
                    MisEstadisticas.Faltas = MisRegistros.getInt(3);
                    MisEstadisticas.Tapones = MisRegistros.getInt(4);
                    MisEstadisticas.RebOff = MisRegistros.getInt(5);
                    MisEstadisticas.RebDef = MisRegistros.getInt(6);
                    MisEstadisticas.Perdidas = MisRegistros.getInt(7);
                    MisEstadisticas.DoblesErrados=MisRegistros.getInt(8);
                    MisEstadisticas.DoblesMetidos=MisRegistros.getInt(9);
                    MisEstadisticas.TirosLibErrados=MisRegistros.getInt(10);
                    MisEstadisticas.TirosLibMetidos=MisRegistros.getInt(11);
                    MisEstadisticas.TriplesErrados=MisRegistros.getInt(12);
                    MisEstadisticas.TriplesMetidos=MisRegistros.getInt(13);

                }
                while (MisRegistros.moveToNext() == true);

            }

        }
        return MisEstadisticas;

    }



    private void ObtenerReferencias()
    {
        puntosTotalesVisitante= (EditText) findViewById(R.id.edtPuntosVisi);
        btnVolver = (Button) findViewById(R.id.btnVolver);
        porcTl=(TextView) findViewById(R.id.txtPorTL);
        txtAsitencias=(TextView) findViewById(R.id.txtAsistencias);
        txtTapones=(TextView) findViewById(R.id.txtTapones);
        txtRebDef=(TextView) findViewById(R.id.txtRebDef);
        txtRebOff=(TextView) findViewById(R.id.txtRebOff);
        txtPerdidas=(TextView) findViewById(R.id.txtPerdidas);
        txtFaltas=(TextView) findViewById(R.id.txtFaltas);
        txtRobos=(TextView) findViewById(R.id.txtRobos);
        txtPorDobles=(TextView) findViewById(R.id.txtPorDoble);
        txtPorTriples=(TextView) findViewById(R.id.txtPorTriple);
        txtPuntos=(TextView) findViewById(R.id.txtPuntos);
        ListaMostrar=(ListView) findViewById(R.id.listView);
        txtTableroLocal=(TextView) findViewById(R.id.txtTableroLocal);
    }
    private ArrayList<Jugador> TraerConvocados(ArrayList<String> ConvocadosString) {

        ArrayList<Jugador> localArrayList = new ArrayList<Jugador>();

        if(AbrirBaseDatos()==true) {
            Log.d("SQLITE", "Ejecuto la lectura de todos");

            for (int i = 0; i < ConvocadosString.size(); i++) {

                Cursor MisRegistros = this.baseDatos.rawQuery("select nombre,edad,altura,posicion,mail,direccion,peso,IdJugador from Jugadores WHERE IdJugador=" + ConvocadosString.get(i), null);
                Log.d("SQLITE", "Verifico si trajo registros");
                if (MisRegistros.moveToFirst()) {

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
            }

        }
        return localArrayList;

    }
    private ArrayList<Jugador> TraerTodosJugadores() {
        ArrayList localArrayList = new ArrayList();

        if (AbrirBaseDatos() == true) {
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
                    localArrayList.add(MiJugador);
                }
                while (MisRegistros.moveToNext() == true);
            else {
                Toast.makeText(getApplicationContext(), "Null en mis registros", Toast.LENGTH_LONG).show();
            }
        }
        return localArrayList;

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
}
