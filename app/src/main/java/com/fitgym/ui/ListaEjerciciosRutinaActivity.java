package com.fitgym.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.fitgym.R;
import com.fitgym.core.DBManager;

public class ListaEjerciciosRutinaActivity extends AppCompatActivity {

    protected static final int CODIGO_ADICION_EJERCICIO_TO_RUTINA = 105;
    protected static final int CODIGO_EDIT_EJERCICIO_TO_RUTINA = 106;

    protected ListView lvRutina;
    protected String fecha;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_rutina);

        this.dbManager = DBManager.get();

        lvRutina = (ListView) this.findViewById( R.id.lvRutina );
        Button btNuevo = (Button) this.findViewById( R.id.btNuevo );
        registerForContextMenu(lvRutina);
        // Inserta
        btNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent subActividad = new Intent( ListaEjerciciosRutinaActivity.this, addEjerciciosToRutinaActivity.class );
                subActividad.putExtra( "_id", "" );
                subActividad.putExtra( "fecha", fecha );
                ListaEjerciciosRutinaActivity.this.startActivityForResult( subActividad, CODIGO_ADICION_EJERCICIO_TO_RUTINA );
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if ( requestCode == CODIGO_ADICION_EJERCICIO_TO_RUTINA
                && resultCode == Activity.RESULT_OK)
        {
            int id = data.getExtras().getInt( "_id");
            fecha = data.getExtras().getString( "fecha");

            int numRepes = 0;
            int series = 0;
            int peso = 0;
            String info = "";
            this.dbManager.insertaEjercicioRutina(id,fecha,numRepes,series,peso,info);
            this.updateRutina();
        }
        if ( requestCode == CODIGO_EDIT_EJERCICIO_TO_RUTINA
                && resultCode == Activity.RESULT_OK)
        {
            int nombre = data.getExtras().getInt( "_id");
            fecha = data.getExtras().getString( "fecha");

            int numRepes = data.getExtras().getInt("numRepes");
            int series = data.getExtras().getInt("series");
            int peso = data.getExtras().getInt("peso");
            String info = data.getExtras().getString("infoExtra").toString();

            this.dbManager.editarEjercicioRutina(nombre,fecha,numRepes,series,peso,info);
            this.updateRutina();
        }
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent datosEnviados = this.getIntent();
        fecha = (String) datosEnviados.getExtras().get("fecha");

        this.lvRutina = this.findViewById(R.id.lvRutina);
        this.mainCursorAdapter = new SimpleCursorAdapter(ListaEjerciciosRutinaActivity.this,
                R.layout.lvrutina_context_menu,
                this.dbManager.getAllEjerRutina(fecha),
                new String[]{"imagen","fecha", "nombre", "num_repeticiones"},
                new int[]{R.id.imgExercise,R.id.lblNombre, R.id.lblNumRepeticion});


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
                  /*  byte[] result = cursor.getBlob(cursor.getColumnIndex("imagen"));//my image is stored as blob in db at 3
                    Bitmap bmp = BitmapFactory.decodeByteArray(result, 0, result.length);
                    ImageView imgExercise=(ImageView)view.findViewById(R.id.imgExercise);
                    imgExercise.setImageBitmap(bmp);*/
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

    //Menu contextual
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual_menu_rutina, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int position = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
        Cursor cursor = ListaEjerciciosRutinaActivity.this.mainCursorAdapter.getCursor();

        switch (item.getItemId()) {
            case R.id.menu_editar:
                if (cursor.moveToPosition(position)) {

                    Intent subActividad = new Intent(ListaEjerciciosRutinaActivity.this, editEjercicioRutinaActivity.class);

                    subActividad.putExtra("_id", cursor.getInt(cursor.getColumnIndex("_id")));
                    subActividad.putExtra("series", cursor.getInt(cursor.getColumnIndex("series")));
                    subActividad.putExtra("repeticiones", cursor.getInt(cursor.getColumnIndex("num_repeticiones")));
                    subActividad.putExtra("peso", cursor.getInt(cursor.getColumnIndex("peso")));
                    subActividad.putExtra("infoExtra", cursor.getString(cursor.getColumnIndex("infoExtra")));
                    subActividad.putExtra( "fecha", fecha );

                    ListaEjerciciosRutinaActivity.this.startActivityForResult(subActividad, CODIGO_EDIT_EJERCICIO_TO_RUTINA);

                } else {
                    String errMsg = "Error en el ejercicio de " + ": " + position;
                    Log.e("main.modifyContact", errMsg);
                }
                return true;

            case R.id.menu_Eliminar:
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                dbManager.eliminaEjercicioRutina(id,fecha);
                updateRutina();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private SimpleCursorAdapter mainCursorAdapter;
    private DBManager dbManager;


}
