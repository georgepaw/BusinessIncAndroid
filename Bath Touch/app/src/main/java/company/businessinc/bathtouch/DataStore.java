package company.businessinc.bathtouch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import company.businessinc.dataModels.League;
import company.businessinc.dataModels.Match;

/**
 * Created by Louis on 29/11/2014.
 */
public class DataStore extends SQLiteOpenHelper {

    private static DataStore sInstance;

    private static final String TAG = "DataStore";
    private static Match nextMatch;

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "BathTouchDB";
    private static final String TABLE_LEAGUELISTS = "LeagueLists";
    private static final String CREATE_LEAGUELISTS_TABLE = "CREATE TABLE " + TABLE_LEAGUELISTS + "( " + League.CREATE_TABLE + " )";
    private static final String[] TABLES = {TABLE_LEAGUELISTS};

    public static DataStore getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new DataStore(context.getApplicationContext());
        }
        return sInstance;
    }

    private DataStore(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create table
        db.execSQL(CREATE_LEAGUELISTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        for(String table : TABLES) {
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }

        // create fresh books tables
        this.onCreate(db);
    }

    public static void newInstance(Context c) {
        sInstance = new DataStore(c.getApplicationContext());
    }

    public static void clearData() {
        nextMatch = null;
    }

    public static void setNextMatch(Match match) {
        nextMatch = match;
    }

    public static Match getNextMatch() {
        return nextMatch;
    }

}
