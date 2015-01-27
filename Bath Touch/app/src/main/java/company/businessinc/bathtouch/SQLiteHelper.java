package company.businessinc.bathtouch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import company.businessinc.dataModels.Team;

/**
 * Created by Grzegorz on 27/01/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper{

    /**
     * Create tables strings
     */
    private static final String CREATE_ALLTEAMS_TABLE = "CREATE TABLE " + DBProviderContract.ALLTEAMS_TABLE_NAME + "( " + Team.CREATE_TABLE + " )";

    SQLiteHelper(Context context) {
        super(context, DBProviderContract.DATABASE_NAME, null, DBProviderContract.DATABASE_VERSION);
        this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create table
        db.execSQL(CREATE_ALLTEAMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        for(String table : DBProviderContract.TABLES) {
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }
        // create fresh tables
        this.onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop newer tables if existed
        for(String table : DBProviderContract.TABLES) {
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }
        // create fresh tables
        this.onCreate(db);
    }
}
