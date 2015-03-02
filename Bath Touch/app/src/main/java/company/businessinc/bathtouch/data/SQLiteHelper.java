package company.businessinc.bathtouch.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Grzegorz on 27/01/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper{

    private static SQLiteHelper sInstance;

    public static SQLiteHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SQLiteHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private SQLiteHelper(Context context) {
        super(context, DBProviderContract.DATABASE_NAME, null, DBProviderContract.DATABASE_VERSION);
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
