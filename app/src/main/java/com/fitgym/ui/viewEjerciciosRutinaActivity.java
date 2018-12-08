package com.fitgym.ui;

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
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fitgym.R;

public class viewEjerciciosRutinaActivity extends AppCompatActivity {
    Boolean resume = false;
    long elapsedTime;
    Chronometer cmTimer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_exercise_rutina);

        final Button btCancelar = (Button) this.findViewById(R.id.btCancelar2);

        final TextView numero_series = (TextView) this.findViewById(R.id.series);
        final TextView numero_repeticiones = (TextView) this.findViewById(R.id.repeticiones);
        final TextView peso = (TextView) this.findViewById(R.id.peso);
        final TextView info_extra = (TextView) this.findViewById(R.id.informacion_extra);
        final TextView cmTimer = (TextView) this.findViewById(R.id.cmTimer);

        Intent datosEnviados = this.getIntent();
        final int nombre = datosEnviados.getExtras().getInt("_id");
        final String fecha = (String) datosEnviados.getExtras().get("fecha");


        numero_series.setText(String.valueOf(datosEnviados.getExtras().getInt("series")));
        numero_repeticiones.setText(String.valueOf(datosEnviados.getExtras().getInt("repeticiones")));
        peso.setText(String.valueOf(datosEnviados.getExtras().getInt("peso")));
        info_extra.setText(datosEnviados.getExtras().getString("infoExtra"));
        cmTimer.setText(datosEnviados.getExtras().getString("tiempo"));

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewEjerciciosRutinaActivity.this.finish();
            }
        });

    }
        public boolean onCreateOptionsMenu (Menu menu)
        {
            super.onCreateOptionsMenu(menu);
            this.getMenuInflater().inflate(R.menu.activity_actions, menu);
            return true;
        }
        public boolean onOptionsItemSelected (MenuItem menuItem)
        {
            boolean toret = false;
            switch (menuItem.getItemId()) {
                case R.id.action_atras:
                    this.finish();
                    break;
            }
            return toret;
        }


}
