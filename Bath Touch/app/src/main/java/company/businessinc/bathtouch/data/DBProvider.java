package company.businessinc.bathtouch.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Grzegorz on 27/01/2015.
 */
public class DBProvider extends ContentProvider {

    private static final String TAG = "DBProvider";

    // Defines an helper object for the backing database
    private SQLiteOpenHelper mHelper;

    // Defines a helper object that matches content URIs to table-specific parameters
    private static final UriMatcher sUriMatcher;

    static {

        // Creates an object that associates content URIs with numeric codes
        sUriMatcher = new UriMatcher(0);

        // Adds a URI "match" entry that maps content URIs to a numeric code
        sUriMatcher.addURI(
                DBProviderContract.AUTHORITY,
                DBProviderContract.ALLTEAMS_TABLE_NAME,
                DBProviderContract.GETALLTEAMS_URL_QUERY);

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
        String tableName = "";
        switch (sUriMatcher.match(uri)) {
            case DBProviderContract.GETALLTEAMS_URL_QUERY:
                tableName = DBProviderContract.ALLTEAMS_TABLE_NAME;
            default:
                Log.d(TAG, "Failed query");
                //throw new IllegalArgumentException("Invalid URI:" + uri);
        }
        Cursor returnCursor = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName = "";
        switch (sUriMatcher.match(uri)) {
            // For the modification
            case DBProviderContract.GETALLTEAMS_URL_QUERY:
                tableName = DBProviderContract.ALLTEAMS_TABLE_NAME;
                break;
                default:
                Log.d(TAG, "Failed insert");
                return null;
                //throw new IllegalArgumentException("Invalid URI:" + uri);

        }
        // Get the database
        SQLiteDatabase localSQLiteDatabase = mHelper.getWritableDatabase();
        long id = localSQLiteDatabase.insert(tableName, null, values);
        //check if it was correct
        if (-1 != id) {
            getContext().getContentResolver().notifyChange(uri, null);
            return Uri.withAppendedPath(uri, Long.toString(id));
        } else {
            throw new SQLiteException("Insert error:" + uri);
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] insertValuesArray) {
        switch (sUriMatcher.match(uri)) {
            case DBProviderContract.GETALLTEAMS_URL_QUERY:
                return super.bulkInsert(uri, insertValuesArray);
            default:
                //throw new IllegalArgumentException("Invalid URI:" + uri);
                Log.d(TAG, "Failed bulk insert");

        }
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return -1;
        //throw new UnsupportedOperationException("Delete -- unsupported operation " + uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String tableName = "";
        switch (sUriMatcher.match(uri)) {
            case DBProviderContract.GETALLTEAMS_URL_QUERY:
                tableName = DBProviderContract.ALLTEAMS_TABLE_NAME;
            default:
                Log.d(TAG, "Failed updates");
                //throw new IllegalArgumentException("Update: Invalid URI: " + uri);
        }
        SQLiteDatabase localSQLiteDatabase = mHelper.getWritableDatabase();
        int rows = localSQLiteDatabase.update(tableName, values, selection, selectionArgs);
        if (0 != rows) {
            getContext().getContentResolver().notifyChange(uri, null);
            return rows;
        } else {
            throw new SQLiteException("Update error:" + uri);
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
