package com.fitgym.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


import com.fitgym.R;
import com.fitgym.core.DBManager;
import com.fitgym.core.Ejercicio;

import java.util.ArrayList;

public class ListaEjerciciosActivity extends AppCompatActivity  {

    protected static final int CODIGO_ADICION_EJERCICIO = 100;
    protected static final int CODIGO_EDIT_EJERCICIO= 101;

    protected  ListView lvEjercicios;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.lista_ejercicios);
            this.dbManager = DBManager.get();

            lvEjercicios = (ListView) this.findViewById( R.id.lvEjericios );
            Button btNuevo = (Button) this.findViewById( R.id.btNuevo );


            // Inserta
            btNuevo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent subActividad = new Intent( ListaEjerciciosActivity.this, addEjerciciosActivity.class );
                    subActividad.putExtra( "nombre", "" );
                    subActividad.putExtra( "descripcion", "" );
                    subActividad.putExtra( "imagen","" );
                    ListaEjerciciosActivity.this.startActivityForResult( subActividad, CODIGO_ADICION_EJERCICIO );
                }
            });

            // Modifica
            lvEjercicios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Cursor cursor = ListaEjerciciosActivity.this.mainCursorAdapter.getCursor();
                    if ( cursor.moveToPosition( i ) ) {

                        Intent subActividad = new Intent(ListaEjerciciosActivity.this, editEjerciciosActivity.class);

                        Ejercicio ejercicio = (Ejercicio) ListaEjerciciosActivity.this.mainCursorAdapter.getItem(i);

                        subActividad.putExtra("nombre", cursor.getString(0));
                        subActividad.putExtra("descripcion", cursor.getString(1));
                        subActividad.putExtra("imagen", cursor.getBlob(2));

                        subActividad.putExtra("pos", i);
                        ListaEjerciciosActivity.this.startActivityForResult(subActividad, CODIGO_EDIT_EJERCICIO);

                        return true;
                    }else{

                        String errMsg = "Error en el ejercicio de " + ": " + i;
                        Log.e( "main.modifyContact", errMsg );
                        Toast.makeText( ListaEjerciciosActivity.this, errMsg, Toast.LENGTH_LONG ).show();
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
            this.updateEjercicios();

        }
        if ( requestCode == CODIGO_EDIT_EJERCICIO
                && resultCode == Activity.RESULT_OK )
        {
            int pos = data.getExtras().getInt( "pos" );
            Ejercicio ejer = new Ejercicio( data.getExtras().getString( "nombre").toString() ,data.getExtras().getString( "descripcion").toString(),data.getExtras().getByteArray("imagen"));
            this.dbManager.insertaEjercicio( ejer.getNombre(), ejer.getDescripcion(),ejer.getImagen());
            this.updateEjercicios();

        }

        return;
    }


    @Override
    protected void onStart()
    {
        super.onStart();


        this.lvEjercicios = this.findViewById( R.id.lvEjericios );
        this.mainCursorAdapter = new SimpleCursorAdapter( ListaEjerciciosActivity.this,
                R.layout.lvejercicio_context_menu,
                this.dbManager.getAllEjercicios(),
                new String[]{ dbManager.EJERCICIO_COL_NOMBRE, dbManager.EJERCICIO_COL_DESCRIPCION,dbManager.EJERCICIO_COL_IMAGEN},
                new int[] { R.id.lblNombre, R.id.lblDescripcion ,R.id.imgExercise} );//Sin la ultima columna si que se ejecuta, no consigue transformar bloc a string


        mainCursorAdapter.setViewBinder(new EjercicioViewBinder());

        this.lvEjercicios.setAdapter( this.mainCursorAdapter );

        //No se porque no va
       //this.updateEjercicios();


/*
        this.ejercicios = new ArrayList<>();
        Cursor cursor =  dbManager.getAllEjercicios();
        ejercicios.clear();
        while (cursor.moveToNext()) {

            String nombre = cursor.getString(0);
            String descripcion = cursor.getString(1);
            byte[] imagen = cursor.getBlob(2);

            ejercicios.add(new Ejercicio(nombre, descripcion, imagen));
        }

    //    this.ejercicios = dbManager.getArrayEjercicio();
        // Lista

        this.adaptadorEjercicios = new ListaEjercicioActivityEntryArrayAdapter(
                ListaEjerciciosActivity.this,
                this.ejercicios );
        lvEjercicios.setAdapter( this.adaptadorEjercicios );
        adaptadorEjercicios.notifyDataSetChanged();
*/
    }

    private void updateEjercicios()
    {
        this.mainCursorAdapter.changeCursor( this.dbManager.getAllEjercicios() );
    }

    @Override
    public void onPause()
    {
        super.onPause();
     //   this.mainCursorAdapter.getCursor().close();
        this.dbManager.close();
    }
/*
    private void updateEjercicioList(){
        Cursor cursor =  dbManager.getAllEjercicios();
        ejercicios.clear();
        while (cursor.moveToNext()) {

            String nombre = cursor.getString(0);
            String descripcion = cursor.getString(1);
            byte[] imagen = cursor.getBlob(2);

            ejercicios.add(new Ejercicio(nombre, descripcion, imagen));
        }

      //  this.ejercicios = dbManager.getArrayEjercicio();
        adaptadorEjercicios.notifyDataSetChanged();
    }*/
    class EjercicioViewBinder implements SimpleCursorAdapter.ViewBinder
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
                Toast.makeText(ListaEjerciciosActivity.this, e.toString()+" err", Toast.LENGTH_LONG).show();
            }
            return false;
        }
    }


    private SimpleCursorAdapter mainCursorAdapter;
    private DBManager dbManager;

  //  private ArrayAdapter<Ejercicio> adaptadorEjercicios;
  //  private ArrayList<Ejercicio> ejercicios;
}