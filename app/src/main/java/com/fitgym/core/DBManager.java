package com.fitgym.core;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Blob;
import java.util.ArrayList;

/** Maneja el acceso a la base de datos. */
public class DBManager extends SQLiteOpenHelper {
    public static final String DB_NOMBRE = "GYM";
    public static final int DB_VERSION = 2;

    public static final String TABLA_EJERCICIO;
    public static final String EJERCICIO_COL_NOMBRE; //nombre
    public static final String EJERCICIO_COL_DESCRIPCION;
    public static final String EJERCICIO_COL_IMAGEN;
    private static DBManager dbManager;

    static {
        TABLA_EJERCICIO = "ejercicio";
        EJERCICIO_COL_NOMBRE = "_id";
        EJERCICIO_COL_DESCRIPCION = "descripcion";
        EJERCICIO_COL_IMAGEN = "imagen";
    }


    private DBManager(Context context)
    {
        super( context, DB_NOMBRE, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        Log.i(  "DBManager",
                "Creando BBDD " + DB_NOMBRE + " creating: " + TABLA_EJERCICIO);
        try {
            db.beginTransaction();
            db.execSQL( "CREATE TABLE IF NOT EXISTS " + TABLA_EJERCICIO + "( "
                    + EJERCICIO_COL_NOMBRE + " string(255) PRIMARY KEY NOT NULL, "
                    + EJERCICIO_COL_DESCRIPCION + " string(255) NOT NULL,"
                    + EJERCICIO_COL_IMAGEN + " blob NOT NULL" +
                    ")");
            db.setTransactionSuccessful();

        }
        catch(SQLException exc)
        {
            Log.e( "DBManager.onCreate", exc.getMessage() );
        }
        finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i(  "DBManager",
                "DB: " + DB_NOMBRE + ": v" + oldVersion + " -> v" + newVersion );

        try {
            db.beginTransaction();
            db.execSQL( "DROP TABLE IF EXISTS " + TABLA_EJERCICIO );
            db.setTransactionSuccessful();
        }  catch(SQLException exc) {
            Log.e( "DBManager.onUpgrade", exc.getMessage() );
        }
        finally {
            db.endTransaction();
        }

        this.onCreate( db );
    }
    //Singlenton
    public static DBManager open(Context context){
        if(dbManager == null) {
            dbManager = new DBManager(context);
        }
        return dbManager;
    }
    //Singlenton
    public static DBManager get(){
        return dbManager;
    }
    /** Devuelve todas los ejercicios en la BD
     * @return Un Cursor con los ejercicios. */
    public Cursor getAllEjercicios()
    {
        return this.getReadableDatabase().query( TABLA_EJERCICIO,
                new String[]{EJERCICIO_COL_NOMBRE, EJERCICIO_COL_DESCRIPCION,EJERCICIO_COL_IMAGEN}, null, null, null, null, null );
    }

    public ArrayList<Ejercicio> getArrayEjercicio(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Ejercicio> toret = new ArrayList<>();
        try {
            db.beginTransaction();
            Cursor cursor = db.query(TABLA_EJERCICIO,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);

            if (cursor.moveToFirst()) {
                do {
                    String nombre = cursor.getString(cursor.getColumnIndex(EJERCICIO_COL_NOMBRE));
                    String descripcion = cursor.getString(cursor.getColumnIndex(EJERCICIO_COL_DESCRIPCION));
                    byte[] imagen = cursor.getBlob(cursor.getColumnIndex(EJERCICIO_COL_IMAGEN));
                    toret.add(new Ejercicio(nombre, descripcion, imagen));
                } while (cursor.moveToNext());
            }
        }catch (SQLException exc){
            Log.e("DBManager array",exc.getMessage());
        }
        db.setTransactionSuccessful();

        return toret;
    }

    /** Inserta un nuevo ejercicio.
     * @param nombre El nombre del ejercicio.
     * @param descripcion La descripcion del ejercicio.
     * @return true si se pudo insertar (o modificar), false en otro caso.
     */
    public boolean insertaEjercicio(String nombre, String descripcion,byte[] imagen)
    {
        Cursor cursor = null;
        boolean toret = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put( EJERCICIO_COL_NOMBRE, nombre );
        values.put( EJERCICIO_COL_DESCRIPCION, descripcion );
        values.put( EJERCICIO_COL_IMAGEN,imagen);
        try {
            db.beginTransaction();
            cursor = db.query( TABLA_EJERCICIO,
                    null,
                    EJERCICIO_COL_NOMBRE + "=?",
                    new String[]{ nombre },
                    null, null, null, null );

            if ( cursor.getCount() > 0 ) {
                db.update( TABLA_EJERCICIO,
                        values, EJERCICIO_COL_NOMBRE + "= ?", new String[]{ nombre } );
            } else {
                db.insert( TABLA_EJERCICIO, null, values );
            }

            db.setTransactionSuccessful();
            toret = true;
        } catch(SQLException exc)
        {
            Log.e( "DBManager.inserta", exc.getMessage() );
        }
        finally {
            if ( cursor != null ) {
                cursor.close();
            }

            db.endTransaction();
        }

        return toret;
    }

    /** Elimina un elemento de la base de datos
     * @param nombre El identificador del elemento.
     * @return true si se pudo eliminar, false en otro caso.
     */
    public boolean eliminaEjercicio(String nombre)
    {
        boolean toret = false;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            db.delete( TABLA_EJERCICIO, EJERCICIO_COL_NOMBRE + "=?", new String[]{ nombre } );
            db.setTransactionSuccessful();
            toret = true;
        } catch(SQLException exc) {
            Log.e( "DBManager.elimina", exc.getMessage() );
        } finally {
            db.endTransaction();
        }

        return toret;
    }
}
