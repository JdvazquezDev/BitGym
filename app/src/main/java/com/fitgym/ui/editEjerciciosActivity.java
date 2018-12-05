package com.fitgym.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fitgym.R;
import com.fitgym.core.DBManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;


public class editEjerciciosActivity extends AppCompatActivity {

    final int REQUEST_CODE_GALLERY = 999;
    ImageView imagenView;
    File file;
    String path;
    int id;
    WebView mWebView;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_ejercicio_to_lista_ejercicios);

        final Button btGuardarEdit = (Button) this.findViewById( R.id.btGuardarEdit );
        final Button btCancelar = (Button) this.findViewById( R.id.btCancelar );
        final Button btImagen = (Button) this.findViewById(R.id.btImagen);

        final EditText nombre_nuevo_ejercicio = (EditText) this.findViewById( R.id.nombre_nuevo_ejercicio );
        final EditText descripcion_nuevo_ejercicio = (EditText) this.findViewById( R.id.descripcion_nuevo_ejercicio);
        final EditText urlVideo = (EditText) this.findViewById(R.id.urlvideo);
        imagenView = findViewById(R.id.imageView);
        mWebView = (WebView) findViewById(R.id.webview);


        mWebView.getSettings().setJavaScriptEnabled(true);



        String nombreDirectorioPublico = "imagen";
        file = crearDirectorioPublico(this,nombreDirectorioPublico);

        Intent datosEnviados = this.getIntent();
        id = datosEnviados.getExtras().getInt("_id");

        nombre_nuevo_ejercicio.setText(datosEnviados.getExtras().getString(("nombre")));
        descripcion_nuevo_ejercicio.setText(datosEnviados.getExtras().getString(("descripcion")));
        urlVideo.setText(datosEnviados.getExtras().getString(("url")));
        path = datosEnviados.getExtras().getString(("imagen"));
        final Bitmap bitmap = BitmapFactory.decodeFile(path);
        imagenView.setImageBitmap(bitmap);

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
        String video = urlVideo.getText().toString();
        if(video.contains("v=")) {
            String[] parts = video.split("v=");
            String urlId = parts[1];
            String playVideo = "<html><body> <iframe class=\"youtube-player\" type=\"text/html\" width=\"100%\" height=\"250\" src=\"https://www.youtube.com/embed/" + urlId + "\" frameborder=\"1\"></body></html>";

            mWebView.loadData(playVideo, "text/html", "utf-8");
            mWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // YouTube video link
                    if (url.startsWith("vnd.youtube:")) {
                        int n = url.indexOf("?");
                        if (n > 0) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("http://www.youtube.com/v/%s", url.substring("vnd.youtube:".length(), n)))));
                        }
                        return (true);
                    }

                    return (false);
                }
            });
        }
        if(video.contains("be/")) {
            String[] parts = video.split("be/");
            String urlId = parts[1];
            String playVideo = "<html><body> <iframe class=\"youtube-player\" type=\"text/html\" width=\"100%\" height=\"250\" src=\"https://www.youtube.com/embed/" + urlId + "\" frameborder=\"0\"></body></html>";

            mWebView.loadData(playVideo, "text/html", "utf-8");
            mWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // YouTube video link
                    if (url.startsWith("vnd.youtube:")) {
                        int n = url.indexOf("?");
                        if (n > 0) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("http://www.youtube.com/v/%s", url.substring("vnd.youtube:".length(), n)))));
                        }
                        return (true);
                    }

                    return (false);
                }
            });
        }
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

                datosRetornar.putExtra( "_id", id );
                datosRetornar.putExtra( "nombre", nombre_nuevo_ejercicio.getText().toString() );
                datosRetornar.putExtra( "descripcion", descripcion_nuevo_ejercicio.getText().toString() );
                datosRetornar.putExtra( "imagen", path );
                datosRetornar.putExtra("url", urlVideo.getText().toString());
                SaveImage(bitmap);
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
            }
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
        Toast.makeText(editEjerciciosActivity.this, "¡No se ha podido guardar la imagen!", Toast.LENGTH_SHORT).show();
    }

    private void AbleToSave() {
        Toast.makeText(editEjerciciosActivity.this, "Imagen guardada", Toast.LENGTH_SHORT).show();
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