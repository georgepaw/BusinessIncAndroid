package company.businessinc.dataModels;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gp on 18/11/14.
 */
public class Team {
    private Integer teamID;
    private String teamName;
    private Integer captainID;
    private String captainName;
    private String teamColorPrimary;
    private String teamColorSecondary;

    public static final String KEY_TEAMID = "teamID";
    public static final String KEY_TEAMNAME = "teamName";
    public static final String KEY_CAPTAINID = "captainID";
    public static final String KEY_CAPTAINNAME = "captainName";
    public static final String KEY_TEAMCOLORPRIMARY = "teamColorPrimary";
    public static final String KEY_TEAMCOLORSECONDARY = "teamColorSecondary";
    public static final String[] COLUMNS = {KEY_TEAMID, KEY_TEAMNAME, KEY_CAPTAINID, KEY_CAPTAINNAME, KEY_TEAMCOLORPRIMARY, KEY_TEAMCOLORSECONDARY};
    public static final String CREATE_TABLE =   KEY_TEAMID + "\tINTEGER,\n" +
                                                KEY_TEAMNAME + "\tTEXT,\n" +
                                                KEY_CAPTAINID + "\tINTEGER,\n" +
                                                KEY_CAPTAINNAME + "\tTEXT,\n" +
                                                KEY_TEAMCOLORPRIMARY + "\tTEXT,\n" +
                                                KEY_TEAMCOLORSECONDARY + "\tTEXT";


    public Team(Integer teamID, String teamName, Integer captainID, String captainName, String teamColorPrimary, String teamColorSecondary) {
        this.teamID = teamID;
        this.teamName = teamName;
        this.captainID = captainID;
        this.captainName = captainName;
        this.teamColorPrimary = teamColorPrimary;
        this.teamColorSecondary = teamColorSecondary;
    }

    public Team(Cursor cursor){
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

    public Team (JSONObject jsonObject) throws JSONException{
        try {
            this.teamID = jsonObject.getInt(KEY_TEAMID);
        } catch(JSONException e) {
            this.teamID = null;
        }
        try {
            this.teamName = jsonObject.getString(KEY_TEAMNAME);
        } catch(JSONException e) {
            this.teamName = null;
        }
        try {
            this.captainID = jsonObject.getInt(KEY_CAPTAINID);
        } catch(JSONException e) {
            this.captainID = null;
        }
        try {
            this.captainName = jsonObject.getString(KEY_CAPTAINNAME);
        } catch(JSONException e) {
            this.captainName = null;
        }
        try {
            this.teamColorPrimary = jsonObject.getString(KEY_TEAMCOLORPRIMARY);
        } catch(JSONException e) {
            this.teamColorPrimary = null;
        }
        try {
            this.teamColorSecondary = jsonObject.getString(KEY_TEAMCOLORSECONDARY);
        } catch(JSONException e) {
            this.teamColorSecondary = null;
        }
    }

    public Integer getTeamID() {
        return teamID;
    }

    public void setTeamID(Integer teamID) {
        this.teamID = teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getCaptainID() {
        return captainID;
    }

    public void setCaptainID(Integer captainID) {
        this.captainID = captainID;
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public String getTeamColorPrimary() {
        return "#" + teamColorPrimary;
    }

    public void setTeamColorPrimary(String teamColorPrimary) {
        this.teamColorPrimary = teamColorPrimary;
    }

    public String getTeamColorSecondary() {
        return "#" + teamColorSecondary;
    }

    public void setTeamColorSecondary(String teamColorSecondary) {
        this.teamColorSecondary = teamColorSecondary;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(KEY_TEAMID,teamID);
        values.put(KEY_TEAMNAME, teamName);
        values.put(KEY_CAPTAINID, captainID);
        values.put(KEY_CAPTAINNAME, captainName);
        values.put(KEY_TEAMCOLORPRIMARY, teamColorPrimary);
        values.put(KEY_TEAMCOLORSECONDARY, teamColorSecondary);
        return values;
    }
}
