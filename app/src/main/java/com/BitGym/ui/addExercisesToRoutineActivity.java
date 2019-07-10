package com.BitGym.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.BitGym.R;
import com.BitGym.core.DBManager;
//import com.fitgym.core.Ejercicio;

import java.io.File;

public class addExercisesToRoutineActivity extends AppCompatActivity
{

    protected ListView list_ejercicios_to_add_rutina;
    private String fecha;
    protected EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_exercises_add_to_routine);
        this.dbManager = DBManager.get();

        edit = (EditText) this.findViewById(R.id.editView);
        list_ejercicios_to_add_rutina = (ListView) this.findViewById( R.id.list_ejercicios_to_add_rutina );

        list_ejercicios_to_add_rutina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Cursor cursor = addExercisesToRoutineActivity.this.mainCursorAdapter.getCursor();
                if ( cursor.moveToPosition( i ) ) {

                    Intent datosRetornar = new Intent();
                    datosRetornar.putExtra( "_id", cursor.getInt(0) );
                    datosRetornar.putExtra("fecha",fecha);

                    addExercisesToRoutineActivity.this.setResult( Activity.RESULT_OK, datosRetornar );
                    addExercisesToRoutineActivity.this.finish();

                    return;
                }else{

                    String errMsg = "Error en el ejercicio de " + ": " + i;
                    Log.e( "main.modifyContact", errMsg );
                    Toast.makeText( addExercisesToRoutineActivity.this, errMsg, Toast.LENGTH_LONG ).show();
                    return;
                }
            }
        });

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mainCursorAdapter.getFilter().filter(s.toString());
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
        this.mainCursorAdapter = new SimpleCursorAdapter( addExercisesToRoutineActivity.this,
                R.layout.lvexercise_context_menu,
                this.dbManager.getAllEjerciciosNotInRutina(fecha),
                new String[]{dbManager.EJERCICIO_COL_NOMBRE, dbManager.EJERCICIO_COL_DESCRIPCION,dbManager.EJERCICIO_COL_IMAGEN},
                new int[] { R.id.lblNombre, R.id.imgExercise} );

        mainCursorAdapter.setViewBinder(new addExercisesToRoutineActivity.EjercicioViewBinder());

        this.list_ejercicios_to_add_rutina.setAdapter( this.mainCursorAdapter );

        mainCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return dbManager.searchExerciseNotInRutina(constraint.toString(),fecha);
            }
        });
    }

    @Override
    public void onPause()
    {
        super.onPause();
        this.mainCursorAdapter.getCursor().close();
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
                    String path = cursor.getString(cursor.getColumnIndex("imagen"));
                    File imgFile = new File(path);
                    Bitmap bm = BitmapFactory.decodeFile(imgFile.getPath());
                    bm =resizeImage(addExercisesToRoutineActivity.this,bm,400,250);

                    ImageView imgExercise = (ImageView) view.findViewById(R.id.imgExercise);
                    imgExercise.setImageBitmap(bm);
                    return true;
                }
            }
            catch(Exception e)
            {
                Toast.makeText(addExercisesToRoutineActivity.this, e.toString()+" err", Toast.LENGTH_LONG).show();
            }
            return false;
        }
    }

    public Bitmap resizeImage(Context ctx, Bitmap BitmapOrg, int w, int h) {

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        // calculamos el escalado de la imagen destino
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // para poder manipular la imagen
        // debemos crear una matriz

        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);

        // volvemos a crear la imagen con los nuevos valores
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0,
                width, height, matrix, true);

        // si queremos poder mostrar nuestra imagen tenemos que crear un
        // objeto drawable y así asignarlo a un botón, imageview...
        return resizedBitmap;

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
    private SimpleCursorAdapter mainCursorAdapter;
    private DBManager dbManager;

}
