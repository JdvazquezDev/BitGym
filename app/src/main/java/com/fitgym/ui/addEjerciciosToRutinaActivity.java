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
import android.widget.Toast;

import com.fitgym.R;
import com.fitgym.core.DBManager;
import com.fitgym.core.Ejercicio;

public class addEjerciciosToRutinaActivity extends AppCompatActivity
{
    protected static final int CODIGO_ADICION_EJERCICIO = 100;
    protected static final int CODIGO_EDIT_EJERCICIO= 101;
    protected ListView list_ejercicios_to_add_rutina;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_ejercicios_add_to_rutina);
        this.dbManager = DBManager.get();

        list_ejercicios_to_add_rutina = (ListView) this.findViewById( R.id.list_ejercicios_to_add_rutina );





    }


    @Override
    protected void onResume()
    {
        super.onResume();


        this.list_ejercicios_to_add_rutina = this.findViewById( R.id.list_ejercicios_to_add_rutina );
        this.mainCursorAdapter = new SimpleCursorAdapter( addEjerciciosToRutinaActivity.this,
                R.layout.lvejercicio_context_menu,
                this.dbManager.getAllEjercicios(),
                new String[]{ dbManager.EJERCICIO_COL_NOMBRE, dbManager.EJERCICIO_COL_DESCRIPCION,dbManager.EJERCICIO_COL_IMAGEN},
                new int[] { R.id.lblNombre, R.id.lblDescripcion ,R.id.imgExercise} );

        mainCursorAdapter.setViewBinder(new addEjerciciosToRutinaActivity.EjercicioViewBinder());

        this.list_ejercicios_to_add_rutina.setAdapter( this.mainCursorAdapter );
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
                Toast.makeText(addEjerciciosToRutinaActivity.this, e.toString()+" err", Toast.LENGTH_LONG).show();
            }
            return false;
        }
    }


    private SimpleCursorAdapter mainCursorAdapter;
    private DBManager dbManager;

}
