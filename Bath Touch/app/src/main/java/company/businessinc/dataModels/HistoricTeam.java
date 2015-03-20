package company.businessinc.dataModels;

import android.content.ContentValues;
import android.database.Cursor;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by Grzegorz on 20/03/2015.
 */
public class HistoricTeam {
    private Integer teamID;
    private LinkedList<Integer> points;
    private Integer played;

    public static final String KEY_TEAMID = "teamID";
    public static final String KEY_POINTS = "points";
    public static final String KEY_PLAYED = "captainID";
    public static final String[] COLUMNS = {KEY_TEAMID, KEY_POINTS, KEY_PLAYED};
    public static final String CREATE_TABLE =   KEY_TEAMID + "\tINTEGER,\n" +
                                                KEY_POINTS + "\tTEXT,\n" +
                                                KEY_PLAYED + "\tINTEGER";

    public HistoricTeam(Integer teamID, LinkedList<Integer> points, Integer played) {
        this.teamID = teamID;
        this.points = points;
        this.played = played;
    }

    public HistoricTeam(Cursor cursor){
        try {
            this.teamID = cursor.getInt(cursor.getColumnIndex(KEY_TEAMID));
        } catch(Exception e) {
            this.teamID = null;
        }
        try {
            this.points = convertStringToPoints(cursor.getString(cursor.getColumnIndex(KEY_POINTS)));
        } catch(Exception e) {
            this.points = null;
        }
        try {
            this.played = cursor.getInt(cursor.getColumnIndex(KEY_PLAYED));
        } catch(Exception e) {
            this.played = null;
        }
    }

    public HistoricTeam(JSONObject jsonObject) throws JSONException {
        try {
            this.teamID = jsonObject.getInt(KEY_TEAMID);
        } catch(JSONException e) {
            this.teamID = null;
        }
        try {
            this.points = convertStringToPoints(jsonObject.getString(KEY_POINTS));
        } catch(JSONException e) {
            this.points = null;
        }
        try {
            this.played = jsonObject.getInt(KEY_PLAYED);
        } catch(JSONException e) {
            this.played = null;
        }
    }

    public LinkedList<Integer> convertStringToPoints(String points){
        LinkedList<Integer> output = new LinkedList<>();
        //TODO fix this once we have the endpoint working
        return output;
    }

    public String convertPointsToString(LinkedList<Integer> points){
        String output = "";
        //TODO fix this once we have the endpoint
        return output;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(KEY_TEAMID,teamID);
        values.put(KEY_POINTS, convertPointsToString(points));
        values.put(KEY_PLAYED, played);
        return values;
    }

    public Integer getTeamID() {
        return teamID;
    }

    public void setTeamID(Integer teamID) {
        this.teamID = teamID;
    }

    public LinkedList<Integer> getPoints() {
        return points;
    }

    public void setPoints(LinkedList<Integer> points) {
        this.points = points;
    }

    public Integer getPlayed() {
        return played;
    }

    public void setPlayed(Integer played) {
        this.played = played;
    }
}
