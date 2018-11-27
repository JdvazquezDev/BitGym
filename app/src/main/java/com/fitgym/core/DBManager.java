package com.fitgym.core;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/** Maneja el acceso a la base de datos. */
public class DBManager extends SQLiteOpenHelper {
    public static final String DB_NOMBRE = "GYM";
    public static final int DB_VERSION = 2;

    public static final String TABLA_EJERCICIO;
    public static final String TABLA_EJERCICIO_RUTINA;

    public static final String EJERCICIO_COL_CLAVE;
    public static final String EJERCICIO_COL_NOMBRE;
    public static final String EJERCICIO_COL_DESCRIPCION;
    public static final String EJERCICIO_COL_IMAGEN;


    public static final String EJERCICIO_RUTINA_COL_EJERCICIO;
    public static final String EJERCICIO_RUTINA_COL_FECHA;
    public static final String EJERCICIO_RUTINA_COL_SERIES;
    public static final String EJERCICIO_RUTINA_COL_REPETICIONES;
    public static final String EJERCICIO_RUTINA_COL_PESO;
    public static final String EJERCICIO_RUTINA_COL_INFO;
    public static final String EJERCICIO_RUTINA_COL_CLAVE;

    private static DBManager dbManager;

    static {
        TABLA_EJERCICIO = "ejercicio";
        EJERCICIO_COL_CLAVE = "_id";
        EJERCICIO_COL_NOMBRE = "nombre";
        EJERCICIO_COL_DESCRIPCION = "descripcion";
        EJERCICIO_COL_IMAGEN = "imagen";

        TABLA_EJERCICIO_RUTINA = "ejercicioRutina";
        EJERCICIO_RUTINA_COL_CLAVE = "_id";
        EJERCICIO_RUTINA_COL_EJERCICIO = "nombre";
        EJERCICIO_RUTINA_COL_FECHA = "fecha";
        EJERCICIO_RUTINA_COL_REPETICIONES = "num_repeticiones";
        EJERCICIO_RUTINA_COL_SERIES = "series";
        EJERCICIO_RUTINA_COL_PESO = "peso";
        EJERCICIO_RUTINA_COL_INFO ="infoExtra";
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
                    + EJERCICIO_COL_CLAVE + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + EJERCICIO_COL_NOMBRE + " string(255) NOT NULL, "
                    + EJERCICIO_COL_DESCRIPCION + " string(255) NOT NULL,"
                    + EJERCICIO_COL_IMAGEN + " blob NOT NULL" +
                    ")");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLA_EJERCICIO_RUTINA + "( "
                    + EJERCICIO_RUTINA_COL_CLAVE + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + EJERCICIO_RUTINA_COL_FECHA + " string(255) NOT NULL, " +
                     EJERCICIO_RUTINA_COL_EJERCICIO + " INTEGER NOT NULL, " +
                    EJERCICIO_RUTINA_COL_REPETICIONES + " int NOT NULL," +
                    EJERCICIO_RUTINA_COL_SERIES + " int NOT NULL, " +
                    EJERCICIO_RUTINA_COL_PESO + " int NOT NULL, " +
                    EJERCICIO_RUTINA_COL_INFO + " string(255) NULL, " +
                    "FOREIGN KEY(" + EJERCICIO_RUTINA_COL_EJERCICIO + ") REFERENCES ejercicio(_id) " +
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
            db.execSQL( "DROP TABLE IF EXISTS " + TABLA_EJERCICIO_RUTINA );
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
    //Singleton
    public static DBManager get(){
        return dbManager;
    }
    /** Devuelve todas los ejercicios en la BD
     * @return Un Cursor con los ejercicios. */
    public Cursor getAllEjercicios()
    {
        return this.getReadableDatabase().query( TABLA_EJERCICIO,
                null, null, null, null, null,null );
    }

    /** Devuelve todos los ejercicios de una rutina en la BD
     * @return Un Cursor con los ejercicios de la rutina.
     */
    public Cursor getAllEjerRutina(String fecha){

        String SELECT_QUERY;

        SELECT_QUERY = "SELECT t1._id as _id, t2.imagen AS imagen, t1.fecha AS fecha , t2.nombre AS nombre ,t1.num_repeticiones  AS num_repeticiones, t1.series AS series, t1.peso AS peso, t1.infoExtra AS infoExtra" +
        " FROM ejercicio t2 INNER JOIN ejercicioRutina t1 " +
        "ON t1.nombre = t2." + EJERCICIO_COL_CLAVE + " WHERE t1.fecha = ?"
        ;
        Cursor c = this.getReadableDatabase().rawQuery(SELECT_QUERY, new String[]{fecha});

        return c;
    }

    public Cursor getAllEjerciciosNotInRutina(String fecha){

        String SELECT_QUERY = "SELECT *" +
                " FROM ejercicio t2 " +
                " WHERE  t2. " + EJERCICIO_COL_CLAVE + " NOT IN( SELECT t1.nombre FROM ejercicioRutina t1 WHERE t1.fecha = ?" + ")"
                ;
        Cursor c = this.getReadableDatabase().rawQuery(SELECT_QUERY, new String[]{fecha});
        return c;
    }

    public Cursor getAllDatesRutina(){

        String SELECT_QUERY = "SELECT DISTINCT fecha" +
                " FROM ejercicioRutina ";
        Cursor c = this.getReadableDatabase().rawQuery(SELECT_QUERY, null);
        return c;
    }



