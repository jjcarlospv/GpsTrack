package smoothcombtt.xpo.com.gpstrack.TrackingModule.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jose.paucar on 03/12/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper{

    static final int DB_VERSION = 1;
    static final String DB_NAME = "unencrypted-tracking-module-dataBase";

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_SCRIPT_SAVE_POSITION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * Definicion de tipos de variable*
     */
    public static final String STRING_TYPE = "TEXT";
    public static final String INT_TYPE = "INTEGER";
    public static final String BOOLEAN_TYPE = "boolean";
    public static final String DOUBLE_TYPE = "double";
    public static final String PK = "PRIMARY KEY AUTOINCREMENT";
    public static final String FK = "FOREIGN KEY (";
    public static final String REF = " REFERENCES ";


    /**
     * Nombres de tablas
     */

    public static final String TABLE_NAME_SAVE_POSITION = "SavePosition";


    /**
     * Definicion de variables de campo
     */
    public static final String ID = "_id";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ADDRESS = "address";
    public static final String DATE = "date";
    public static final String DISTANCE = "distance";

    public static final String CREATE_SCRIPT_SAVE_POSITION = "CREATE TABLE " + TABLE_NAME_SAVE_POSITION + "(" +

            ID + " " + INT_TYPE + " " + PK + "," +
            LATITUDE + " " + DOUBLE_TYPE + "," +
            LONGITUDE + " " + DOUBLE_TYPE + "," +
            ADDRESS + " " + STRING_TYPE + "," +
            DATE + " " + STRING_TYPE + "," +
            DISTANCE + " " + DOUBLE_TYPE + ")";
}
