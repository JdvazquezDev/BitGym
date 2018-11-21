package com.fitgym.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fitgym.R;

public class editEjercicioRutinaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_ejercicio_to_lista_rutina);

        final Button btGuardar = (Button) this.findViewById( R.id.btGuardarEdit );
        final Button btCancelar = (Button) this.findViewById( R.id.btCancelar );
        final EditText nombre_nuevo_ejercicio = (EditText) this.findViewById( R.id.nombre_nuevo_ejercicio );
        final EditText descripcion_nuevo_ejercicio = (EditText) this.findViewById( R.id.descripcion_nuevo_ejercicio );

        Intent datosEnviados = this.getIntent();

        nombre_nuevo_ejercicio.setText( datosEnviados.getExtras().getString(( "nombre" ) ) );
        descripcion_nuevo_ejercicio.setText(datosEnviados.getExtras().getString(("descripcion")));
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
                datosRetornar.putExtra( "nombre", nombre_nuevo_ejercicio.getText().toString() );
                datosRetornar.putExtra( "descripcion", descripcion_nuevo_ejercicio.getText().toString() );
                datosRetornar.putExtra( "pos", editEjercicioRutinaActivity.this.getIntent().getExtras().getInt( "pos" ) );

                editEjercicioRutinaActivity.this.setResult( Activity.RESULT_OK, datosRetornar );
                editEjercicioRutinaActivity.this.finish();
            }
        });
        btGuardar.setEnabled( false );

        nombre_nuevo_ejercicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btGuardar.setEnabled( nombre_nuevo_ejercicio.getText().toString().trim().length() > 0 && nombre_nuevo_ejercicio.getText().toString().trim().length() > 0);
            }
        });
        descripcion_nuevo_ejercicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btGuardar.setEnabled( descripcion_nuevo_ejercicio.getText().toString().trim().length() > 0  &&  descripcion_nuevo_ejercicio.getText().toString().trim().length() > 0);
            }
        });
    }

}
