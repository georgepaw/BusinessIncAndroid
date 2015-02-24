package company.businessinc.dataModels;

import android.content.ContentValues;
import android.database.Cursor;
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
    private Integer captainID = null;
    private String captainName = null;
    private Boolean isCaptain = false;
    private Boolean isReferee = false;
    private String teamColorPrimary;
    private String teamColorSecondary;

    public User() {
        isLoggedIn = false;
        name = null;
        teamName = null;
        teamID = null;
        captainID = null;
        captainName = null;
        isCaptain = false;
        isReferee = false;
        teamColorPrimary = null;
        teamColorSecondary = null;
    }

    public static final String KEY_NAME = "name";
    public static final String KEY_TEAMNAME = "teamName";
    public static final String KEY_TEAMID = "teamID";
    public static final String KEY_CAPTAINID = "captainID";
    public static final String KEY_CAPTAINNAME = "captainName";
    public static final String KEY_ISCAPTAIN = "isCaptain";
    public static final String KEY_ISREFEREE = "isReferee";
    public static final String KEY_TEAMCOLORPRIMARY = "teamColorPrimary";
    public static final String KEY_TEAMCOLORSECONDARY = "teamColorSecondary";
    public static final String[] COLUMNS = {KEY_NAME, KEY_TEAMNAME, KEY_TEAMID, KEY_TEAMCOLORPRIMARY, KEY_TEAMCOLORSECONDARY};
    public static final String CREATE_TABLE =   KEY_NAME + "\tTEXT,\n" +
            KEY_TEAMNAME + "\tTEXT,\n" +
            KEY_TEAMID + "\tINTEGER,\n" +
            KEY_CAPTAINID + "\tINTEGER,\n" +
            KEY_CAPTAINNAME + "\tTEXT,\n" +
            KEY_ISCAPTAIN + "\tINTEGER,\n" +
            KEY_ISREFEREE + "\tINTEGER,\n" +
            KEY_TEAMCOLORPRIMARY + "\tTEXT,\n" +
            KEY_TEAMCOLORSECONDARY + "\tTEXT,\n";

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
            this.captainID = cursor.getInt(cursor.getColumnIndex(KEY_CAPTAINID));
        } catch(Exception e) {
            this.captainID = null;
        }
        try {
            this.captainName = cursor.getString(cursor.getColumnIndex(KEY_CAPTAINNAME));
        } catch(Exception e) {
            this.captainName = null;
        }
        try {
            this.isCaptain = (cursor.getInt(cursor.getColumnIndex(KEY_ISCAPTAIN)) == 1);
        } catch(Exception e) {
            this.isCaptain = false;
        }
        try {
            this.isReferee = (cursor.getInt(cursor.getColumnIndex(KEY_ISREFEREE)) == 1);
        } catch(Exception e) {
            this.isReferee = false;
        }
        try {
            this.name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
        } catch(Exception e) {
            this.name = null;
        }
        try {
            this.teamColorPrimary = cursor.getString(cursor.getColumnIndex(KEY_TEAMCOLORPRIMARY));
        } catch(Exception e) {
            this.teamColorPrimary = null;
        }
        try {
            this.teamColorSecondary = cursor.getString(cursor.getColumnIndex(KEY_TEAMCOLORSECONDARY));
        } catch(Exception e) {
            this.teamColorSecondary = null;
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
        try {
            this.captainID = jsonObject.getInt(KEY_CAPTAINID);
        } catch (JSONException e){
            this.captainID = null;
        }
        try {
            this.captainName = jsonObject.getString(KEY_CAPTAINNAME);
        } catch (JSONException e){
            this.captainName = null;
        }
        try {
            this.isCaptain = jsonObject.getBoolean(KEY_ISCAPTAIN);
        } catch (JSONException e){
            this.isCaptain = false;
        }
        try {
            this.isReferee = jsonObject.getBoolean(KEY_ISREFEREE);
        } catch (JSONException e){
            this.isReferee = false;
        }
        try {
            this.teamColorPrimary = jsonObject.getString(KEY_TEAMCOLORPRIMARY);
        } catch (JSONException e){
            this.teamColorPrimary = null;
        }
        try {
            this.teamColorSecondary = jsonObject.getString(KEY_TEAMCOLORSECONDARY);
        } catch (JSONException e){
            this.teamColorSecondary = null;
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

    public Integer getCaptainID() {
        return captainID;
    }

    public String getCaptainName() {
        return captainName;
    }

    public Boolean isCaptain() {
        return isCaptain;
    }

    public Boolean isReferee() {
        return isReferee;
    }

    public String getTeamColorPrimary() { //TODO Request change of color storage from web team
        return "#" + teamColorPrimary;
    }

    public String getTeamColorSecondary() {
        return '#' + teamColorSecondary;
    }

    public String toString(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("status", isLoggedIn);
            jsonObject.put(KEY_NAME, name);
            if (isLoggedIn){
                jsonObject.put(KEY_TEAMNAME, teamName);
                jsonObject.put(KEY_TEAMID, teamID);
                jsonObject.put(KEY_ISCAPTAIN, isCaptain);
                jsonObject.put(KEY_ISREFEREE, isReferee);
                jsonObject.put(KEY_TEAMCOLORPRIMARY, teamColorPrimary);
                jsonObject.put(KEY_TEAMCOLORSECONDARY, teamColorSecondary);
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
        values.put(KEY_ISCAPTAIN, isCaptain ? 1 : 0);
        values.put(KEY_ISREFEREE, isReferee ? 1 : 0);
        values.put(KEY_TEAMCOLORPRIMARY,teamColorPrimary);
        values.put(KEY_TEAMCOLORSECONDARY,teamColorSecondary);
        return values;
    }
}
