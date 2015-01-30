package company.businessinc.bathtouch.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.dataModels.Team;

/**
 * Created by Grzegorz on 27/01/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper{

    SQLiteHelper(Context context) {
        super(context, DBProviderContract.DATABASE_NAME, null, DBProviderContract.DATABASE_VERSION);
        this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statements to create tables
        for(String createTable : DBProviderContract.CREATE_TABLES){
            db.execSQL(createTable);
        }
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

    public void dropTable(String tableName){
        getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    public void createTable(String createTable){
        getWritableDatabase().execSQL(createTable);
    }
}
