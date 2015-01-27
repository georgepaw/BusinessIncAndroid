package company.businessinc.bathtouch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import company.businessinc.dataModels.League;

/**
 * Created by Grzegorz on 27/01/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "BathTouchDB";
    private static final String TABLE_LEAGUELISTS = "LeagueLists";
    private static final String CREATE_LEAGUELISTS_TABLE = "CREATE TABLE " + TABLE_LEAGUELISTS + ( " + League.tableString() + " )";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table

        // create books table
        db.execSQL(CREATE_LEAGUELISTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS LeagueLists");

        // create fresh books table
        this.onCreate(db);
    }

}