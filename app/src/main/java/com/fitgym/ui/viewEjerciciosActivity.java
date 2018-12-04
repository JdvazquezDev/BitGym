package com.fitgym.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fitgym.R;

public class viewEjerciciosActivity extends AppCompatActivity {
    ImageView imagenView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_ejercicio_to_lista_ejercicios);

        final Button btCancelar = (Button) this.findViewById(R.id.btCancelar);
        final EditText nombre_nuevo_ejercicio = (EditText) this.findViewById( R.id.nombre_nuevo_ejercicio );
        final EditText descripcion_nuevo_ejercicio = (EditText) this.findViewById( R.id.descripcion_nuevo_ejercicio);
        imagenView = findViewById(R.id.imageView);

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewEjerciciosActivity.this.setResult( Activity.RESULT_CANCELED );
                viewEjerciciosActivity.this.finish();
            }
        });

    }
}
