package com.example.leo.bva;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class activityEmpezarPartido extends AppCompatActivity {
    View vwAnterior = null;
    ProyectoFinalDB accesoBaseDatos;
    Boolean[] tom = new Boolean[10];
    Integer alongi;
    TextView txtAsistencias,txtRobos,txtPerdidas,txtRebotes,txtFaltas,txtTapones;
    SQLiteDatabase baseDatos;
    Button btnmasuno,btnmasdos,btnmastres,btnTerminar, btnOutFT, btnOutDob,btnOutTri,btnMasAsist,btnMenosAsist,btnMasFalta,btnMenosFalta,btnMasPerdida;
    Button btnMenosPerdida,btnVolver,btnMasTapon,btnMenosTapon,btnMasRebOff,btnMenosRebOff,btnMasRebDeff,btnMenosRebDeff,btnMasRobo,btnMenosRobo,btnCancelarPartido,btnConsultarPorcentajes;
    Integer puntos=0,asistencias=0,rebotesOff=0,rebotesDef=0,Faltas=0,Tapones=0,Robos=0,Perdidas=0,TLErrado=0,DobleErrado=0,TripleErrado=0,TLMetido=0,DobleMetido=0,TripleMetido=0;
    TextView ptsJug,txtVS;
    ArrayList<Jugador> ListJug;
    ArrayList<String> ConvocadosString;
    ListView ListaMostrar;
    String id;
    String PartidosJug,VS;
    Integer IdPartido=-1;
    String IdJugador="-1";
    String Condicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_empezar_partido);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        IdPartido= bundle.getInt("IdPartido");
        ConvocadosString = bundle.getStringArrayList("Convocados");
        VS=bundle.getString("Vs");
        Condicion=bundle.getString("Condicion");

        AbrirBaseDatos();

        ObtenerReferencias();

        txtVS.setText("VS:" + VS);
        ListJug = TraerConvocados(ConvocadosString);
        ArrayAdapter<Jugador> localArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, ListJug);
        this.ListaMostrar.setAdapter(localArrayAdapter);

        CargarConvocadosATabla(ConvocadosString,IdPartido);
        btnVolver.setOnClickListener(btnVolver_Click);
        btnmasuno.setOnClickListener(btnmasuno_Click);
        btnmasdos.setOnClickListener(btnmasdos_Click);
        btnmastres.setOnClickListener(btnmastres_Click);
        btnOutFT.setOnClickListener(btnOutFT_Click);
        btnOutDob.setOnClickListener(btnOutDob_Click);
        btnOutTri.setOnClickListener(btnOutTri_Click);
        btnMasAsist.setOnClickListener(btnMasAsist_Click);
        btnMenosAsist.setOnClickListener(btnMenosAsist_Click);
        btnMasPerdida.setOnClickListener(btnMasPerdida_Click);
        btnMenosPerdida.setOnClickListener(btnMenosPerdida_Click);
        btnMasFalta.setOnClickListener(btnMasFalta_Click);
        btnMenosFalta.setOnClickListener(btnMenosFalta_Click);
        btnMasTapon.setOnClickListener(btnMasTapon_Click);
        btnMenosTapon.setOnClickListener(btnMenosTapon_Click);
        btnMasRobo.setOnClickListener(btnMasRobo_Click);
        btnMenosRobo.setOnClickListener(btnMenosRobo_Click);
        btnMasRebDeff.setOnClickListener(btnMasRebDeff_Click);
        btnMenosRebDeff.setOnClickListener(btnMenosRebDeff_Click);
        btnMasRebOff.setOnClickListener(btnMasRebOff_Click);
        btnMenosRebOff.setOnClickListener(btnMenosRebOff_Click);
        btnTerminar.setOnClickListener(btnTerminar_Click);




        ListaMostrar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Jugador jugadorSeleccionado;
                ListaMostrar.setItemChecked(i, true);
                if (vwAnterior!=null){
                    vwAnterior.setBackgroundColor(Color.TRANSPARENT);
                }
                view.setBackgroundColor(Color.LTGRAY);
                vwAnterior = view;


                jugadorSeleccionado = ListJug.get(i);
                IdJugador=jugadorSeleccionado.id;
                EstadisticasXJugador MiJugadorEstadisticas;
                MiJugadorEstadisticas=TraerDatosJugador(IdJugador,IdPartido);
                ptsJug.setText("Puntos: " + MiJugadorEstadisticas.Puntos.toString());
                txtAsistencias.setText("Asistencias: " + MiJugadorEstadisticas.Asistencias.toString());
                txtPerdidas.setText("Perdidas: " + MiJugadorEstadisticas.Perdidas.toString());
                txtFaltas.setText("Faltas: " + MiJugadorEstadisticas.Faltas.toString());
                txtTapones.setText("Tapones: " + MiJugadorEstadisticas.Tapones.toString());
                txtRobos.setText("Robos: " + MiJugadorEstadisticas.Robos.toString());
                Integer Rebotes = MiJugadorEstadisticas.RebOff + MiJugadorEstadisticas.RebDef;

                txtRebotes.setText("Rebotes: " + Rebotes.toString());



            }
        });
    }

    private View.OnClickListener btnVolver_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activityEmpezarPartido.this,MainActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener btnTerminar_Click= new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(activityEmpezarPartido.this, activityFinalPartido.class);
            Bundle bundle = new Bundle();

            bundle.putInt("Puntos",puntos);
            bundle.putInt("Asistencias",asistencias);
            bundle.putInt("RebotesOff",rebotesOff);
            bundle.putInt("RebotesDef",rebotesDef);
            bundle.putInt("Robos",Robos);
            bundle.putInt("Perdidas",Perdidas);
            bundle.putInt("Faltas",Faltas);
            bundle.putInt("Tapones",Tapones);
            bundle.putInt("TLErrados",TLErrado);
            bundle.putInt("DoblesErrado",DobleErrado);
            bundle.putInt("TriplesErrados",TripleErrado);
            bundle.putInt("TLMetidos",TLMetido);
            bundle.putInt("DoblesMetidos",DobleMetido);
            bundle.putInt("TriplesMetidos",TripleMetido);
            bundle.putInt("IdPartido",IdPartido);
            bundle.putStringArrayList("Convocados",ConvocadosString);
            bundle.putString("Condicion",Condicion);
            intent.putExtras(bundle);




            AlertDialog.Builder alert = new AlertDialog.Builder(activityEmpezarPartido.this);
            LayoutInflater inflater=activityEmpezarPartido.this.getLayoutInflater();
            View layout=inflater.inflate(R.layout.activity_activity_agregar_jugador,null);
            alert.setView(layout);

            startActivity(intent);
        }
    };


    private View.OnClickListener btnMasRebDeff_Click= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SumarEstadisticas("RebDef",true,IdJugador);
            EstadisticasXJugador MiJugadorEstadisticas;
            MiJugadorEstadisticas=TraerDatosJugador(IdJugador,IdPartido);
            Integer Rebotes = MiJugadorEstadisticas.RebOff + MiJugadorEstadisticas.RebDef;
            rebotesDef=rebotesDef+1;

            txtRebotes.setText("Rebotes: " + Rebotes.toString());
        }
    };
    private View.OnClickListener btnMenosRebDeff_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SumarEstadisticas("RebDef",false,IdJugador);
            EstadisticasXJugador MiJugadorEstadisticas;
            MiJugadorEstadisticas=TraerDatosJugador(IdJugador,IdPartido);
            Integer Rebotes = MiJugadorEstadisticas.RebOff + MiJugadorEstadisticas.RebDef;
            rebotesDef=rebotesDef-1;

            txtRebotes.setText("Rebotes: " + Rebotes.toString());
        }
    };

    private View.OnClickListener btnMenosRebOff_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            SumarEstadisticas("RebOff",false,IdJugador);
            EstadisticasXJugador MiJugadorEstadisticas;
            MiJugadorEstadisticas=TraerDatosJugador(IdJugador,IdPartido);
            Integer Rebotes = MiJugadorEstadisticas.RebOff + MiJugadorEstadisticas.RebDef;
            rebotesOff=rebotesOff-1;
            txtRebotes.setText("Rebotes: " + Rebotes.toString());
        }
    };
    private View.OnClickListener btnMasRebOff_Click= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SumarEstadisticas("RebOff",true,IdJugador);
            EstadisticasXJugador MiJugadorEstadisticas;
            MiJugadorEstadisticas=TraerDatosJugador(IdJugador,IdPartido);
            Integer Rebotes = MiJugadorEstadisticas.RebOff + MiJugadorEstadisticas.RebDef;
            rebotesOff++;
            txtRebotes.setText("Rebotes: " + Rebotes.toString());

        }
    };

    private View.OnClickListener btnMasAsist_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SumarEstadisticas("Asistencias",true,IdJugador);
            EstadisticasXJugador MiJugadorEstadisticas;
            MiJugadorEstadisticas=TraerDatosJugador(IdJugador,IdPartido);
            asistencias++;
            txtAsistencias.setText("Asistencias: " + MiJugadorEstadisticas.Asistencias.toString());
        }
    };
    private View.OnClickListener btnMasFalta_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SumarEstadisticas("Faltas",true,IdJugador);
            EstadisticasXJugador MiJugadorEstadisticas;
            MiJugadorEstadisticas=TraerDatosJugador(IdJugador,IdPartido);
            Faltas++;
            txtFaltas.setText("Faltas: " + MiJugadorEstadisticas.Faltas.toString());
        }
    };
    private View.OnClickListener btnMasTapon_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SumarEstadisticas("Tapones",true,IdJugador);
            EstadisticasXJugador MiJugadorEstadisticas;
            MiJugadorEstadisticas=TraerDatosJugador(IdJugador,IdPartido);
            Tapones++;
            txtTapones.setText("Tapones: " + MiJugadorEstadisticas.Tapones.toString());
        }
    };
    private View.OnClickListener btnMenosTapon_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SumarEstadisticas("Tapones",false,IdJugador);
            EstadisticasXJugador MiJugadorEstadisticas;
            MiJugadorEstadisticas=TraerDatosJugador(IdJugador,IdPartido);
            Tapones--;
            txtTapones.setText("Tapones: " + MiJugadorEstadisticas.Tapones.toString());
        }
    };
    private View.OnClickListener btnMasRobo_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SumarEstadisticas("Robos",true,IdJugador);
            EstadisticasXJugador MiJugadorEstadisticas;
            MiJugadorEstadisticas=TraerDatosJugador(IdJugador,IdPartido);
            Robos++;
            txtRobos.setText("Robos: " + MiJugadorEstadisticas.Robos.toString());
        }
    };
    private View.OnClickListener btnMenosRobo_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SumarEstadisticas("Robos",false,IdJugador);
            EstadisticasXJugador MiJugadorEstadisticas;
            MiJugadorEstadisticas=TraerDatosJugador(IdJugador,IdPartido);
            Robos--;
            txtRobos.setText("Robos: " + MiJugadorEstadisticas.Robos.toString());
        }
    };
    private View.OnClickListener btnMenosFalta_Click= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SumarEstadisticas("Faltas",false,IdJugador);
            EstadisticasXJugador MiJugadorEstadisticas;
            MiJugadorEstadisticas=TraerDatosJugador(IdJugador,IdPartido);
            txtFaltas.setText("Faltas: " + MiJugadorEstadisticas.Faltas.toString());
        }
    };
    private View.OnClickListener btnMenosAsist_Click= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SumarEstadisticas("Asistencias",false,IdJugador);
            EstadisticasXJugador MiJugadorEstadisticas;
            MiJugadorEstadisticas=TraerDatosJugador(IdJugador,IdPartido);
            asistencias--;
            txtAsistencias.setText("Asistencias: " + MiJugadorEstadisticas.Asistencias.toString());
        }
    };
    private View.OnClickListener btnMasPerdida_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SumarEstadisticas("Perdidas",true,IdJugador);
            EstadisticasXJugador MiJugadorEstadisticas;
            MiJugadorEstadisticas=TraerDatosJugador(IdJugador,IdPartido);
            Perdidas++;
            txtPerdidas.setText("Perdidas: " + MiJugadorEstadisticas.Perdidas.toString());
        }
    };
    private View.OnClickListener btnMenosPerdida_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SumarEstadisticas("Perdidas",false,IdJugador);
            EstadisticasXJugador MiJugadorEstadisticas;
            MiJugadorEstadisticas=TraerDatosJugador(IdJugador,IdPartido);
            Perdidas--;
            txtPerdidas.setText("Perdidas: " + MiJugadorEstadisticas.Perdidas.toString());
        }
    };
    private View.OnClickListener btnOutFT_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SumarErrados(IdJugador,1);
            TLErrado++;
        }
    };

    private View.OnClickListener btnOutDob_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SumarErrados(IdJugador,2);
            DobleErrado++;

        }
    };

    private  View.OnClickListener btnOutTri_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SumarErrados(IdJugador,3);
            TripleErrado++;

        }
    };

    private View.OnClickListener btnmasuno_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            SumarPuntos(IdJugador,1);
            EstadisticasXJugador MiJugadorEstadisticas;
            puntos+=1;
            TLMetido++;
            MiJugadorEstadisticas=TraerDatosJugador(IdJugador,IdPartido);
            ptsJug.setText("Puntos: " + MiJugadorEstadisticas.Puntos.toString());



        }
    };

    private View.OnClickListener btnmasdos_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SumarPuntos(IdJugador,2);
            EstadisticasXJugador MiJugadorEstadisticas;
            DobleMetido++;
            MiJugadorEstadisticas=TraerDatosJugador(IdJugador,IdPartido);
            puntos+=2;
            ptsJug.setText("Puntos: " + MiJugadorEstadisticas.Puntos.toString());

        }
    };

    private View.OnClickListener btnmastres_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SumarPuntos(IdJugador,3);
            EstadisticasXJugador MiJugadorEstadisticas;
            TripleMetido++;
            MiJugadorEstadisticas=TraerDatosJugador(IdJugador,IdPartido);
            puntos+=3;
            ptsJug.setText("Puntos: " + MiJugadorEstadisticas.Puntos.toString());

        }
    };




    private void SumarEstadisticas(String campo,boolean metio,String id) {
        if (AbrirBaseDatos()) {
            if (metio) {

                this.baseDatos.execSQL("UPDATE EstadisticasXJugador SET " + campo + " = " + campo + " + " + 1 + " WHERE IdJugador=" + id + " AND IdPartido=" + IdPartido);
            }
            else {
                this.baseDatos.execSQL("UPDATE EstadisticasXJugador SET " + campo + " = " + campo + " - " + 1 + " WHERE IdJugador=" + id + " AND IdPartido=" + IdPartido);
            }
        }
    }


    public boolean AbrirBaseDatos()
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
    private void ObtenerReferencias()
    {
        btnVolver = (Button) findViewById(R.id.btnCancelarPartido);
        txtVS=(TextView) findViewById(R.id.txtVS);
        txtAsistencias = (TextView) findViewById(R.id.txtAsistencias);
        txtRebotes = (TextView) findViewById(R.id.txtRebotes);
        txtFaltas = (TextView) findViewById(R.id.txtFaltas);
        txtTapones = (TextView) findViewById(R.id.txtTapones);
        txtRobos = (TextView) findViewById(R.id.txtRobos);
        txtPerdidas = (TextView) findViewById(R.id.txtPerdidas);
        btnmasuno = (Button) findViewById(R.id.btnMasUno);
        btnmasdos = (Button) findViewById(R.id.btnMasDos);
        btnmastres = (Button) findViewById(R.id.btnTriple);
        ptsJug = (TextView) findViewById(R.id.txtPts);
        btnOutFT = (Button) findViewById(R.id.btnOut1);
        btnOutDob = (Button) findViewById(R.id.btnOut2);
        btnOutTri = (Button) findViewById(R.id.btnOut3);
        btnMasAsist=(Button) findViewById(R.id.btnAsistMas);
        btnMenosAsist=(Button) findViewById(R.id.btnAsistMenos);
        btnMasFalta = (Button) findViewById(R.id.btnFaltasMas);
        btnMenosFalta = (Button) findViewById(R.id.btnFaltasMenos);
        btnMasTapon = (Button) findViewById(R.id.btnTaponesMas);
        btnMenosTapon = (Button) findViewById(R.id.btnTaponesMenos);
        btnMenosRebOff=(Button) findViewById(R.id.btnOffRebMenos);
        btnMasRebDeff=(Button) findViewById(R.id.btnDeffRebMas);
        btnMenosRebDeff=(Button) findViewById(R.id.btnDeffRebMenos);
        btnMasRebOff=(Button) findViewById(R.id.btnOffRebMas);
        btnCancelarPartido=(Button) findViewById(R.id.btnCancelarPartido);
        btnMasRobo=(Button) findViewById(R.id.btnRobosMas);
        btnMenosRobo=(Button) findViewById(R.id.btnRobosMenos);
        btnTerminar = (Button) findViewById(R.id.btnTerminar);
        ListaMostrar = (ListView) findViewById(R.id.lstJug);
        btnMasPerdida = (Button) findViewById(R.id.btnMasPerdidas);
        btnMenosPerdida = (Button) findViewById(R.id.btnMenosPerdidas);
    }

    private ArrayList<Jugador> TraerConvocados(ArrayList<String> ConvocadosString) {
        ArrayList<Jugador> localArrayList = new ArrayList<Jugador>();
        Log.d("SQLITE", "Ejecuto la lectura de todos");

        for (int i = 0; i < ConvocadosString.size(); i++) {

            Cursor MisRegistros = this.baseDatos.rawQuery("select nombre,edad,altura,posicion,mail,direccion,peso,IdJugador from Jugadores WHERE IdJugador=" + ConvocadosString.get(i), null);
            Log.d("SQLITE", "Verifico si trajo registros");
            if (MisRegistros.moveToFirst()){

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
        return localArrayList;

    }

    private void SumarErrados(String id,Integer pts)
    {
        if(AbrirBaseDatos()) {

            switch (pts) {

                case 1:
                    this.baseDatos.execSQL("UPDATE EstadisticasXJugador SET TirosLibErrados = TirosLibErrados + " + 1 + " WHERE IdJugador=" + id + " AND IdPartido=" + IdPartido);
                    break;

                case 2:
                    this.baseDatos.execSQL("UPDATE EstadisticasXJugador SET DoblesErrados = DoblesErrados + " + 1 + " WHERE IdJugador=" + id + " AND IdPartido=" + IdPartido);
                    break;

                case 3:
                    this.baseDatos.execSQL("UPDATE EstadisticasXJugador SET TriplesErrados = TriplesErrados + " + 1 + " WHERE IdJugador=" + id + " AND IdPartido=" + IdPartido);
                    break;


            }
        }
    }
    private void SumarPuntos(String id, Integer PuntosaSumar){
        if(AbrirBaseDatos()) {

            this.baseDatos.execSQL("UPDATE EstadisticasXJugador SET Puntos = Puntos + "+PuntosaSumar+" WHERE IdJugador="+id +" AND IdPartido=" + IdPartido);

            switch(PuntosaSumar) {

                case 1: this.baseDatos.execSQL("UPDATE EstadisticasXJugador SET TirosLibMetidos = TirosLibMetidos + "+1+" WHERE IdJugador="+id +" AND IdPartido=" + IdPartido); break;

                case 2: this.baseDatos.execSQL("UPDATE EstadisticasXJugador SET DoblesMetidos = DoblesMetidos + "+1+" WHERE IdJugador="+id +" AND IdPartido=" + IdPartido); break;

                case 3: this.baseDatos.execSQL("UPDATE EstadisticasXJugador SET TriplesMetidos = TriplesMetidos + "+1+" WHERE IdJugador="+id +" AND IdPartido=" + IdPartido); break;


            }



        }

    }


    private void CargarConvocadosATabla(ArrayList<String>Convocados,Integer idPartidin){

        if (AbrirBaseDatos() == true)
        {
            for(Integer i=0;i<Convocados.size();i++) {

                Log.d("SQLite", "Creo a convocado");
                ContentValues localContentValues = new ContentValues();
                localContentValues.put("IdJugador", Convocados.get(i));
                localContentValues.put("IdPartido", idPartidin);

                this.baseDatos.insert("EstadisticasXJugador", null, localContentValues);


            }

        }
    }

    private EstadisticasXJugador TraerDatosJugador(String idJgador,Integer idPartidin){


        EstadisticasXJugador MisEstadisticas = new EstadisticasXJugador();
        Log.d("SQLITE", "Ejecuto la lectura de todos");
        Cursor MisRegistros = this.baseDatos.rawQuery("SELECT Puntos,Asistencias,Robos,Faltas,Tapones,RebOff,RebDef,Perdidas from EstadisticasXJugador WHERE IdJugador ="+idJgador+" AND IdPartido="+idPartidin.toString(), null);
        Log.d("SQLITE", "Verifico si trajo registros");
        if (MisRegistros.moveToFirst()) {
            do {
                Log.d("sql", "Cargarjugador provisorio");
                MisEstadisticas.Puntos = MisRegistros.getInt(0);
                MisEstadisticas.Asistencias=MisRegistros.getInt(1);
                MisEstadisticas.Robos=MisRegistros.getInt(2);
                MisEstadisticas.Faltas=MisRegistros.getInt(3);
                MisEstadisticas.Tapones=MisRegistros.getInt(4);
                MisEstadisticas.RebOff=MisRegistros.getInt(5);
                MisEstadisticas.RebDef=MisRegistros.getInt(6);
                MisEstadisticas.Perdidas=MisRegistros.getInt(7);

            }
            while (MisRegistros.moveToNext() == true);

        }
        return MisEstadisticas;

    }

}






