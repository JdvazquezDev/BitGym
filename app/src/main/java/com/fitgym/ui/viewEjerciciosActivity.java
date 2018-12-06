package com.fitgym.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fitgym.R;

public class viewEjerciciosActivity extends AppCompatActivity {
    ImageView imagenView;
    TextView nombre_nuevo_ejercicio;
    TextView urlvideo;
    WebView mWebView;
    TextView descripcion_nuevo_ejercicio;
    String path;
    int id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_exercise);

        final Button btAtras = (Button) this.findViewById(R.id.btAtras);
        nombre_nuevo_ejercicio = (TextView) this.findViewById( R.id.nombre_nuevo_ejercicio );
        imagenView = findViewById(R.id.imageView);
        urlvideo = (TextView) this.findViewById(R.id.urlvideo);
        mWebView = (WebView) findViewById(R.id.webview);
        descripcion_nuevo_ejercicio  = (TextView) this.findViewById( R.id.descripcion_nuevo_ejercicio);


        Intent datosEnviados = this.getIntent();
        id = datosEnviados.getExtras().getInt("_id");

        nombre_nuevo_ejercicio.setText(datosEnviados.getExtras().getString(("nombre")));
        descripcion_nuevo_ejercicio.setText(datosEnviados.getExtras().getString(("descripcion")));
        urlvideo.setText(datosEnviados.getExtras().getString(("url")));
        path = datosEnviados.getExtras().getString(("imagen"));
        final Bitmap bitmap = BitmapFactory.decodeFile(path);
        imagenView.setImageBitmap(bitmap);

        btAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewEjerciciosActivity.this.finish();
            }
        });

        mWebView.getSettings().setJavaScriptEnabled(true);

        String video = urlvideo.getText().toString();
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
