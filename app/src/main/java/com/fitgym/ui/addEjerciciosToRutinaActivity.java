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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fitgym.R;
import com.fitgym.core.DBManager;
//import com.fitgym.core.Ejercicio;

import org.w3c.dom.Text;

public class addEjerciciosToRutinaActivity extends AppCompatActivity
{

    protected ListView list_ejercicios_to_add_rutina;
    private String fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_ejercicios_add_to_rutina);
        this.dbManager = DBManager.get();

        list_ejercicios_to_add_rutina = (ListView) this.findViewById( R.id.list_ejercicios_to_add_rutina );

        list_ejercicios_to_add_rutina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Cursor cursor = addEjerciciosToRutinaActivity.this.mainCursorAdapter.getCursor();
                if ( cursor.moveToPosition( i ) ) {

                    Intent datosRetornar = new Intent();
                    datosRetornar.putExtra( "_id", cursor.getInt(0) );
                    datosRetornar.putExtra("fecha",fecha);

                    addEjerciciosToRutinaActivity.this.setResult( Activity.RESULT_OK, datosRetornar );
                    addEjerciciosToRutinaActivity.this.finish();

                    return;
                }else{

                    String errMsg = "Error en el ejercicio de " + ": " + i;
                    Log.e( "main.modifyContact", errMsg );
                    Toast.makeText( addEjerciciosToRutinaActivity.this, errMsg, Toast.LENGTH_LONG ).show();
                    return;
                }
            }
        });



    }


    @Override
    protected void onResume()
    {
        super.onResume();

        Intent datosEnviados = this.getIntent();
        fecha = (String) datosEnviados.getExtras().get("fecha");

        this.list_ejercicios_to_add_rutina = this.findViewById( R.id.list_ejercicios_to_add_rutina );
        this.mainCursorAdapter = new SimpleCursorAdapter( addEjerciciosToRutinaActivity.this,
                R.layout.lvejercicio_context_menu,
                this.dbManager.getAllEjerciciosNotInRutina(fecha),
                new String[]{dbManager.EJERCICIO_COL_NOMBRE, dbManager.EJERCICIO_COL_DESCRIPCION,dbManager.EJERCICIO_COL_IMAGEN},
                new int[] { R.id.lblNombre, R.id.lblDescripcion ,R.id.imgExercise} );

        mainCursorAdapter.setViewBinder(new addEjerciciosToRutinaActivity.EjercicioViewBinder());

        this.list_ejercicios_to_add_rutina.setAdapter( this.mainCursorAdapter );
    }

    @Override
    public void onPause()
    {
        super.onPause();
        //   this.mainCursorAdapter.getCursor().close();
        this.dbManager.close();
    }

    class EjercicioViewBinder implements SimpleCursorAdapter.ViewBinder
    {
        public boolean setViewValue(View view, Cursor cursor, int columnIndex)
        {
            try
            {
                if (view instanceof ImageView)
                {
                   /* byte[] result = cursor.getBlob(cursor.getColumnIndex("imagen"));//my image is stored as blob in db at 3
                    Bitmap bmp = BitmapFactory.decodeByteArray(result, 0, result.length);
                    ImageView imgExercise=(ImageView)view.findViewById(R.id.imgExercise);
                    imgExercise.setImageBitmap(bmp);*/
                    return true;
                }

            }
            catch(Exception e)
            {
                Toast.makeText(addEjerciciosToRutinaActivity.this, e.toString()+" err", Toast.LENGTH_LONG).show();
            }
            return false;
        }
    }


    private SimpleCursorAdapter mainCursorAdapter;
    private DBManager dbManager;

}
