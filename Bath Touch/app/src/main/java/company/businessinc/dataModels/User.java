package company.businessinc.dataModels;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gp on 18/11/14.
 */
public class User {
    private Boolean isLoggedIn = false;
    private String name = null;
    private String teamName = null;
    private Integer teamID = null;

    public User() {
        isLoggedIn = false;
        name = null;
        teamName = null;
        teamID = null;
    }

    public static final String KEY_NAME = "name";
    public static final String KEY_TEAMNAME = "teamName";
    public static final String KEY_TEAMID = "teamID";
    public static final String[] COLUMNS = {KEY_NAME, KEY_TEAMNAME, KEY_TEAMID};
    public static final String CREATE_TABLE =   KEY_NAME + "\tTEXT,\n" +
            KEY_TEAMNAME + "\tTEXT,\n" +
            KEY_TEAMID + "\tINTEGER,\n";

    public User(Cursor cursor){
        try {
            this.teamID = cursor.getInt(cursor.getColumnIndex(KEY_TEAMID));
        } catch(Exception e) {
            this.teamID = null;
        }
        try {
            this.teamName = cursor.getString(cursor.getColumnIndex(KEY_TEAMNAME));
        } catch(Exception e) {
            this.teamName = null;
        }
        try {
            this.name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
        } catch(Exception e) {
            this.name = null;
        }
    }

    public User(JSONObject jsonObject) throws JSONException{
        try {
            this.isLoggedIn = jsonObject.getBoolean("status");
        } catch (JSONException e){
            this.isLoggedIn = null;
        }
        try {
            this.name = jsonObject.getString(KEY_NAME);
        } catch(JSONException e){
            this.name = null;
        }
        try {
            this.teamName = jsonObject.getString(KEY_TEAMNAME);
        } catch (JSONException e){
            this.teamName = null;
        }
        try {
            this.teamID = jsonObject.getInt(KEY_TEAMID);
        } catch (JSONException e){
            this.teamID = null;
        }
    }

    public Boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name){
        name = Name;
    }

    public String getTeamName() {
        return teamName;
    }

    public Integer getTeamID() {
        return teamID;
    }

    public String toString(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("status", isLoggedIn);
            jsonObject.put(KEY_NAME, name);
            if (isLoggedIn){
                jsonObject.put(KEY_TEAMNAME, teamName);
                jsonObject.put(KEY_TEAMID, teamID);
            }

        } catch (JSONException e){

        }
        return  jsonObject.toString();
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_TEAMNAME, teamName);
        values.put(KEY_TEAMID,teamID);
        return values;
    }
}
