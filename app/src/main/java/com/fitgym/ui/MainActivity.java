package com.fitgym.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fitgym.R;

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

    }
}
