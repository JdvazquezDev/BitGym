package com.fitgym.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fitgym.R;
import com.fitgym.core.DBManager;
import com.fitgym.core.Ejercicio;
import com.fitgym.core.Rutina;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ListaEjerciciosRutinaActivity extends AppCompatActivity {

    protected static final int CODIGO_ADICION_EJERCICIO = 100;
    protected static final int CODIGO_EDIT_EJERCICIO= 101;
    protected ListView lvRutina;
    protected Date fecha;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_rutina);

        this.dbManager = ( (MiApp) this.getApplication() ).getBD();


        lvRutina = (ListView) this.findViewById( R.id.lvRutina );
        Button btNuevo = (Button) this.findViewById( R.id.btNuevo );

        Intent datosEnviados = this.getIntent();
        fecha = (Date) datosEnviados.getExtras().get("fecha");

/*
        if(this.ejercicios == null){
            this.ejercicios = new ArrayList<>();
            CalendarioRutinaActivity.ejericios.addRutina(fecha,new Rutina(this.ejercicios));
        }
*/
        // Inserta
        btNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cambiar por la actividad de seleccionar ejercicios
                Intent subActividad = new Intent( ListaEjerciciosRutinaActivity.this, addEjerciciosActivity.class );
                subActividad.putExtra( "nombre", "" );
                subActividad.putExtra( "descripcion", "" );
                ListaEjerciciosRutinaActivity.this.startActivityForResult( subActividad, CODIGO_ADICION_EJERCICIO );
            }
        });

        // Modifica
        lvRutina.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Cambiar por la actividad de editar ejercicios de la rutina

                Cursor cursor = ListaEjerciciosRutinaActivity.this.mainCursorAdapter.getCursor();
                if ( cursor.moveToPosition( i ) ) {

                    Intent subActividad = new Intent(ListaEjerciciosRutinaActivity.this, editEjerciciosActivity.class);

                    Ejercicio ejercicio = (Ejercicio) ListaEjerciciosRutinaActivity.this.mainCursorAdapter.getItem(i);

                    subActividad.putExtra("nombre", cursor.getString(0));
                    subActividad.putExtra("descripcion", cursor.getString(1));
                    subActividad.putExtra("pos", i);
                    ListaEjerciciosRutinaActivity.this.startActivityForResult(subActividad, CODIGO_EDIT_EJERCICIO);

                    return true;
                }else{

                    String errMsg = "Error en el ejercicio de " + ": " + i;
                    Log.e( "main.modifyContact", errMsg );
                    Toast.makeText( ListaEjerciciosRutinaActivity.this, errMsg, Toast.LENGTH_LONG ).show();
                    return false;
                }
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if ( requestCode == CODIGO_ADICION_EJERCICIO
                && resultCode == Activity.RESULT_OK)
        {
            Ejercicio ejer = new Ejercicio( data.getExtras().getString( "nombre").toString() ,data.getExtras().getString( "descripcion").toString(),data.getExtras().getByteArray("imagen"));
            this.dbManager.insertaEjercicio( ejer.getNombre(), ejer.getDescripcion(),ejer.getImagen());
            this.updateRutina();

        }
        if ( requestCode == CODIGO_EDIT_EJERCICIO
                && resultCode == Activity.RESULT_OK )
        {
            int pos = data.getExtras().getInt( "pos" );
            Ejercicio ejer = new Ejercicio( data.getExtras().getString( "nombre").toString() ,data.getExtras().getString( "descripcion").toString(),data.getExtras().getByteArray("imagen"));
            this.dbManager.insertaEjercicio( ejer.getNombre(), ejer.getDescripcion(),ejer.getImagen());
            this.updateRutina();
        }

        return;
    }



    @Override
    protected void onStart()
    {
        super.onStart();


        this.lvRutina = this.findViewById( R.id.lvRutina );
        this.mainCursorAdapter = new SimpleCursorAdapter( this,
                R.layout.lvejercicio_context_menu,

                null,
                new String[]{ dbManager.EJERCICIO_COL_NOMBRE, dbManager.EJERCICIO_COL_DESCRIPCION },
                new int[] { R.id.lblNombre, R.id.lblDescripcion } );

        this.lvRutina.setAdapter( this.mainCursorAdapter );

        this.updateRutina();

    }

    private void updateRutina()
    {
        this.mainCursorAdapter.changeCursor( this.dbManager.getAllEjercicios() );
    }

    private CursorAdapter mainCursorAdapter;
    private DBManager dbManager;

    //private ArrayAdapter<Ejercicio> adaptadorRutina;
    //private ArrayList<Ejercicio> ejercicios;
}
