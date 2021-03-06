package com.BitGym.core;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


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
    public static final String EJERCICIO_COL_URL;


    public static final String EJERCICIO_RUTINA_COL_EJERCICIO;
    public static final String EJERCICIO_RUTINA_COL_FECHA;
    public static final String EJERCICIO_RUTINA_COL_SERIES;
    public static final String EJERCICIO_RUTINA_COL_REPETICIONES;
    public static final String EJERCICIO_RUTINA_COL_PESO;
    public static final String EJERCICIO_RUTINA_COL_INFO;
    public static final String EJERCICIO_RUTINA_COL_CLAVE;
    public static final String EJERCICIO_RUTINA_COL_TIEMPO;

    private static DBManager dbManager;

    static {
        TABLA_EJERCICIO = "ejercicio";
        EJERCICIO_COL_CLAVE = "_id";
        EJERCICIO_COL_NOMBRE = "nombre";
        EJERCICIO_COL_DESCRIPCION = "descripcion";
        EJERCICIO_COL_IMAGEN = "imagen";
        EJERCICIO_COL_URL = "url";

        TABLA_EJERCICIO_RUTINA = "ejercicioRutina";
        EJERCICIO_RUTINA_COL_CLAVE = "_id";
        EJERCICIO_RUTINA_COL_EJERCICIO = "nombre";
        EJERCICIO_RUTINA_COL_FECHA = "fecha";
        EJERCICIO_RUTINA_COL_REPETICIONES = "repeticiones";
        EJERCICIO_RUTINA_COL_SERIES = "series";
        EJERCICIO_RUTINA_COL_PESO = "peso";
        EJERCICIO_RUTINA_COL_INFO ="infoExtra";
        EJERCICIO_RUTINA_COL_TIEMPO = "tiempo";
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
                    + EJERCICIO_COL_DESCRIPCION + " string(255) NOT NULL, "
                    + EJERCICIO_COL_IMAGEN + " string(255) NOT  NULL, "
                    + EJERCICIO_COL_URL + " string(255) NULL" +
                    ")" );

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLA_EJERCICIO_RUTINA + "( "
                    + EJERCICIO_RUTINA_COL_CLAVE + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + EJERCICIO_RUTINA_COL_FECHA + " string(255) NOT NULL, " +
                    EJERCICIO_RUTINA_COL_EJERCICIO + " INTEGER NOT NULL, " +
                    EJERCICIO_RUTINA_COL_REPETICIONES + " int NOT NULL," +
                    EJERCICIO_RUTINA_COL_SERIES + " int NOT NULL, " +
                    EJERCICIO_RUTINA_COL_PESO + " int NOT NULL, " +
                    EJERCICIO_RUTINA_COL_INFO + " string(255) NOT NULL, " +
                    EJERCICIO_RUTINA_COL_TIEMPO + " string(255) NOT NULL, " +
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

        SELECT_QUERY = "SELECT t1._id as _id, t2.imagen AS imagen, t1.fecha AS fecha , t2.nombre AS nombre ,t1.repeticiones  AS repeticiones, t1.series AS series, t1.peso AS peso, t1.infoExtra AS infoExtra,t1.tiempo AS tiempo" +
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
    public boolean insertaEjercicio(String nombre, String descripcion, String imagen, String url)
    {
        Cursor cursor = null;
        boolean toret = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put( EJERCICIO_COL_NOMBRE, nombre );
        values.put( EJERCICIO_COL_DESCRIPCION, descripcion );
        values.put( EJERCICIO_COL_IMAGEN,imagen);
        values.put( EJERCICIO_COL_URL, url);

        try {
            db.beginTransaction();
            cursor = db.query( TABLA_EJERCICIO,
                    null,
                    EJERCICIO_COL_NOMBRE + "=?",
                    new String[]{ nombre },
                    null, null, null, null );
                db.insert( TABLA_EJERCICIO, null, values );

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
    public boolean editEjercicio(int id,String nombre, String descripcion, String imagen, String url)
    {
        Cursor cursor = null;
        boolean toret = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put( EJERCICIO_COL_NOMBRE, nombre );
        values.put( EJERCICIO_COL_DESCRIPCION, descripcion );
        values.put( EJERCICIO_COL_IMAGEN,imagen);
        values.put(EJERCICIO_COL_URL, url);
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
     * @param repeticiones El numero de repeticiones del ejercicio de la rutina
     * @return true si se pudo insertar (o modificar), false en otro caso.
     */
    public boolean insertaEjercicioRutina(int nombre, String fecha, int repeticiones,int  series, int peso, String infoExtra,String tiempo)
    {
        Cursor cursor = null;
        boolean toret = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( EJERCICIO_RUTINA_COL_EJERCICIO, nombre);
        values.put( EJERCICIO_RUTINA_COL_FECHA, fecha);
        values.put(EJERCICIO_RUTINA_COL_REPETICIONES, repeticiones);
        values.put( EJERCICIO_RUTINA_COL_SERIES,series);
        values.put( EJERCICIO_RUTINA_COL_PESO, peso);
        values.put(EJERCICIO_RUTINA_COL_INFO, infoExtra);
        values.put(EJERCICIO_RUTINA_COL_TIEMPO, tiempo);
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

    public boolean editarEjercicioRutina(int id, String fecha, int repeticiones, int  series, int peso, String infoExtra,String tiempo)
    {
        Cursor cursor = null;
        boolean toret = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put( EJERCICIO_RUTINA_COL_FECHA, fecha);
        values.put( EJERCICIO_RUTINA_COL_REPETICIONES, repeticiones);
        values.put( EJERCICIO_RUTINA_COL_SERIES,series);
        values.put( EJERCICIO_RUTINA_COL_PESO, peso);
        values.put( EJERCICIO_RUTINA_COL_INFO, infoExtra);
        values.put( EJERCICIO_RUTINA_COL_TIEMPO, tiempo);
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
            db.delete(TABLA_EJERCICIO, EJERCICIO_COL_CLAVE + "=?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } catch(SQLException exc) {
            Log.e( "DBManager.elimina", exc.getMessage() );
        } finally {
            Log.i( "DBManager.elimina", "bien");
            db.endTransaction();
        }

        return toret;
    }
    public boolean estaEjercicioRutina( int id){
        String SELECT_QUERY;
        SELECT_QUERY = "SELECT *" + "FROM ejercicioRutina WHERE nombre=?";
        Cursor c = this.getReadableDatabase().rawQuery(SELECT_QUERY,new String[]{String.valueOf(id)});
        if(c.getCount() == 0){
            return false;
        }else {
            return true;
        }
    }
/*

    public boolean estaEjercicioRutina(int id) {
        boolean toret = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            db.beginTransaction();
            cursor = db.query(true, TABLA_EJERCICIO, null,
                    "( " + EJERCICIO_COL_CLAVE + " NOT IN( SELECT t1.nombre FROM ejercicioRutina t1 ))", null,
                    null, null, null, null);
            db.setTransactionSuccessful();
            toret = true;
        } catch(SQLException exc) {
            Log.e( "estaEjercicio", exc.getMessage() );
        } finally {
            db.endTransaction();
        }

        if ( cursor.getColumnIndex("id") == id){
            toret = true;
        }
        else {
            toret = false;
        }
        return toret;
    }
*/
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


    public Cursor searchExercise(String nombre){

        Cursor mCursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        if (nombre == null  ||  nombre.length () == 0)  {
            mCursor = db.query(TABLA_EJERCICIO, null,
                    null, null, null, null, null);

        }
        else {
            mCursor = db.query(true, TABLA_EJERCICIO, null,
                    EJERCICIO_COL_NOMBRE + " like '%" + nombre + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor searchExerciseNotInRutina(String nombre,String fecha){

        Cursor mCursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        if (nombre == null  ||  nombre.length () == 0)  {
            mCursor = db.query(true, TABLA_EJERCICIO, null,
                    "( " + EJERCICIO_COL_CLAVE + " NOT IN( SELECT t1.nombre FROM ejercicioRutina t1 WHERE t1.fecha = " + fecha  + "))", null,
                    null, null, null, null);
        }
        else {
            mCursor = db.query(true, TABLA_EJERCICIO, null,
                    "( " + EJERCICIO_COL_CLAVE + " NOT IN( SELECT t1.nombre FROM ejercicioRutina t1 WHERE t1.fecha = " + fecha  + ")) AND nombre like '%" + nombre + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor searchRutina(String nombre,String fecha){

        Cursor mCursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        if (nombre == null  ||  nombre.length () == 0)  {
            String SELECT_QUERY = "SELECT t1._id as _id, t2.imagen AS imagen, t1.fecha AS fecha , t2.nombre AS nombre ,t1.repeticiones  AS repeticiones, t1.series AS series, t1.peso AS peso, t1.infoExtra AS infoExtra" +
                    " FROM ejercicio t2 INNER JOIN ejercicioRutina t1 " +
                    "ON t1.nombre = t2." + EJERCICIO_COL_CLAVE + " WHERE t1.fecha = ?"
            ;
            mCursor = db.rawQuery(SELECT_QUERY, new String[]{fecha});
        }
        else {
            String SELECT_QUERY = "SELECT t1._id as _id, t2.imagen AS imagen, t1.fecha AS fecha , t2.nombre AS nombre ,t1.repeticiones  AS repeticiones, t1.series AS series, t1.peso AS peso, t1.infoExtra AS infoExtra" +
                    " FROM ejercicio t2 INNER JOIN ejercicioRutina t1 " +
                    "ON t1.nombre = t2." + EJERCICIO_COL_CLAVE + " WHERE t1.fecha = ? AND t2.nombre like '%" + nombre + "%'"
                    ;
            mCursor = db.rawQuery(SELECT_QUERY, new String[]{fecha});
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }
}
