package com.example.leo.bva;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnEquipo,volver,btnPartido,btnEstadisticas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ObtenerReferencias();
        btnPartido.setOnClickListener(btnPartido_Click);
        btnEstadisticas.setOnClickListener(btnEstadisticas_Click);
        btnEquipo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent PasarActEquipo = new Intent(MainActivity.this,ActivityEquipo.class);
                startActivity(PasarActEquipo);

            }
        });
    }

    private View.OnClickListener btnPartido_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this,configurarPartido.class);
            startActivity(intent);
        }
    };
    private View.OnClickListener btnEstadisticas_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this,ActivityEstadisticas.class);
            startActivity(intent);
        }
    };


    private void ObtenerReferencias()
    {
        btnEquipo = ((Button)findViewById(R.id.btnEquipo));
        volver=(Button) findViewById(R.id.button5);
        btnPartido = (Button) findViewById(R.id.button2);
        btnEstadisticas=(Button) findViewById(R.id.button4);
    }
}
