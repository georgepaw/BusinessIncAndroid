package company.businessinc.bathtouch.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Grzegorz on 11/02/2015.
 */
public class SQLiteManager {

    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static SQLiteManager sInstance;
    private static SQLiteOpenHelper mySQLiteHelper;
    private SQLiteDatabase mDatabase;

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
}
