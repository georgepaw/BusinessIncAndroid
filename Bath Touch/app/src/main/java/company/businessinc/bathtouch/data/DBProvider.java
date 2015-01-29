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
                DBProviderContract.ALLTEAMS_URL_QUERY);

        sUriMatcher.addURI(
                DBProviderContract.AUTHORITY,
                DBProviderContract.ALLLEAGUES_TABLE_NAME,
                DBProviderContract.ALLLEAGUES_URL_QUERY);

        sUriMatcher.addURI(
                DBProviderContract.AUTHORITY,
                DBProviderContract.MYLEAGUES_TABLE_NAME,
                DBProviderContract.MYLEAGUES_URL_QUERY);

        sUriMatcher.addURI(
                DBProviderContract.AUTHORITY,
                DBProviderContract.LEAGUESSCORE_TABLE_NAME,
                DBProviderContract.LEAGUESSCORE_URL_QUERY);

        sUriMatcher.addURI(
                DBProviderContract.AUTHORITY,
                DBProviderContract.LEAGUESFIXTURES_TABLE_NAME,
                DBProviderContract.LEAGUESFIXTURES_URL_QUERY);

        sUriMatcher.addURI(
                DBProviderContract.AUTHORITY,
                DBProviderContract.LEAGUESSTANDINGS_TABLE_NAME,
                DBProviderContract.LEAGUESSTANDINGS_URL_QUERY);

        sUriMatcher.addURI(
                DBProviderContract.AUTHORITY,
                DBProviderContract.TEAMSFIXTURES_TABLE_NAME,
                DBProviderContract.TEAMSFIXTURES_URL_QUERY);

        sUriMatcher.addURI(
                DBProviderContract.AUTHORITY,
                DBProviderContract.TEAMSSCORES_TABLE_NAME,
                DBProviderContract.TEAMSSCORES_URL_QUERY);

        sUriMatcher.addURI(
                DBProviderContract.AUTHORITY,
                DBProviderContract.TEAMSFIXTURES_TABLE_NAME,
                DBProviderContract.TEAMSFIXTURES_URL_QUERY);

        sUriMatcher.addURI(
                DBProviderContract.AUTHORITY,
                DBProviderContract.MYUPCOMINGGAMES_TABLE_NAME,
                DBProviderContract.MYUPCOMINGGAMES_URL_QUERY);

        sUriMatcher.addURI(
                DBProviderContract.AUTHORITY,
                DBProviderContract.MYUPCOMINGREFEREEGAMES_TABLE_NAME,
                DBProviderContract.MYUPCOMINGREFEREEGAMES_URL_QUERY);

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
        String tableName = getTableName(uri);
        Cursor returnCursor = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        if(returnCursor.getCount() < 1){ //the cursor is empty, tell the data store to load the data
            switch (sUriMatcher.match(uri)) {
                case DBProviderContract.ALLTEAMS_URL_QUERY:
                    DataStore.getInstance(getContext()).loadAllTeams();
                    break;
                case DBProviderContract.ALLLEAGUES_URL_QUERY:
                    DataStore.getInstance(getContext()).loadAllLeagues();
                    break;
                case DBProviderContract.MYLEAGUES_URL_QUERY:
                    DataStore.getInstance(getContext()).loadMyLeagues();
                    break;
                case DBProviderContract.LEAGUESSCORE_URL_QUERY:
                    if(selection.equals(DBProviderContract.SELECTION_LEAGUEID) && selectionArgs.length == 1) {
                        DataStore.getInstance(getContext()).loadLeagueScores(Integer.valueOf(selectionArgs[0]));
                    }
                    break;
                case DBProviderContract.LEAGUESFIXTURES_URL_QUERY:
                    if(selection.equals(DBProviderContract.SELECTION_LEAGUEID) && selectionArgs.length == 1) {
                        DataStore.getInstance(getContext()).loadLeagueFutureFixtures(Integer.valueOf(selectionArgs[0]));
                    }
                    break;
                case DBProviderContract.LEAGUESSTANDINGS_URL_QUERY:
                    if(selection.equals(DBProviderContract.SELECTION_LEAGUEID) && selectionArgs.length == 1) {
                        DataStore.getInstance(getContext()).loadLeagueStandings(Integer.valueOf(selectionArgs[0]));
                    }
                    break;
                case DBProviderContract.TEAMSFIXTURES_URL_QUERY:
                    if(selection.equals(DBProviderContract.SELECTION_LEAGUEIDANDTEAMID) && selectionArgs.length == 2) {
                        DataStore.getInstance(getContext()).loadTeamsFutureFixtures(Integer.valueOf(selectionArgs[0]), Integer.valueOf(selectionArgs[1]));
                    }
                    break;
                case DBProviderContract.TEAMSSCORES_URL_QUERY:
                    if(selection.equals(DBProviderContract.SELECTION_LEAGUEIDANDTEAMID) && selectionArgs.length == 2) {
                        DataStore.getInstance(getContext()).loadTeamsLeagueScore(Integer.valueOf(selectionArgs[0]), Integer.valueOf(selectionArgs[1]));
                    }
                    break;
                case DBProviderContract.MYUPCOMINGGAMES_URL_QUERY:
                    DataStore.getInstance(getContext()).loadMyUpcomingGames();
                    break;
                case DBProviderContract.MYUPCOMINGREFEREEGAMES_URL_QUERY:
                    DataStore.getInstance(getContext()).loadMyUpcomingRefGames();
                    break;
            }
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName = getTableName(uri);
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
        String tableName = getTableName(uri);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.beginTransaction();
        for(ContentValues cv : insertValuesArray) {
            db.insertOrThrow(tableName, null, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();

        getContext().getApplicationContext().getContentResolver().notifyChange(uri, null);
        return insertValuesArray.length;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return -1;
        //throw new UnsupportedOperationException("Delete -- unsupported operation " + uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String tableName = getTableName(uri);
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

    public void dropUserData(){
        //first drop the tables that always exist
        SQLiteHelper db = (SQLiteHelper)mHelper;
        db.dropTable(DBProviderContract.MYLEAGUES_TABLE_NAME);

        //Recreate them
        db.createTable(DBProviderContract.CREATE_MYLEAGUES_TABLE);
    }

    private String getTableName(Uri uri){
        switch (sUriMatcher.match(uri)) {
            case DBProviderContract.ALLTEAMS_URL_QUERY:
                return DBProviderContract.ALLTEAMS_TABLE_NAME;
            case DBProviderContract.ALLLEAGUES_URL_QUERY:
                return DBProviderContract.ALLLEAGUES_TABLE_NAME;
            case DBProviderContract.MYLEAGUES_URL_QUERY:
                return DBProviderContract.MYLEAGUES_TABLE_NAME;
            case DBProviderContract.LEAGUESSCORE_URL_QUERY:
                return DBProviderContract.LEAGUESSCORE_TABLE_NAME;
            case DBProviderContract.LEAGUESFIXTURES_URL_QUERY:
                return DBProviderContract.LEAGUESFIXTURES_TABLE_NAME;
            case DBProviderContract.LEAGUESSTANDINGS_URL_QUERY:
                return DBProviderContract.LEAGUESSTANDINGS_TABLE_NAME;
            case DBProviderContract.TEAMSFIXTURES_URL_QUERY:
                return DBProviderContract.TEAMSFIXTURES_TABLE_NAME;
            case DBProviderContract.TEAMSSCORES_URL_QUERY:
                return DBProviderContract.TEAMSSCORES_TABLE_NAME;
            case DBProviderContract.MYUPCOMINGGAMES_URL_QUERY:
                return DBProviderContract.MYUPCOMINGGAMES_TABLE_NAME;
            case DBProviderContract.MYUPCOMINGREFEREEGAMES_URL_QUERY:
                return DBProviderContract.MYUPCOMINGREFEREEGAMES_TABLE_NAME;
            default:
                Log.d(TAG, "Could not recognize URI");
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }
    }
}
