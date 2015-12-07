package smoothcombtt.xpo.com.gpstrack.TrackingModule.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import smoothcombtt.xpo.com.gpstrack.TrackingModule.database.DataBaseHelper;

/**
 * Created by jose.paucar on 03/12/2015.
 */
public class TrackingProvider extends ContentProvider {


    public static final String CLASS_NAME = "TrackingProvider";
    public static final String PACKAGE_NAME = "com.tzigostore.project";
    public static final Uri URI = Uri.parse("content://" + PACKAGE_NAME + "." + CLASS_NAME);

    public static final Uri URI_SEARCH_PLACE = Uri.parse(URI + "/" + DataBaseHelper.TABLE_NAME_SAVE_POSITION);

    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase sqLiteDatabase;


    private static final int SAVE_POSITION = 1;
    private static final int SAVE_POSITION_ID = 2;

    private static UriMatcher uriMatcher;

    static{

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(URI.getAuthority(), DataBaseHelper.TABLE_NAME_SAVE_POSITION, SAVE_POSITION);
        uriMatcher.addURI(URI.getAuthority(), DataBaseHelper.TABLE_NAME_SAVE_POSITION,SAVE_POSITION_ID);
    }


    @Override
    public boolean onCreate() {

        dataBaseHelper = new DataBaseHelper(getContext());
        sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor cursor = null;

        if (uriMatcher.match(uri) != UriMatcher.NO_MATCH) {

            switch (uriMatcher.match(uri)) {

                case SAVE_POSITION:
                    cursor = sqLiteDatabase.query(DataBaseHelper.TABLE_NAME_SAVE_POSITION, strings, s, strings1, null, null, s1);
                    break;

                case SAVE_POSITION_ID:
                    s = "_id=" + uri.getLastPathSegment();
                    cursor = sqLiteDatabase.query(DataBaseHelper.TABLE_NAME_SAVE_POSITION, strings, s, strings1, null, null, s1);
                    break;
            }
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        Uri uri1 = null;

        if (uriMatcher.match(uri) != UriMatcher.NO_MATCH) {

            switch (uriMatcher.match(uri)) {

                case SAVE_POSITION:
                    long idsp = sqLiteDatabase.insert(DataBaseHelper.TABLE_NAME_SAVE_POSITION, null, contentValues);
                    if (idsp != -1) {
                        uri1 = ContentUris.withAppendedId(URI_SEARCH_PLACE, idsp);
                    } else {
                        uri1 = null;
                    }
                    break;
            }

        }
        return uri1;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int i = 0;

        if (uriMatcher.match(uri) != UriMatcher.NO_MATCH) {

            switch (uriMatcher.match(uri)) {

                case SAVE_POSITION_ID:
                    s = "_id=" + uri.getLastPathSegment();
                    i = sqLiteDatabase.delete(DataBaseHelper.TABLE_NAME_SAVE_POSITION, s, strings);
                    break;

            }
        }
        return i;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int i = 0;

        if (uriMatcher.match(uri) != UriMatcher.NO_MATCH) {

            switch (uriMatcher.match(uri)) {

                case SAVE_POSITION_ID:
                    s = "_id=" + uri.getLastPathSegment();
                    i = sqLiteDatabase.update(DataBaseHelper.TABLE_NAME_SAVE_POSITION, contentValues, s, strings);
                    break;
            }
        }
        return i;

    }
}
