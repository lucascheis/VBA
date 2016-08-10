package com.example.leo.bva;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ActivityDatosPartido extends AppCompatActivity {
    ProyectoFinalDB accesoBaseDatos;
    SQLiteDatabase baseDatos;
    TextView porcTl,txtPuntos,txtAsitencias,txtRebOff,txtRebDef,txtRobos,txtTapones,txtPerdidas,txtFaltas,txtPorDobles,txtPorTriples;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_datos_partido);
        ObtenerReferencias();

        //RECIBIR EL ID DEL PARITIDO QUE TOCO
        // SELECT IDJUG FROM ESTADISTICASXJUGADOR WHERE IDPARTIDO=AL QUE TRAJISTE DEL BUNDLE. GUARDAS EN UN ARRAYLIST DE INT TODOS LOS ID

        // FOR QUE RECORRA LEL ARRAYLIST Y ADENTRO DEL FOR UN (SELECT NOMBRE,POSICION FROM JUGADORES WHERE IDJUGADOR=ARRAYLIST(i) Y AGREGAS A UNN ARRAYLIST

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
        porcTl = (TextView) findViewById(R.id.txtPorcTirosLib);
        txtPuntos = (TextView) findViewById(R.id.txtPuntos);
        txtAsitencias = (TextView) findViewById(R.id.txtAsistencias);
        txtRebOff = (TextView) findViewById(R.id.txtRebOff);
        txtRebDef = (TextView) findViewById(R.id.txtRebDef);
        txtRobos=(TextView)findViewById(R.id.txtRobos);
        txtTapones=(TextView)findViewById(R.id.txtTapones);
        txtPerdidas=(TextView)findViewById(R.id.txtPerdidas);
        txtFaltas=(TextView)findViewById(R.id.txtFaltas);
        txtPorDobles=(TextView)findViewById(R.id.txtPorcDobles);
        txtPorTriples=(TextView)findViewById(R.id.txtPorcTripl);
    }
}
