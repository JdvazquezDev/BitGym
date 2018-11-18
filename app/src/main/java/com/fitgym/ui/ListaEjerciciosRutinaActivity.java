package com.fitgym.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
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

        this.dbManager = DBManager.get();


        lvRutina = (ListView) this.findViewById( R.id.lvRutina );
        Button btNuevo = (Button) this.findViewById( R.id.btNuevo );


        // Inserta
        btNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cambiar por la actividad de seleccionar ejercicios
                Intent subActividad = new Intent( ListaEjerciciosRutinaActivity.this, addEjerciciosActivity.class );
                subActividad.putExtra( "nombre", "" );
                ListaEjerciciosRutinaActivity.this.startActivityForResult( subActividad, CODIGO_ADICION_EJERCICIO );
            }
        });
/*
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
*/
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

        Intent datosEnviados = this.getIntent();
        fecha = (Date) datosEnviados.getExtras().get("fecha");

        dbManager.insertaRutina(fecha);

        this.lvRutina = this.findViewById( R.id.lvRutina );
        this.mainCursorAdapter = new SimpleCursorAdapter( ListaEjerciciosRutinaActivity.this,
                R.layout.lvrutina_context_menu,

                this.dbManager.getAllEjerRutina(fecha),
                new String[]{ dbManager.EJERCICIO_COL_IMAGEN, dbManager.EJERCICIO_RUTINA_COL_NOMBRE,dbManager.EJERCICIO_RUTINA_COL_REPETICIONES },
                new int[] { R.id.imgExercise, R.id.lblNombre, R.id.lblNumRepeticion } );

        mainCursorAdapter.setViewBinder(new RutinaViewBinder());

        this.lvRutina.setAdapter(this.mainCursorAdapter);

    }

    private void updateRutina()
    {
        this.mainCursorAdapter.changeCursor( this.dbManager.getAllEjerRutina(fecha) );
    }
    @Override
    public void onPause()
    {
        super.onPause();
        // this.mainCursorAdapter.getCursor().close();
        this.dbManager.close();
    }

    class RutinaViewBinder implements SimpleCursorAdapter.ViewBinder
    {
        public boolean setViewValue(View view, Cursor cursor, int columnIndex)
        {
            try
            {
                if (view instanceof ImageView)
                {
                    byte[] result = cursor.getBlob(cursor.getColumnIndex("imagen"));//my image is stored as blob in db at 3
                    Bitmap bmp = BitmapFactory.decodeByteArray(result, 0, result.length);
                    ImageView imgExercise=(ImageView)view.findViewById(R.id.imgExercise);
                    imgExercise.setImageBitmap(bmp);
                    return true;
                }

            }
            catch(Exception e)
            {
                Toast.makeText(ListaEjerciciosRutinaActivity.this, e.toString()+" err", Toast.LENGTH_LONG).show();
            }
            return false;
        }
    }

    private SimpleCursorAdapter mainCursorAdapter;
    private DBManager dbManager;

    //private ArrayAdapter<Ejercicio> adaptadorRutina;
    //private ArrayList<Ejercicio> ejercicios;
}
