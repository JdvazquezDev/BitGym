package com.fitgym.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fitgym.R;
import com.fitgym.core.Ejercicio;

import java.util.ArrayList;

public class ListaEjerciciosActivity extends AppCompatActivity {

        protected static final int CODIGO_ADICION_EJERCICIO = 100;
    protected static final int CODIGO_EDIT_EJERCICIO= 101;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.lista_ejercicios);

            ListView lvEjericios = (ListView) this.findViewById( R.id.lvEjericios );
            Button btNuevo = (Button) this.findViewById( R.id.btNuevo );

            // Lista
            this.ejercicios = new ArrayList<>();
            this.adaptadorEjercicios = new ArrayAdapter<Ejercicio>(
                    this,
                    android.R.layout.simple_selectable_list_item,
                    this.ejercicios );
            lvEjericios.setAdapter( this.adaptadorEjercicios );

            // Inserta
            btNuevo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent subActividad = new Intent( ListaEjerciciosActivity.this, addEjerciciosActivity.class );
                    subActividad.putExtra( "nombre", "" );
                    ListaEjerciciosActivity.this.startActivityForResult( subActividad, CODIGO_ADICION_EJERCICIO );
                }
            });

            // Modifica
            lvEjericios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent subActividad = new Intent( ListaEjerciciosActivity.this, editEjerciciosActivity.class );
                    Ejercicio ejercicio = ListaEjerciciosActivity.this.adaptadorEjercicios.getItem( i );

                    subActividad.putExtra( "nombre", ejercicio.getNombre() );
                    subActividad.putExtra( "pos", i );
                    ListaEjerciciosActivity.this.startActivityForResult( subActividad, CODIGO_EDIT_EJERCICIO );

                    return true;
                }
            });

        }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if ( requestCode == CODIGO_ADICION_EJERCICIO
                && resultCode == Activity.RESULT_OK)
        {

            Ejercicio item = new Ejercicio( data.getExtras().getString( "nombre").toString() );
            this.adaptadorEjercicios.add( item );
            this.updateStatus();

        }
        if ( requestCode == CODIGO_EDIT_EJERCICIO
                && resultCode == Activity.RESULT_OK )
        {
            int pos = data.getExtras().getInt( "pos" );
            Ejercicio ejercicio = new Ejercicio( data.getExtras().getString( "nombre").toString() );


            this.ejercicios.set( pos, ejercicio );
            this.adaptadorEjercicios.notifyDataSetChanged();
        }

        return;
    }

    private void updateStatus()
    {
        TextView lblNum = (TextView) this.findViewById( R.id.lblNum );

        lblNum.setText( Integer.toString( this.adaptadorEjercicios.getCount() ) );
    }

    private ArrayAdapter<Ejercicio> adaptadorEjercicios;
    private ArrayList<Ejercicio> ejercicios;
}