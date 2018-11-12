package com.fitgym.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fitgym.R;
import com.fitgym.core.Ejercicio;
import com.fitgym.core.Rutina;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ListaEjerciciosRutinaActivity extends AppCompatActivity {

    protected static final int CODIGO_ADICION_EJERCICIO = 100;
    protected static final int CODIGO_EDIT_EJERCICIO= 101;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_rutina);

        ListView lvEjericios = (ListView) this.findViewById( R.id.lvEjericios );
        Button btNuevo = (Button) this.findViewById( R.id.btNuevo );

        Intent datosEnviados = this.getIntent();
        Date fecha = (Date) datosEnviados.getExtras().get("fecha");
        this.ejercicios = CalendarioRutinaActivity.ejericios.getRutinas(fecha).getEjercicios();
        if(this.ejercicios == null){
            this.ejercicios = new ArrayList<>();
            CalendarioRutinaActivity.ejericios.addRutina(fecha,new Rutina(this.ejercicios));
        }

        // Lista
       // this.ejercicios = new ArrayList<>();//Cargar la lista de ejercicios
        this.adaptadorRutina = new ArrayAdapter<Ejercicio>(
                this,
                android.R.layout.simple_selectable_list_item,
                this.ejercicios );
        lvEjericios.setAdapter( this.adaptadorRutina );

        // Inserta
        btNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent subActividad = new Intent( ListaEjerciciosRutinaActivity.this, addEjerciciosActivity.class );
                subActividad.putExtra( "nombre", "" );
                subActividad.putExtra( "descripcion", "" );
                ListaEjerciciosRutinaActivity.this.startActivityForResult( subActividad, CODIGO_ADICION_EJERCICIO );
            }
        });

        // Modifica
        lvEjericios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent subActividad = new Intent( ListaEjerciciosRutinaActivity.this, editEjerciciosActivity.class );
                Ejercicio ejercicio = ListaEjerciciosRutinaActivity.this.adaptadorRutina.getItem( i );

                subActividad.putExtra( "nombre", ejercicio.getNombre() );
                subActividad.putExtra("descripcion",ejercicio.getDescripcion());
                subActividad.putExtra( "pos", i );
                ListaEjerciciosRutinaActivity.this.startActivityForResult( subActividad, CODIGO_EDIT_EJERCICIO );

                return true;
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if ( requestCode == CODIGO_ADICION_EJERCICIO
                && resultCode == Activity.RESULT_OK)
        {

            Ejercicio item = new Ejercicio( data.getExtras().getString( "nombre").toString() ,data.getExtras().getString( "descripcion").toString());
            this.adaptadorRutina.add( item );

        }
        if ( requestCode == CODIGO_EDIT_EJERCICIO
                && resultCode == Activity.RESULT_OK )
        {
            int pos = data.getExtras().getInt( "pos" );
            Ejercicio ejercicio = new Ejercicio( data.getExtras().getString( "nombre").toString() ,data.getExtras().getString( "descripcion").toString());


            this.ejercicios.set( pos, ejercicio );
            this.adaptadorRutina.notifyDataSetChanged();
        }

        return;
    }


    private ArrayAdapter<Ejercicio> adaptadorRutina;
    private ArrayList<Ejercicio> ejercicios;
}
