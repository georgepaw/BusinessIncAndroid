package company.businessinc.bathtouch;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import company.businessinc.dataModels.Team;

/**
 * Created by Grzegorz on 27/01/2015.
 */
public class DBProvider extends ContentProvider {
    /**
     * Integers which indicate which query to run
     */
    public static final int GETALLTEAMS_URL_QUERY = 0;

    // Defines an helper object for the backing database
    private SQLiteOpenHelper mHelper;

    // Defines a helper object that matches content URIs to table-specific parameters
    private static final UriMatcher sUriMatcher;

    static {

        // Creates an object that associates content URIs with numeric codes
        sUriMatcher = new UriMatcher(0);

        // Adds a URI "match" entry that maps picture URL content URIs to a numeric code
        sUriMatcher.addURI(
                DBProviderContract.AUTHORITY,
                DBProviderContract.ALLTEAMS_TABLE_NAME,
                GETALLTEAMS_URL_QUERY);

    }

    // Closes the SQLite database helper class, to avoid memory leaks
    public void close() {
        mHelper.close();
    }

    @Override
    public boolean onCreate() {
        mHelper = new SQLiteHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)) {
            case GETALLTEAMS_URL_QUERY:
                // Does the query against a read-only version of the database
                Cursor returnCursor = db.query(
                        DBProviderContract.ALLTEAMS_TABLE_NAME,
                        projection,
                        null, null, null, null, null);

                // Sets the ContentResolver to watch this content URI for data changes
                returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
                return returnCursor;
            default:
                throw new IllegalArgumentException("Invalid URI:" + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            // For the modification
            case GETALLTEAMS_URL_QUERY:
                // Creates a writeable database or gets one from cache
                SQLiteDatabase localSQLiteDatabase = mHelper.getWritableDatabase();
                long id = localSQLiteDatabase.insert(DBProviderContract.ALLTEAMS_TABLE_NAME, null, values);

                // If the insert succeeded, notify a change and return the new row's content URI.
                if (-1 != id) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return Uri.withAppendedPath(uri, Long.toString(id));
                } else {
                    throw new SQLiteException("Insert error:" + uri);
                }

            default:
                throw new IllegalArgumentException("Invalid URI:" + uri);

        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] insertValuesArray) {

        // Decodes the content URI and choose which insert to use
        switch (sUriMatcher.match(uri)) {
            case GETALLTEAMS_URL_QUERY:
                // Do inserts by calling SQLiteDatabase.insert on each row in insertValuesArray
                int i = 0;
                for(ContentValues a : insertValuesArray){
                    insert(uri,a);
                    i++;
                }
                return i;

            default:
                throw new IllegalArgumentException("Invalid URI:" + uri);

        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return -1;
        //throw new UnsupportedOperationException("Delete -- unsupported operation " + uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case GETALLTEAMS_URL_QUERY:
                SQLiteDatabase localSQLiteDatabase = mHelper.getWritableDatabase();
                // Updates the table
                int rows = localSQLiteDatabase.update(
                        DBProviderContract.ALLTEAMS_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);

                // If the update succeeded, notify a change and return the number of updated rows.
                if (0 != rows) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return rows;
                } else {
                    throw new SQLiteException("Update error:" + uri);
                }

            default:
                throw new IllegalArgumentException("Update: Invalid URI: " + uri);
        }
    }

    public boolean isTableEmpty(String tableName) {
        Cursor cursor = mHelper.getReadableDatabase().rawQuery("SELECT count(*) FROM  " + tableName , null);
        if(cursor!=null) {
            cursor.moveToFirst();
            if(cursor.getInt(0)>0) {
                cursor.close();
                return false;
            }
            cursor.close();
        }
        return true;
    }
}
