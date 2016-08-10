package com.example.leo.bva;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ActivityDatosPartido extends AppCompatActivity {
    ProyectoFinalDB accesoBaseDatos;
    View vwAnterior = null;
    SQLiteDatabase baseDatos;
    ArrayList<Integer> TraerJugadoresId = new ArrayList<>();
    ArrayList<Jugador> ConvocadosArray = new ArrayList<>();
    ListView lstConvocados;
    TextView porcTl,txtPuntos,txtAsitencias,txtRebs,txtRobos,txtTapones,txtPerdidas,txtFaltas,txtPorDobles,txtPorTriples;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_datos_partido);
        ObtenerReferencias();
        Bundle datos = this.getIntent().getExtras();
        String idPartido = datos.getString("id");
        final String IdPartido = idPartido;
        AbrirBaseDatos();
        TraerJugadoresId = TraerJugadores(idPartido);
        ConvocadosArray = TraerConvocados(TraerJugadoresId);
        ArrayAdapter<Jugador> adapter = new ArrayAdapter<Jugador>(this,android.R.layout.simple_list_item_1, ConvocadosArray);
        lstConvocados.setAdapter(adapter);
        lstConvocados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                lstConvocados.setItemChecked(i, true);
                if (vwAnterior!=null){
                    vwAnterior.setBackgroundColor(Color.TRANSPARENT);
                }
                view.setBackgroundColor(Color.LTGRAY);
                vwAnterior = view;
                String idJugador = TraerJugadoresId.get(i).toString();
                EstadisticasXJugador MiJugadorEstadisticas;
                MiJugadorEstadisticas=TraerEstadisticas(idJugador,IdPartido);
                txtPuntos.setText("Puntos: " + MiJugadorEstadisticas.Puntos.toString());
                txtAsitencias.setText("Asistencias: " + MiJugadorEstadisticas.Asistencias.toString());
                txtPerdidas.setText("Perdidas: " + MiJugadorEstadisticas.Perdidas.toString());
                txtFaltas.setText("Faltas: " + MiJugadorEstadisticas.Faltas.toString());
                txtTapones.setText("Tapones: " + MiJugadorEstadisticas.Tapones.toString());
                txtRobos.setText("Robos: " + MiJugadorEstadisticas.Robos.toString());
                Integer Rebotes = MiJugadorEstadisticas.RebOff + MiJugadorEstadisticas.RebDef;
                txtRebs.setText("Rebotes: " + Rebotes.toString());
            }
        });
        // SELECT IDJUG FROM ESTADISTICASXJUGADOR WHERE IDPARTIDO=AL QUE TRAJISTE DEL BUNDLE. GUARDAS EN UN ARRAYLIST DE INT TODOS LOS ID
        // FOR QUE RECORRA LEL ARRAYLIST Y ADENTRO DEL FOR UN (SELECT NOMBRE,POSICION FROM JUGADORES WHERE IDJUGADOR=ARRAYLIST(i) Y AGREGAS A UNN ARRAYLIST

    }




    private EstadisticasXJugador TraerEstadisticas(String idJugador,String idPartido)
    {
        Cursor MisRegistros = this.baseDatos.rawQuery("select Puntos,Asistencias,Robos,Faltas,Tapones,RebOff,RebDef,Perdidas from EstadisticasXJugador WHERE IdPartido="+idPartido+" AND IdJugador= "+idJugador,null);
        EstadisticasXJugador misEstadisticas = new EstadisticasXJugador();
        if(MisRegistros.moveToFirst())
        {
            misEstadisticas.Puntos = MisRegistros.getInt(0);
            misEstadisticas.Asistencias=MisRegistros.getInt(1);
            misEstadisticas.Robos=MisRegistros.getInt(2);
            misEstadisticas.Faltas=MisRegistros.getInt(3);
            misEstadisticas.Tapones=MisRegistros.getInt(4);
            misEstadisticas.RebOff=MisRegistros.getInt(5);
            misEstadisticas.RebDef=MisRegistros.getInt(6);
            misEstadisticas.Perdidas=MisRegistros.getInt(7);
        }
        return misEstadisticas;
    }



    private ArrayList<Integer> TraerJugadores(String id)
    {

            Log.d("SQLITE", "Ejecuto la lectura de todos");
            Cursor MisRegistros = this.baseDatos.rawQuery("select IdJugador from EstadisticasXJugador WHERE IdPartido=?", new String[]{id});
            Log.d("SQLITE", "Verifico si trajo registros");
            ArrayList<Integer> jugadoresArray = new ArrayList<>();
            if (MisRegistros.moveToFirst()) {
                do {

                    Log.d("sql", "Cargarjugador provisorio");
                    jugadoresArray.add(MisRegistros.getInt(0));
                }
                while (MisRegistros.moveToNext() == true);

            } else {
                Toast.makeText(getApplicationContext(), "Null en mis registros", Toast.LENGTH_LONG).show();
            }

        return jugadoresArray;
    }

    private ArrayList<Jugador> TraerConvocados(ArrayList<Integer> ConvocadosString) {
        ArrayList<Jugador> localArrayList = new ArrayList<Jugador>();
        Log.d("SQLITE", "Ejecuto la lectura de todos");

        for (int i = 0; i < ConvocadosString.size(); i++) {

            Cursor MisRegistros = this.baseDatos.rawQuery("select nombre,posicion from Jugadores WHERE IdJugador=" + ConvocadosString.get(i), null);
            Log.d("SQLITE", "Verifico si trajo registros");
            if (MisRegistros.moveToFirst()){

                Jugador MiJugador = new Jugador();
                Log.d("sql", "Cargarjugador provisorio");
                MiJugador.Nombre = MisRegistros.getString(0);
                MiJugador.Posicion = MisRegistros.getString(1);
                localArrayList.add(MiJugador);
            }
        }
        return localArrayList;

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
    private void ObtenerReferencias()
    {
        lstConvocados = (ListView) findViewById(R.id.lstConvocados);
        porcTl = (TextView) findViewById(R.id.txtPorcTirosLib);
        txtPuntos = (TextView) findViewById(R.id.txtPuntos);
        txtAsitencias = (TextView) findViewById(R.id.txtAsitencias);
        txtRebs = (TextView) findViewById(R.id.txtRebs);
        txtRobos=(TextView)findViewById(R.id.txtRobos);
        txtTapones=(TextView)findViewById(R.id.txtTapones);
        txtPerdidas=(TextView)findViewById(R.id.txtPerdidas);
        txtFaltas=(TextView)findViewById(R.id.txtFaltas);
        txtPorDobles=(TextView)findViewById(R.id.txtPorcDobles);
        txtPorTriples=(TextView)findViewById(R.id.txtPorcTripl);
    }
}
