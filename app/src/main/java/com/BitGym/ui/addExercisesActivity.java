package com.BitGym.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.BitGym.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class addExercisesActivity extends AppCompatActivity {

    final int REQUEST_CODE_GALLERY = 999;
    final int REQUEST_CODE_GALLERY_VIDEO = 998;
    ImageView imagenView;

    File file;
    String path;
    Bitmap bitmap;
    private WebView mWebView;
    boolean estaImagen;
    Button btGuardarAdd;
    EditText nombre_nuevo_ejercicio;
    EditText descripcion_nuevo_ejercicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exercise_to_list_exercises);

        btGuardarAdd = (Button) this.findViewById( R.id.btGuardarAdd );
        final Button btCancelar = (Button) this.findViewById( R.id.btCancelar );
        final Button btImagen = (Button) this.findViewById(R.id.btImagen);

        nombre_nuevo_ejercicio = (EditText) this.findViewById( R.id.nombre_nuevo_ejercicio );
        descripcion_nuevo_ejercicio = (EditText) this.findViewById( R.id.descripcion_nuevo_ejercicio);
        final EditText urlVideo = (EditText) this.findViewById(R.id.urlvideo);
        imagenView = (ImageView) findViewById(R.id.imageView);
        estaImagen = false;
        mWebView = (WebView) findViewById(R.id.webview);

        mWebView.getSettings().setJavaScriptEnabled(true);

        String nombreDirectorioPublico = "imagen";
        file = crearDirectorioPublico(this,nombreDirectorioPublico);

        if (!file.exists()) {
            file.mkdir();
            Log.i("direr","se creo -- " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
        }else{
            Log.i("direr","esta creado -- " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
        }

        Intent datosEnviados = this.getIntent();
        nombre_nuevo_ejercicio.setText( datosEnviados.getExtras().getString(( "nombre" ) ) );
        descripcion_nuevo_ejercicio.setText(datosEnviados.getExtras().getString(("descripcion")));

       btImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        addExercisesActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });
        String video = urlVideo.getText().toString();

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExercisesActivity.this.setResult( Activity.RESULT_CANCELED );
                addExercisesActivity.this.finish();
            }
        });

        btGuardarAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent datosRetornar = new Intent();
                datosRetornar.putExtra( "nombre", nombre_nuevo_ejercicio.getText().toString() );
                datosRetornar.putExtra( "descripcion", descripcion_nuevo_ejercicio.getText().toString() );
                SaveImage(bitmap);
                datosRetornar.putExtra("imagen", path);
                datosRetornar.putExtra("url", urlVideo.getText().toString());

                addExercisesActivity.this.setResult( Activity.RESULT_OK, datosRetornar );
                addExercisesActivity.this.finish();
            }
        });
        btGuardarAdd.setEnabled( false );

        nombre_nuevo_ejercicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btGuardarAdd.setEnabled(nombre_nuevo_ejercicio.getText().toString().trim().length() > 0 &&   descripcion_nuevo_ejercicio.getText().toString().trim().length() > 0 && estaImagen
                );
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                btGuardarAdd.setEnabled(  nombre_nuevo_ejercicio.getText().toString().trim().length() > 0 && descripcion_nuevo_ejercicio.getText().toString().trim().length() > 0 && estaImagen);
            }
        });

        urlVideo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btGuardarAdd.setEnabled(
                        nombre_nuevo_ejercicio.getText().toString().trim().length() > 0 &&   descripcion_nuevo_ejercicio.getText().toString().trim().length() > 0 && estaImagen);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btGuardarAdd.setEnabled( nombre_nuevo_ejercicio.getText().toString().trim().length() > 0 &&  descripcion_nuevo_ejercicio.getText().toString().trim().length() > 0 && estaImagen);
            }
        });


        descripcion_nuevo_ejercicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btGuardarAdd.setEnabled(nombre_nuevo_ejercicio.getText().toString().trim().length() > 0   &&  descripcion_nuevo_ejercicio.getText().toString().trim().length() > 0 && estaImagen);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                btGuardarAdd.setEnabled(nombre_nuevo_ejercicio.getText().toString().trim().length() > 0 &&  descripcion_nuevo_ejercicio.getText().toString().trim().length() > 0 && estaImagen);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        if(requestCode == REQUEST_CODE_GALLERY_VIDEO){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
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

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri imagenSeleccionada = data.getData();
            String[] fillPath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imagenSeleccionada, fillPath, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(fillPath[0]));
            cursor.close();
            bitmap = BitmapFactory.decodeFile(path);
            imagenView.setImageBitmap(bitmap);
            estaImagen = true;

            btGuardarAdd.setEnabled(nombre_nuevo_ejercicio.getText().toString().trim().length() > 0 &&  descripcion_nuevo_ejercicio.getText().toString().trim().length() > 0 && estaImagen);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public File crearDirectorioPublico(Context context, String nombreDirectorio) {
       File directorio =new File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                nombreDirectorio);
        return directorio;
    }

    public void SaveImage( Bitmap ImageToSave) {

        File dir = file;//Carpeta donde se guarda
        File fichero = new File(path);//Fichero a guardar

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File paraGuardar = new File(dir, fichero.getName());//Fichero donde se guarda

        try {
            FileOutputStream fOut = new FileOutputStream(paraGuardar);

            ImageToSave.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            AbleToSave();
        }
        catch(FileNotFoundException e) {
            UnableToSave();
        }
        catch(IOException e) {
            UnableToSave();
        }
    }

    private void UnableToSave() {
        Toast.makeText(addExercisesActivity.this, "¡No se ha podido guardar la imagen!", Toast.LENGTH_SHORT).show();
    }

    private void AbleToSave() {
        Toast.makeText(addExercisesActivity.this, "Imagen guardada", Toast.LENGTH_SHORT).show();
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
}