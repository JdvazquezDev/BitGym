package com.fitgym.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
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


            this.dbManager = ( (MiApp) this.getApplication() ).getBD();


             lvEjercicios = (ListView) this.findViewById( R.id.lvEjericios );
            Button btNuevo = (Button) this.findViewById( R.id.btNuevo );

            // Lista
           /* this.ejercicios = new ArrayList<>();
            this.adaptadorEjercicios = new ArrayAdapter<Ejercicio>(
                    this,
                    android.R.layout.simple_selectable_list_item,
                    this.ejercicios );
            lvEjericios.setAdapter( this.adaptadorEjercicios );
*/
            // Inserta
            btNuevo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent subActividad = new Intent( ListaEjerciciosActivity.this, addEjerciciosActivity.class );
                    subActividad.putExtra( "nombre", "" );
                    subActividad.putExtra( "descripcion", "" );
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

            Ejercicio ejer = new Ejercicio( data.getExtras().getString( "nombre").toString() ,data.getExtras().getString( "descripcion").toString());
            this.dbManager.insertaEjercicio( ejer.getNombre(), ejer.getDescripcion());
            this.updateEjercicios();

            //this.adaptadorEjercicios.add( ejer );
          //  this.updateStatus();

        }
        if ( requestCode == CODIGO_EDIT_EJERCICIO
                && resultCode == Activity.RESULT_OK )
        {
            int pos = data.getExtras().getInt( "pos" );
            Ejercicio ejer = new Ejercicio( data.getExtras().getString( "nombre").toString() ,data.getExtras().getString( "descripcion").toString());
            this.dbManager.insertaEjercicio( ejer.getNombre(), ejer.getDescripcion());
            this.updateEjercicios();

          ///  this.ejercicios.set( pos, ejer );
          //  this.adaptadorEjercicios.notifyDataSetChanged();
        }

        return;
    }

   /* private void updateStatus()
    {
        TextView lblNum = (TextView) this.findViewById( R.id.lblNum );
        lblNum.setText( Integer.toString( this.mainCursorAdapter.getCount() ) );
    }
*/


    @Override
    protected void onStart()
    {
        super.onStart();


        this.lvEjercicios = this.findViewById( R.id.lvEjericios );
        this.mainCursorAdapter = new SimpleCursorAdapter( this,
                R.layout.lvejercicio_context_menu,

                null,
                new String[]{ dbManager.EJERCICIO_COL_NOMBRE, dbManager.EJERCICIO_COL_DESCRIPCION },
                new int[] { R.id.lblNombre, R.id.lblDescripcion } );

        this.lvEjercicios.setAdapter( this.mainCursorAdapter );

        this.updateEjercicios();


    }

    private void updateEjercicios()
    {
        this.mainCursorAdapter.changeCursor( this.dbManager.getAllEjercicios() );
    }

    @Override
    public void onPause()
    {
        super.onPause();
        this.mainCursorAdapter.getCursor().close();
        this.dbManager.close();
    }


    private CursorAdapter mainCursorAdapter;
    private DBManager dbManager;

 //   private ArrayAdapter<Ejercicio> adaptadorEjercicios;
 //  private ArrayList<Ejercicio> ejercicios;
}