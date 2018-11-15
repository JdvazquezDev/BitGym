package com.fitgym.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fitgym.R;
import com.fitgym.core.DBManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btEjercicios = (Button) this.findViewById( R.id.button_ejercicios);
        Button btRutina = (Button) this.findViewById( R.id.button_rutina);

        btEjercicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent subActividad = new Intent(MainActivity.this, ListaEjerciciosActivity.class);
                MainActivity.this.startActivity(subActividad);
            }
        });
        btRutina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent subActividad = new Intent( MainActivity.this, CalendarioRutinaActivity.class );
                MainActivity.this.startActivity(subActividad);
            }
        });

        this.dbManager = DBManager.open( this.getApplicationContext());

    }

    @Override
    public void onPause()
    {
        super.onPause();
       // ( (MiApp) this.getApplication() ).getBD().close();
    }

    private DBManager dbManager;

}
