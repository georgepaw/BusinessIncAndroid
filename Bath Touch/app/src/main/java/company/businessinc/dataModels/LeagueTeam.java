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
public class LeagueTeam {
    private String teamName;
    private Integer teamID;
    private Integer leaguePoints;
    private Integer win;
    private Integer draw;
    private Integer lose;
    private Integer forfeit;
    private Integer position;
    private Integer pointsFor;
    private Integer pointsAgainst;

    public static final String KEY_TEAMNAME = "teamName";
    public static final String KEY_TEAMID = "teamID";
    public static final String KEY_LEAGUEPOINTS = "leaguePoints";
    public static final String KEY_WIN = "win";
    public static final String KEY_DRAW = "draw";
    public static final String KEY_LOSE = "lose";
    public static final String KEY_FORFEIT = "forfeit";
    public static final String KEY_POSITION = "position";
    public static final String KEY_POINTSFOR = "pointsFor";
    public static final String KEY_POINTSAGAINST = "pointsAgainst";
    public static final String[] COLUMNS = {KEY_TEAMNAME, KEY_TEAMID,KEY_LEAGUEPOINTS, KEY_WIN, KEY_DRAW, KEY_LOSE, KEY_FORFEIT, KEY_POSITION, KEY_POINTSFOR, KEY_POINTSAGAINST};
    public static final String CREATE_TABLE =   KEY_TEAMNAME + "\tTEXT,\n" +
                                                KEY_TEAMID   + "\tINTEGER,\n" +
                                                KEY_LEAGUEPOINTS + "\tINTEGER,\n" +
                                                KEY_WIN + "\tINTEGER,\n" +
                                                KEY_DRAW + "\tINTEGER,\n" +
                                                KEY_LOSE + "\tINTEGER,\n" +
                                                KEY_FORFEIT + "\tINTEGER,\n" +
                                                KEY_POSITION + "\tINTEGER,\n" +
                                                KEY_POINTSFOR + "\tINTEGER,\n" +
                                                KEY_POINTSAGAINST + "\tINTEGER";

    public LeagueTeam(String teamName, Integer teamID, Integer leaguePoints, Integer win, Integer draw, Integer lose, Integer forfeit, Integer position, Integer pointsFor, Integer pointsAgainst) {
        this.teamName = teamName;
        this.teamID = teamID;
        this.leaguePoints = leaguePoints;
        this.win = win;
        this.draw = draw;
        this.lose = lose;
        this.forfeit = forfeit;
        this.position = position;
        this.pointsFor = pointsFor;
        this.pointsAgainst = pointsAgainst;
    }

    public LeagueTeam(Cursor cursor){
        try {
            this.teamName = cursor.getString(cursor.getColumnIndex(KEY_TEAMNAME));
        } catch(Exception e) {
            this.teamName = null;
        }
        try {
            this.teamID = cursor.getInt(cursor.getColumnIndex(KEY_TEAMID));
        } catch(Exception e) {
            this.teamID = null;
        }
        try {
            this.leaguePoints = cursor.getInt(cursor.getColumnIndex(KEY_LEAGUEPOINTS));
        } catch(Exception e) {
            this.leaguePoints = null;
        }
        try {
            this.win = cursor.getInt(cursor.getColumnIndex(KEY_WIN));
        } catch(Exception e) {
            this.win = null;
        }
        try {
            this.draw = cursor.getInt(cursor.getColumnIndex(KEY_DRAW));
        } catch(Exception e) {
            this.draw = null;
        }
        try {
            this.lose = cursor.getInt(cursor.getColumnIndex(KEY_LOSE));
        } catch(Exception e) {
            this.lose = null;
        }
        try {
            this.forfeit = cursor.getInt(cursor.getColumnIndex(KEY_FORFEIT));
        } catch(Exception e) {
            this.forfeit = null;
        }
        try {
            this.position = cursor.getInt(cursor.getColumnIndex(KEY_POSITION));
        } catch(Exception e) {
            this.position = null;
        }
        try {
            this.pointsFor = cursor.getInt(cursor.getColumnIndex(KEY_POINTSFOR));
        } catch(Exception e) {
            this.pointsFor = null;
        }
        try {
            this.pointsAgainst = cursor.getInt(cursor.getColumnIndex(KEY_POINTSAGAINST));
        } catch(Exception e) {
            this.pointsAgainst = null;
        }
    }

