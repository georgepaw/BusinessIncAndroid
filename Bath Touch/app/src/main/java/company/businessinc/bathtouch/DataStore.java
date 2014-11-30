package company.businessinc.bathtouch;

import android.util.Log;

import company.businessinc.dataModels.Match;

/**
 * Created by Louis on 29/11/2014.
 */
public class DataStore {

    private static final String TAG = "DataStore";
    private static Match nextMatch;

    private DataStore() {

    }

    public static DataStore newInstance() {
        return new DataStore();
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
