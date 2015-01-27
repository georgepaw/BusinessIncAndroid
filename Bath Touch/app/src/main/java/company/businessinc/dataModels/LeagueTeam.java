package company.businessinc.dataModels;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gp on 18/11/14.
 */
public class LeagueTeam {
    private String teamName;
    private Integer leaguePoints;
    private Integer win;
    private Integer draw;
    private Integer lose;
    private Integer forfeit;
    private Integer position;
    private Integer pointsFor;
    private Integer pointsAgainst;

    public static final String KEY_TEAMNAME = "teamName";
    public static final String KEY_LEAGUEPOINTS = "leaguePoints";
    public static final String KEY_WIN = "win";
    public static final String KEY_DRAW = "draw";
    public static final String KEY_LOSE = "lose";
    public static final String KEY_FORFEIT = "forfeit";
    public static final String KEY_POSITION = "position";
    public static final String KEY_POINTSFOR = "pointsFor";
    public static final String KEY_POINTSAGAINST = "pointsAgainst";
    public static final String[] COLUMNS = {KEY_TEAMNAME,KEY_LEAGUEPOINTS, KEY_WIN, KEY_DRAW, KEY_LOSE, KEY_FORFEIT, KEY_POSITION, KEY_POINTSFOR, KEY_POINTSAGAINST};

    public LeagueTeam(String teamName, Integer leaguePoints, Integer win, Integer draw, Integer lose, Integer forfeit, Integer position, Integer pointsFor, Integer pointsAgainst) {
        this.teamName = teamName;
        this.leaguePoints = leaguePoints;
        this.win = win;
        this.draw = draw;
        this.lose = lose;
        this.forfeit = forfeit;
        this.position = position;
        this.pointsFor = pointsFor;
        this.pointsAgainst = pointsAgainst;
    }

    public LeagueTeam(JSONObject jsonObject) throws JSONException{
        try {
            this.teamName = jsonObject.getString("teamName");
        } catch(JSONException e) {
            this.teamName = null;
        }
        try {
            this.leaguePoints = jsonObject.getInt("leaguePoints");
        } catch(JSONException e) {
            this.leaguePoints = null;
        }
        try {
            this.win = jsonObject.getInt("win");
        } catch(JSONException e) {
            this.win = null;
        }
        try {
            this.draw = jsonObject.getInt("draw");
        } catch(JSONException e) {
            this.draw = null;
        }
        try {
            this.lose = jsonObject.getInt("lose");
        } catch(JSONException e) {
            this.lose = null;
        }
        try {
            this.forfeit = jsonObject.getInt("forfeit");
        } catch(JSONException e) {
            this.forfeit = null;
        }
        try {
            this.position = jsonObject.getInt("position");
        } catch(JSONException e) {
            this.position = null;
        }
        try {
            this.pointsFor = jsonObject.getInt("pointsFor");
        } catch(JSONException e) {
            this.pointsFor = null;
        }
        try {
            this.pointsAgainst = jsonObject.getInt("pointsAgainst");
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
}