    public LeagueTeam(JSONObject jsonObject) throws JSONException{
        try {
            this.teamName = jsonObject.getString(KEY_TEAMNAME);
        } catch(JSONException e) {
            this.teamName = null;
        }
        try {
            this.teamID = jsonObject.getInt(KEY_TEAMID);
        } catch(JSONException e) {
            this.teamID = null;
        }
        try {
            this.leaguePoints = jsonObject.getInt(KEY_LEAGUEPOINTS);
        } catch(JSONException e) {
            this.leaguePoints = null;
        }
        try {
            this.win = jsonObject.getInt(KEY_WIN);
        } catch(JSONException e) {
            this.win = null;
        }
        try {
            this.draw = jsonObject.getInt(KEY_DRAW);
        } catch(JSONException e) {
            this.draw = null;
        }
        try {
            this.lose = jsonObject.getInt(KEY_LOSE);
        } catch(JSONException e) {
            this.lose = null;
        }
        try {
            this.forfeit = jsonObject.getInt(KEY_FORFEIT);
        } catch(JSONException e) {
            this.forfeit = null;
        }
        try {
            this.position = jsonObject.getInt(KEY_POSITION);
        } catch(JSONException e) {
            this.position = null;
        }
        try {
            this.pointsFor = jsonObject.getInt(KEY_POINTSFOR);
        } catch(JSONException e) {
            this.pointsFor = null;
        }
        try {
            this.pointsAgainst = jsonObject.getInt(KEY_POINTSAGAINST);
        } catch(JSONException e) {
            this.pointsAgainst = null;
        }
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getTeamID() {
        return teamID;
    }

    public void setTeamID(Integer teamID) {
        this.teamID = teamID;
    }

    public Integer getLeaguePoints() {
        return leaguePoints;
    }

    public void setLeaguePoints(Integer leaguePoints) {
        this.leaguePoints = leaguePoints;
    }

    public Integer getWin() {
        return win;
    }

    public void setWin(Integer win) {
        this.win = win;
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Integer getLose() {
        return lose;
    }

    public void setLose(Integer lose) {
        this.lose = lose;
    }

    public Integer getForfeit() {
        return forfeit;
    }

    public void setForfeit(Integer forfeit) {
        this.forfeit = forfeit;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getPointsFor() {
        return pointsFor;
    }

    public void setPointsFor(Integer pointsFor) {
        this.pointsFor = pointsFor;
    }

    public Integer getPointsAgainst() {
        return pointsAgainst;
    }

    public void setPointsAgainst(Integer pointsAgainst) {
        this.pointsAgainst = pointsAgainst;
    }

    public static List<LeagueTeam> cursorToList(Cursor cursor){
        List<LeagueTeam> output = new ArrayList<>();
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                output.add(new LeagueTeam(cursor));
                cursor.moveToNext();
            }
        }
        return output;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(KEY_TEAMNAME, teamName);
        values.put(KEY_TEAMID, teamID);
        values.put(KEY_LEAGUEPOINTS, leaguePoints);
        values.put(KEY_WIN, win);
        values.put(KEY_DRAW, draw);
        values.put(KEY_LOSE, lose);
        values.put(KEY_FORFEIT, forfeit);
        values.put(KEY_POSITION, position);
        values.put(KEY_POINTSFOR, pointsFor);
        values.put(KEY_POINTSAGAINST, pointsAgainst);
        return values;
    }
}
