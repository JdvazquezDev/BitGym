package com.fitgym.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.fitgym.R;
import com.fitgym.core.DBManager;

import java.io.File;

public class ListaEjerciciosActivity extends AppCompatActivity  {

    protected static final int CODIGO_ADICION_EJERCICIO = 100;
    protected static final int CODIGO_EDIT_EJERCICIO= 101;

    protected  ListView lvEjercicios;
    protected EditText edit;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.lista_ejercicios);

            this.dbManager = DBManager.get();

            edit = (EditText) this.findViewById(R.id.editView);
            lvEjercicios = (ListView) this.findViewById( R.id.lvEjercicios );
            Button btNuevo = (Button) this.findViewById( R.id.btNuevo );
            lvEjercicios.setAdapter(mainCursorAdapter);
            registerForContextMenu(lvEjercicios);

            // Inserta
            btNuevo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent subActividad = new Intent( ListaEjerciciosActivity.this, addEjerciciosActivity.class );
                    subActividad.putExtra( "nombre", "" );
                    subActividad.putExtra( "descripcion", "" );
                    subActividad.putExtra( "imagen","" );
                    ListaEjerciciosActivity.this.startActivityForResult(subActividad, CODIGO_ADICION_EJERCICIO);
                }
            });
          /*  lvEjercicios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Cursor cursor = ListaEjerciciosActivity.this.mainCursorAdapter.getCursor();
                    if ( cursor.moveToPosition( position ) ) {

                        Intent subActividad = new Intent(ListaEjerciciosActivity.this, viewEjerciciosActivity.class);
                        startActivity(subActividad);
                    }else{
                        String errMsg = "Error en el ejercicio de " + ": " + position;
                        Log.e( "main.modifyContact", errMsg );
                        Toast.makeText( ListaEjerciciosActivity.this, errMsg, Toast.LENGTH_LONG ).show();
                    }
                }
            });*/

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if ( requestCode == CODIGO_ADICION_EJERCICIO
                && resultCode == Activity.RESULT_OK)
        {
            this.dbManager.insertaEjercicio( data.getExtras().getString( "nombre").toString(), data.getExtras().getString( "descripcion").toString(),data.getExtras().getString("imagen"));
            this.updateEjercicios();
        }
        if ( requestCode == CODIGO_EDIT_EJERCICIO
                && resultCode == Activity.RESULT_OK )
        {
            this.dbManager.editEjercicio( data.getExtras().getInt( "_id"),data.getExtras().getString( "nombre").toString(), data.getExtras().getString( "descripcion").toString(),data.getExtras().getString("imagen"));
            this.updateEjercicios();
        }
        return;
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        this.lvEjercicios = this.findViewById( R.id.lvEjercicios );
        this.mainCursorAdapter = new SimpleCursorAdapter( ListaEjerciciosActivity.this,
                R.layout.lvejercicio_context_menu,
                this.dbManager.getAllEjercicios(),
                new String[]{ dbManager.EJERCICIO_COL_NOMBRE, dbManager.EJERCICIO_COL_DESCRIPCION,dbManager.EJERCICIO_COL_IMAGEN},
                new int[] {R.id.lblNombre, R.id.imgExercise} );//Sin la ultima columna si que se ejecuta, no consigue transformar bloc a string

        mainCursorAdapter.setViewBinder(new EjercicioViewBinder());
        this.lvEjercicios.setAdapter( this.mainCursorAdapter );

        mainCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return dbManager.searchExercise(constraint.toString());
            }
        });
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
                    String path = cursor.getString(cursor.getColumnIndex("imagen"));
                    File imgFile = new File(path);
                    Bitmap bm = BitmapFactory.decodeFile(imgFile.getPath());

                    bm =resizeImage(ListaEjerciciosActivity.this,bm,450,300);
                    ImageView imgExercise = (ImageView) view.findViewById(R.id.imgExercise);
                    imgExercise.setImageBitmap(bm);
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
    //Menu contextual
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int position = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
        Cursor cursor = ListaEjerciciosActivity.this.mainCursorAdapter.getCursor();

        switch (item.getItemId()) {
            case R.id.menu_editar:
                if (cursor.moveToPosition(position)) {

                    Intent subActividad = new Intent(ListaEjerciciosActivity.this, editEjerciciosActivity.class);

                    subActividad.putExtra("_id", cursor.getInt(0));
                    subActividad.putExtra("nombre", cursor.getString(1));
                    subActividad.putExtra("descripcion", cursor.getString(2));
                    subActividad.putExtra("imagen", cursor.getString(3));
                    ListaEjerciciosActivity.this.startActivityForResult(subActividad, CODIGO_EDIT_EJERCICIO);

                } else {
                    String errMsg = "Error en el ejercicio de " + ": " + position;
                    Log.e("main.modifyContact", errMsg);
                }
                return true;

            case R.id.menu_Eliminar:
                int id = cursor.getInt(0);

                dbManager.eliminaEjercicio(id);
                updateEjercicios();
                return true;
            default:
            return super.onContextItemSelected(item);
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