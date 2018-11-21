package com.fitgym.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fitgym.R;
import com.fitgym.core.DBManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;


public class editEjerciciosActivity extends AppCompatActivity {

    final int REQUEST_CODE_GALLERY = 999;
    ImageView imagenView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_ejercicio_to_lista_ejercicios);

        final Button btGuardarEdit = (Button) this.findViewById( R.id.btGuardarEdit );
        final Button btCancelar = (Button) this.findViewById( R.id.btCancelar );
        final Button btImagen = (Button) this.findViewById(R.id.btImagen);

        final EditText nombre_nuevo_ejercicio = (EditText) this.findViewById( R.id.nombre_nuevo_ejercicio );
        final EditText descripcion_nuevo_ejercicio = (EditText) this.findViewById( R.id.descripcion_nuevo_ejercicio);

        imagenView = findViewById(R.id.imageView);

        Intent datosEnviados = this.getIntent();

        nombre_nuevo_ejercicio.setText(datosEnviados.getExtras().getString(("nombre")));
        descripcion_nuevo_ejercicio.setText(datosEnviados.getExtras().getString(("descripcion")));

      //Mostrar imagen guardada en la bd
        byte[] byteArray = datosEnviados.getExtras().getByteArray("imagen");
        Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);
        imagenView.setImageBitmap(bm);

        btImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        editEjerciciosActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEjerciciosActivity.this.setResult( Activity.RESULT_CANCELED );
                editEjerciciosActivity.this.finish();
            }
        });

        btGuardarEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent datosRetornar = new Intent();
                datosRetornar.putExtra( "nombre", nombre_nuevo_ejercicio.getText().toString() );
                datosRetornar.putExtra( "descripcion", descripcion_nuevo_ejercicio.getText().toString() );
                datosRetornar.putExtra( "imagen", imageViewToByte(imagenView) );

                editEjerciciosActivity.this.setResult( Activity.RESULT_OK, datosRetornar );
                editEjerciciosActivity.this.finish();
            }
        });
        btGuardarEdit.setEnabled( false );

        nombre_nuevo_ejercicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btGuardarEdit.setEnabled( nombre_nuevo_ejercicio.getText().toString().trim().length() > 0);
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
                btGuardarEdit.setEnabled( descripcion_nuevo_ejercicio.getText().toString().trim().length() > 0 );
            }
        });

    }


    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null ){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imagenView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}