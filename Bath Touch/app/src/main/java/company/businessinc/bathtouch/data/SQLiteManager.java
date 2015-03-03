package company.businessinc.bathtouch.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Grzegorz on 11/02/2015.
 */
public class SQLiteManager {

    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static SQLiteManager sInstance;
    private static SQLiteOpenHelper mySQLiteHelper;
    private SQLiteDatabase mDatabase;
    private static final String TAG = "SQLiteManager";

    public static synchronized SQLiteManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SQLiteManager();
        }
        if(mySQLiteHelper == null){
            mySQLiteHelper = SQLiteHelper.getInstance(context);
        }
        return sInstance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = mySQLiteHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();
        }
    }

    /**
     * Database calls
     */

    public synchronized long insert(String tableName, ContentValues contentValues){
        SQLiteDatabase db = openDatabase();
        long val = 0;
        try{
            val = db.insertOrThrow(tableName, null, contentValues);
        } catch (Exception e){
            Log.d(TAG, "Error doing insert, but we won't let this thing go down!" );
        }
        closeDatabase();
        return val;
    }

    public synchronized long bulkInsert(String tableName, ContentValues[] contentValues){
        SQLiteDatabase db = openDatabase();
        db.beginTransaction();
        long val = 0;
        try{
            for(ContentValues cv : contentValues){
                try {
                    val += db.insertOrThrow(tableName, null, cv);
                } catch( Exception e){
                    Log.d(TAG, "Error doing bulk insert, but we won't let this thing go down!" );
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            closeDatabase();
        }
        return val;
    }

    public synchronized int update(String tableName, ContentValues contentValues, String selection, String[] selectionArgs){
        SQLiteDatabase db = openDatabase();
        int val = 0;
        try{
            val = db.update(tableName, contentValues, selection, selectionArgs);
        } catch (Exception e){
            Log.d(TAG, "Error doing update, but we won't let this thing go down!" );
        }
        closeDatabase();
        return val;
    }

    public synchronized Cursor query(Context context, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit){
        Cursor cursor = openDatabase().query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        if(cursor.getCount() < 1){ //the cursor is empty, tell the data store to load the data
            switch (table) {
                case DBProviderContract.ALLTEAMS_TABLE_NAME:
                    DataStore.getInstance(context).loadAllTeams();
                    break;
                case DBProviderContract.ALLLEAGUES_TABLE_NAME:
                    DataStore.getInstance(context).loadAllLeagues();
                    break;
                case DBProviderContract.LIVELEAGUE_TABLE_NAME:
                    DataStore.getInstance(context).loadLiveLeagues();
                    break;
                case DBProviderContract.MYLEAGUES_TABLE_NAME:
                    DataStore.getInstance(context).loadMyLeagues();
                    break;
                case DBProviderContract.LEAGUESSCORE_TABLE_NAME:
                    if(selection != null && selection.contains(DBProviderContract.SELECTION_LEAGUEID) && selectionArgs.length >= 1) {
                        DataStore.getInstance(context).loadLeagueScores(Integer.valueOf(selectionArgs[0]));
                    } else{
                        Log.d(TAG, "Can't call " + "LeagueScore" + " callback, selection/selection args not valid");
                    }
                    break;
                case DBProviderContract.LEAGUETEAMS_TABLE_NAME:
                    if(selection != null && selection.contains(DBProviderContract.SELECTION_LEAGUEID) && selectionArgs.length >= 1) {
                        DataStore.getInstance(context).loadLeaguesTeams(Integer.valueOf(selectionArgs[0]));
                    } else{
                        Log.d(TAG, "Can't call " + "TeamList" + " callback, selection/selection args not valid");
                    }
                    break;
                case DBProviderContract.LEAGUESFIXTURES_TABLE_NAME:
                    if(selection != null && selection.contains(DBProviderContract.SELECTION_LEAGUEID) && selectionArgs.length >= 1) {
                        DataStore.getInstance(context).loadLeagueFixtures(Integer.valueOf(selectionArgs[0]));
                    } else{
                        Log.d(TAG, "Can't call " + "LeagueSchedule" + " callback, selection/selection args not valid");
                    }
                    break;
                case DBProviderContract.LEAGUESSTANDINGS_TABLE_NAME:
                    if(selection != null && selection.contains(DBProviderContract.SELECTION_LEAGUEID) && selectionArgs.length >= 1) {
                        DataStore.getInstance(context).loadLeagueStandings(Integer.valueOf(selectionArgs[0]));
                    } else{
                        Log.d(TAG, "Can't call " + "LeagueView" + " callback, selection/selection args not valid");
                    }
                    break;
                case DBProviderContract.TEAMSFIXTURES_TABLE_NAME:
                    if(selection != null && selection.contains(DBProviderContract.SELECTION_LEAGUEIDANDTEAMID) && selectionArgs.length>= 2) {
                        DataStore.getInstance(context).loadTeamsFixtures(Integer.valueOf(selectionArgs[0]), Integer.valueOf(selectionArgs[1]));
                    } else{
                        Log.d(TAG, "Can't call " + "TeamFixtures" + " callback, selection/selection args not valid");
                    }
                    break;
                case DBProviderContract.TEAMSSCORES_TABLE_NAME:
                    if(selection != null && selection.contains(DBProviderContract.SELECTION_LEAGUEIDANDTEAMID) && selectionArgs.length >= 2) {
                        DataStore.getInstance(context).loadTeamsLeagueScore(Integer.valueOf(selectionArgs[0]), Integer.valueOf(selectionArgs[1]));
                    } else{
                        Log.d(TAG, "Can't call " + "TeamScores" + " callback, selection/selection args not valid");
                    }
                    break;
                case DBProviderContract.MYUPCOMINGGAMES_TABLE_NAME:
                    DataStore.getInstance(context).loadMyUpcomingGames();
                    break;
                case DBProviderContract.MYUPCOMINGREFEREEGAMES_TABLE_NAME:
                    DataStore.getInstance(context).loadMyUpcomingRefGames();
                    break;
                case DBProviderContract.MYUPCOMINGGAMESAVAILABILITY_TABLE_NAME:
                    if(selection != null && selection.contains(DBProviderContract.SELECTION_MATCHID) && selectionArgs.length >= 1) {
                        DataStore.getInstance(context).loadMyAvailability(Integer.valueOf(selectionArgs[0]));
                    } else{
                        Log.d(TAG, "Can't call " + "MyUpcomingGamesAvailability" + " callback, selection/selection args not valid");
                    }
                    break;
                case DBProviderContract.MYTEAMPLAYERSAVAILABILITY_TABLE_NAME:
                    if(selection != null && selection.contains(DBProviderContract.SELECTION_MATCHID) && selectionArgs.length >= 1) {
                        DataStore.getInstance(context).loadMatchPlayersAvailability(Integer.valueOf(selectionArgs[0]));
                    } else{
                        Log.d(TAG, "Can't call " + "MyTeamPlayersAvailability" + " callback, selection/selection args not valid");
                    }
                    break;
            }
        }
        return cursor;
    }
}
