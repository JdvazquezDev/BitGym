package com.BitGym.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;

import com.BitGym.R;



public class editEjercicioRutinaActivity extends AppCompatActivity {

    Boolean resume = false;
    long elapsedTime;
    Chronometer cmTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_ejercicio_to_lista_rutina);

        final Button btGuardar = (Button) this.findViewById( R.id.btGuardarEdit );
        final Button btCancelar = (Button) this.findViewById( R.id.btCancelar2 );

        final EditText numero_series = (EditText) this.findViewById( R.id.series );
        final EditText numero_repeticiones = (EditText) this.findViewById( R.id.repeticiones );
        final EditText peso = (EditText) this.findViewById( R.id.peso );
        final EditText info_extra = (EditText) this.findViewById( R.id.informacion_extra );
        cmTimer =  (Chronometer) this.findViewById(R.id.cmTimer);

        Intent datosEnviados = this.getIntent();
        final int nombre = datosEnviados.getExtras().getInt("_id");
        final String fecha = (String) datosEnviados.getExtras().get("fecha");


        numero_series.setText( String.valueOf(datosEnviados.getExtras().getInt("series")));
        numero_repeticiones.setText(String.valueOf(datosEnviados.getExtras().getInt("repeticiones")));
        peso.setText(String.valueOf(datosEnviados.getExtras().getInt("peso")));
        info_extra.setText(datosEnviados.getExtras().getString("infoExtra"));
        cmTimer.setText(datosEnviados.getExtras().getString("tiempo"));

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmTimer.stop();
                resume = true;
                editEjercicioRutinaActivity.this.setResult( Activity.RESULT_CANCELED );
                editEjercicioRutinaActivity.this.finish();
            }
        });

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cmTimer.stop();
                resume = true;

                Intent datosRetornar = new Intent();

                datosRetornar.putExtra( "_id", nombre );
                datosRetornar.putExtra( "series", Integer.parseInt(numero_series.getText().toString()));
                datosRetornar.putExtra( "repeticiones", Integer.parseInt(numero_repeticiones.getText().toString()));
                datosRetornar.putExtra( "peso", Integer.parseInt(peso.getText().toString()));
                datosRetornar.putExtra( "infoExtra", info_extra.getText().toString());
                datosRetornar.putExtra( "tiempo", cmTimer.getText().toString());
                datosRetornar.putExtra("fecha",fecha);

                editEjercicioRutinaActivity.this.setResult( Activity.RESULT_OK, datosRetornar );
                editEjercicioRutinaActivity.this.finish();
            }
        });

        Button empezar = (Button) this.findViewById(R.id.empezar);
        Button parar = (Button) this.findViewById(R.id.parar);

        empezar.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {

                if (!resume) {
                    cmTimer.setBase(SystemClock.elapsedRealtime());
                    cmTimer.start();
                } else {
                    cmTimer.start();
                }
            }
        });

        parar.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                cmTimer.stop();
                resume = true;
            }
        });
        cmTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer arg0) {
                if (!resume) {
                    long minutes = ((SystemClock.elapsedRealtime() - cmTimer.getBase())/1000) / 60;
                    long seconds = ((SystemClock.elapsedRealtime() - cmTimer.getBase())/1000) % 60;
                    elapsedTime = SystemClock.elapsedRealtime();
                } else {
                    long minutes = ((elapsedTime - cmTimer.getBase())/1000) / 60;
                    long seconds = ((elapsedTime - cmTimer.getBase())/1000) % 60;
                    elapsedTime = elapsedTime + 1000;
                }
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu( menu );
        this.getMenuInflater().inflate( R.menu.activity_actions, menu );
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        boolean toret = false;
        switch( menuItem.getItemId() ) {
            case R.id.action_atras:
                this.finish();
                break;
        }
        return toret;
    }

    public void onPause() {
        super.onPause();
        cmTimer.stop();
        resume = true;
    }
}