    /** Inserta un nuevo ejercicio.
     * @param nombre El nombre del ejercicio.
     * @param descripcion La descripcion del ejercicio.
     * @return true si se pudo insertar (o modificar), false en otro caso.
     */
    public boolean insertaEjercicio(String nombre, String descripcion, byte[] imagen)
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
/*
            if ( cursor.getCount() > 0 ) {
                db.update( TABLA_EJERCICIO,
                        values, EJERCICIO_COL_NOMBRE + "= ?", new String[]{ nombre } );
            } else {*/
                db.insert( TABLA_EJERCICIO, null, values );
     //       }

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

    /** Inserta un nuevo ejercicio.
     * @param id Id
     * @param nombre El nombre del ejercicio.
     * @param descripcion La descripcion del ejercicio.
     * @return true si se pudo insertar (o modificar), false en otro caso.
     */
    public boolean editEjercicio(int id,String nombre, String descripcion, byte[] imagen)
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
                    EJERCICIO_COL_CLAVE + "=?",
                    new String[]{String.valueOf(id)},
                    null, null, null, null );

            if ( cursor.getCount() > 0 ) {
                db.update( TABLA_EJERCICIO,
                        values, EJERCICIO_COL_CLAVE + "= ?", new String[]{String.valueOf(id)} );
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
    /** Inserta un nuevo ejercicio en la rutina.
     * @param nombre El nombre del ejercicio.
     * @param fecha La fecha de la rutina.s
     * @param numRepes El numero de repeticiones del ejercicio de la rutina
     * @return true si se pudo insertar (o modificar), false en otro caso.
     */
    public boolean insertaEjercicioRutina(int nombre, String fecha, int numRepes,int  series, int peso, String infoExtra)
    {
     //   Log.i("bd","nombre" + nombre);
     //   Log.i("bd","fecha" + fecha);
      //  Log.i("bd","numRepes " + String.valueOf(numRepes));

        Cursor cursor = null;
        boolean toret = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( EJERCICIO_RUTINA_COL_EJERCICIO,nombre);
        values.put( EJERCICIO_RUTINA_COL_FECHA, fecha);
        values.put(EJERCICIO_RUTINA_COL_REPETICIONES,numRepes);
        values.put( EJERCICIO_RUTINA_COL_SERIES,series);
        values.put( EJERCICIO_RUTINA_COL_PESO, peso);
        values.put(EJERCICIO_RUTINA_COL_INFO,infoExtra);


        try {
            db.beginTransaction();
            cursor = db.query( TABLA_EJERCICIO_RUTINA,
                    null,
                    EJERCICIO_RUTINA_COL_EJERCICIO + "= ? AND " + EJERCICIO_RUTINA_COL_FECHA + "= ?"
                    , new String[]{String.valueOf(nombre),fecha} ,
                    null, null, null, null );
            Log.i("bd", "try" + String.valueOf(cursor.getCount()));

            if ( cursor.getCount() > 0 ) {
                Log.i("bd","mayor que 0");
                db.update( TABLA_EJERCICIO_RUTINA,
                        values, EJERCICIO_RUTINA_COL_EJERCICIO + "= ? AND " + EJERCICIO_RUTINA_COL_FECHA + "= ?", new String[]{String.valueOf(nombre),fecha} );
            } else {
                db.insert( TABLA_EJERCICIO_RUTINA, null, values );
                Log.i("bd","igual que 0");
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

    public boolean editarEjercicioRutina(int id, String fecha, int numRepes,int  series, int peso, String infoExtra)
    {

        Cursor cursor = null;
        boolean toret = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put( EJERCICIO_RUTINA_COL_FECHA, fecha);
        values.put(EJERCICIO_RUTINA_COL_REPETICIONES,numRepes);
        values.put( EJERCICIO_RUTINA_COL_SERIES,series);
        values.put( EJERCICIO_RUTINA_COL_PESO, peso);
        values.put(EJERCICIO_RUTINA_COL_INFO,infoExtra);

        try {
            db.beginTransaction();

                db.update( TABLA_EJERCICIO_RUTINA,
                        values, EJERCICIO_RUTINA_COL_CLAVE + "= ? ", new String[]{String.valueOf(id)} );

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
     * @param id El identificador del elemento.
     * @return true si se pudo eliminar, false en otro caso.
     */
    public boolean eliminaEjercicio(int id)
    {
        boolean toret = false;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
                db.delete( TABLA_EJERCICIO, EJERCICIO_COL_CLAVE + "=?", new String[]{String.valueOf(id)} );
            db.setTransactionSuccessful();
            toret = true;
        } catch(SQLException exc) {
            Log.e( "DBManager.elimina", exc.getMessage() );
        } finally {
            Log.i( "DBManager.elimina", "bien");
            db.endTransaction();
        }

        return toret;
    }
    public boolean eliminaEjercicioRutina(int id,String fecha)
    {
        boolean toret = false;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();
            db.delete( TABLA_EJERCICIO_RUTINA, EJERCICIO_RUTINA_COL_CLAVE + "= ? AND " + EJERCICIO_RUTINA_COL_FECHA + "= ?", new String[]{String.valueOf(id),fecha} );
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
