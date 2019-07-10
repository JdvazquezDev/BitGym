package com.BitGym.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.BitGym.core.DBManager;
import com.BitGym.R;

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
                Intent subActividad = new Intent(MainActivity.this, listExercisesActivity.class);
                MainActivity.this.startActivity(subActividad);
            }
        });
        btRutina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent subActividad = new Intent( MainActivity.this, calendarRoutineActivity.class );
                MainActivity.this.startActivity(subActividad);
            }
        });

        this.dbManager = DBManager.open( this.getApplicationContext());

    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    private DBManager dbManager;

}
