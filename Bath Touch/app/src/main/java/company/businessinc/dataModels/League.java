package company.businessinc.dataModels;

import android.content.ContentValues;
import android.database.Cursor;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gp on 18/11/14.
 */
public class League {
    private Integer leagueID;
    private String leagueName;

    public static final String KEY_LEAGUEID = "leagueID";
    public static final String KEY_LEAGUENAME = "leagueName";
    public static final String[] COLUMNS = {KEY_LEAGUEID,KEY_LEAGUENAME};
    public static final String CREATE_TABLE = KEY_LEAGUEID + "\tINTEGER PRIMARY KEY,\n" +
                                              KEY_LEAGUENAME + "\tTEXT";

    public League(Integer leagueID, String leagueName) {
        this.leagueID = leagueID;
        this.leagueName = leagueName;
    }

    public League(Cursor cursor){
        try {
            this.leagueID = cursor.getInt(cursor.getColumnIndex(KEY_LEAGUEID));
        } catch(Exception e) {
            this.leagueID = null;
        }
        try {
            this.leagueName = cursor.getString(cursor.getColumnIndex(KEY_LEAGUENAME));
        } catch(Exception e) {
            this.leagueName = null;
        }
    }

    public League(JSONObject jsonObject){
        try {
            this.leagueID = jsonObject.getInt(KEY_LEAGUEID);
        } catch (JSONException e){
            this.leagueID = null;
        }
        try {
            this.leagueName = jsonObject.getString(KEY_LEAGUENAME);
        } catch (JSONException e){
            this.leagueName = null;
        }
    }

    public Integer getLeagueID() {
        return leagueID;
    }

    public void setLeagueID(Integer leagueID) {
        this.leagueID = leagueID;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public static List<League> cursorToList(Cursor cursor){
        List<League> output = new ArrayList<>();
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                output.add(new League(cursor));
                cursor.moveToNext();
            }
        }
        return output;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(KEY_LEAGUEID,leagueID);
        values.put(KEY_LEAGUENAME, leagueName);
        return values;
    }
}
