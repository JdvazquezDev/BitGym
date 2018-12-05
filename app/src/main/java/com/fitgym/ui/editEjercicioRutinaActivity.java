package com.fitgym.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fitgym.R;



public class editEjercicioRutinaActivity extends AppCompatActivity {
    int nombre;
    String fecha;
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
        Intent datosEnviados = this.getIntent();
        nombre = datosEnviados.getExtras().getInt("_id");
        fecha = (String) datosEnviados.getExtras().get("fecha");


        numero_series.setText( String.valueOf(datosEnviados.getExtras().getInt("series")));
        numero_repeticiones.setText(String.valueOf(datosEnviados.getExtras().getInt("repeticiones")));
        peso.setText(String.valueOf(datosEnviados.getExtras().getInt("peso")));
        info_extra.setText(datosEnviados.getExtras().getString("infoExtra"));

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEjercicioRutinaActivity.this.setResult( Activity.RESULT_CANCELED );
                editEjercicioRutinaActivity.this.finish();
            }
        });

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent datosRetornar = new Intent();

                datosRetornar.putExtra( "_id", nombre );
                datosRetornar.putExtra( "series", Integer.parseInt(numero_series.getText().toString()));
                datosRetornar.putExtra( "repeticiones", Integer.parseInt(numero_repeticiones.getText().toString()));
                datosRetornar.putExtra( "peso", Integer.parseInt(peso.getText().toString()));
                String info = "";

                datosRetornar.putExtra( "infoExtra", info_extra.getText().toString());
                datosRetornar.putExtra("fecha",fecha);

                editEjercicioRutinaActivity.this.setResult( Activity.RESULT_OK, datosRetornar );
                editEjercicioRutinaActivity.this.finish();
            }
        });

        Button empezar = (Button) this.findViewById(R.id.empezar);
        Button parar = (Button) this.findViewById(R.id.parar);
        cmTimer = (Chronometer) findViewById(R.id.cmTimer);
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
        // example setOnChronometerTickListener
        cmTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer arg0) {
                if (!resume) {
                    long minutes = ((SystemClock.elapsedRealtime() - cmTimer.getBase())/1000) / 60;
                    long seconds = ((SystemClock.elapsedRealtime() - cmTimer.getBase())/1000) % 60;
                    elapsedTime = SystemClock.elapsedRealtime();
                    Log.d("asd", "onChronometerTick: " + minutes + " : " + seconds);
                } else {
                    long minutes = ((elapsedTime - cmTimer.getBase())/1000) / 60;
                    long seconds = ((elapsedTime - cmTimer.getBase())/1000) % 60;
                    elapsedTime = elapsedTime + 1000;
                    Log.d("asd", "onChronometerTick: " + minutes + " : " + seconds);
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





    // Variable que indicará si la aplicación está o no en marcha.
    private boolean working;
    private int  tiempo,brewTime;
    // Cronómetro de la aplicación.
    private CountDownTimer timer;
    Boolean resume = false;
    private TextView tiempoT;
    Chronometer cmTimer;
    long elapsedTime;
}
