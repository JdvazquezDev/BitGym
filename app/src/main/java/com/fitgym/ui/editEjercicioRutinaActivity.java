package com.fitgym.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fitgym.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class editEjercicioRutinaActivity extends AppCompatActivity {
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_ejercicio_to_lista_rutina);

        final Button btGuardar = (Button) this.findViewById( R.id.btGuardarEdit );
        final Button btCancelar = (Button) this.findViewById( R.id.btCancelar );
      //  final EditText numero_series = (EditText) this.findViewById( R.id.series );
       // final EditText numero_repeticiones = (EditText) this.findViewById( R.id.repeticiones );
       // final EditText peso = (EditText) this.findViewById( R.id.peso );
        //final EditText info_extra = (EditText) this.findViewById( R.id.informacion_extra );
        Intent datosEnviados = this.getIntent();
        id = datosEnviados.getExtras().getInt("_id");

   //     numero_series.setText(datosEnviados.getExtras().getString(("series")));
    //    numero_repeticiones.setText(datosEnviados.getExtras().getString(("repeticiones")));
        //peso.setText(datosEnviados.getExtras().getString(("peso")));
        //info_extra(datosEnviados.getExtras().getString(("infoExtra")));
/*
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

                datosRetornar.putExtra( "id", id );
                datosRetornar.putExtra( "series", numero_series.getText() );
                datosRetornar.putExtra( "repeticiones", numero_repeticiones.getText() );
                datosRetornar.putExtra( "peso", peso.getText() );
                datorRetornar.putExtra( "infoExtra", info.getText());

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
                btGuardar.setEnabled( id.length() > 0);
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
                btGuardar.setEnabled( descripcion_nuevo_ejercicio.getText().toString().trim().length() > 0 );
            }
        });*/
    }
}
