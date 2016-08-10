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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityEstadisticas extends AppCompatActivity {


    Button btnBuscar,btnPorEquipo,btnPorJugador;
    ListView lstPartidos;
    ArrayList<Partidos> listPartidos = new ArrayList<Partidos>();

    EditText txtFecha,txtRival;
    ProyectoFinalDB accesoBaseDatos;
    SQLiteDatabase baseDatos;
    ArrayList<ListaPartidos> ListarPartidos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_estadisticas);
        AbrirBaseDatos();
        ObtenerReferencias();
        btnBuscar.setOnClickListener(btnBuscar_Click);
        btnPorEquipo.setOnClickListener(btnPorEquipo_Click);
        lstPartidos.setOnItemClickListener(lstPartidos_Click);
    }

    private View.OnClickListener btnPorEquipo_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private AdapterView.OnItemClickListener lstPartidos_Click = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Partidos miPartido = listPartidos.get(i);
            Intent intent = new Intent(ActivityEstadisticas.this,ActivityDatosPartido.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", miPartido.id);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    private View.OnClickListener btnBuscar_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            listPartidos=TraerPartidos(txtFecha.getText().toString(),txtRival.getText().toString());

            partidosView();

        }
    };

    private void ObtenerReferencias()
    {
        btnBuscar= (Button) findViewById(R.id.btnBuscar);
        btnPorEquipo=(Button) findViewById(R.id.btnVerxEquipo);
        btnPorJugador=(Button) findViewById(R.id.btnPorJugador);
        lstPartidos=(ListView) findViewById(R.id.listView2);
        txtFecha=(EditText) findViewById(R.id.editText2);
        txtRival=(EditText) findViewById(R.id.editText);
    }

    private ArrayList<Partidos> TraerPartidos(String mes,String rival)
    {



        ArrayList<Partidos> localArrayList = new ArrayList();
        Log.d("SQLITE", "Ejecuto la lectura de todos");

        Cursor MisRegistros=null;
        if(mes.isEmpty() && rival.isEmpty()==false) {
            String Consulta ="select * from Partidos WHERE Vs="+ "'"+rival+"'";
           MisRegistros = this.baseDatos.rawQuery(Consulta
                   , null);
        }
        if(mes.isEmpty()==false && rival.isEmpty())
        {
            MisRegistros = this.baseDatos.rawQuery("select * from Partidos WHERE Mes="+mes, null);
        }
        if(mes.isEmpty()==false && rival.isEmpty()==false)
        {
            MisRegistros = this.baseDatos.rawQuery("select * from Partidos WHERE Mes="+mes+" AND Vs="+"'"+rival+"'", null);
        }
        ListarPartidos.clear();

        Log.d("SQLITE", "Verifico si trajo registros");
        if (MisRegistros.moveToFirst()) {
            do {
                Partidos miPartido = new Partidos();
                miPartido.id = MisRegistros.getString(0);
                miPartido.Vs = MisRegistros.getString(1);
                miPartido.Año = MisRegistros.getString(2);
                miPartido.Mes = MisRegistros.getString(3);
                miPartido.Dia = MisRegistros.getString(4);
                miPartido.Condicion = MisRegistros.getString(5);
                miPartido.PuntosLocal = MisRegistros.getInt(6);
                miPartido.PuntosVisitante = MisRegistros.getInt(7);

                String fecha = miPartido.Año + "/" + miPartido.Mes + "/" + miPartido.Dia;


                if (miPartido.Condicion == "Local") {
                    if (miPartido.PuntosLocal > miPartido.PuntosVisitante) {
                        ListarPartidos.add((new ListaPartidos(miPartido.Vs.toString(), fecha, R.drawable.victoria)));
                    } else {
                        ListarPartidos.add((new ListaPartidos(miPartido.Vs.toString(), fecha, R.drawable.derrota)));
                    }
                } else {
                    if (miPartido.PuntosVisitante > miPartido.PuntosLocal) {
                        ListarPartidos.add((new ListaPartidos(miPartido.Vs.toString(), fecha, R.drawable.victoria)));
                    } else {
                        ListarPartidos.add((new ListaPartidos(miPartido.Vs.toString(), fecha, R.drawable.derrota)));
                    }
                }

                localArrayList.add(miPartido);
            }
            while (MisRegistros.moveToNext() == true);
        } else {
            Toast.makeText(getApplicationContext(), "Null en mis registros", Toast.LENGTH_LONG).show();
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
    ArrayAdapter<ListaPartidos> adapter;
    public void partidosView() {

     adapter = new MyListAdapter();
        lstPartidos.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<ListaPartidos> {

        public MyListAdapter() {
            super(ActivityEstadisticas.this, R.layout.itemviewpartidos, ListarPartidos);
        }

        public View getView(int Position, View convertView, ViewGroup parent) {


            View itemView = convertView;

            if(itemView==null) {
                itemView = getLayoutInflater().inflate(R.layout.itemviewpartidos, parent, false);

                ListaPartidos MiPartido = ListarPartidos.get(Position);
                ImageView imagen = (ImageView) itemView.findViewById(R.id.imageViewResultado);

                imagen.setImageResource(MiPartido.getResultado());

                TextView Nombre = (TextView) itemView.findViewById(R.id.txtVsAZO);
                String Name = MiPartido.getVS();
                Nombre.setText(MiPartido.getVS());

                TextView Posicione = (TextView) itemView.findViewById(R.id.txtFechazo);
                Posicione.setText(MiPartido.getFecha());
            }

            return itemView;

        }
    }
}
