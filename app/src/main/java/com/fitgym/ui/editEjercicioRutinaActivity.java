package com.fitgym.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


    }
}
