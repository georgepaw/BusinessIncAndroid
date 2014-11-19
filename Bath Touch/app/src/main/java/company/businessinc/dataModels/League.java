package company.businessinc.dataModels;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by gp on 18/11/14.
 */
public class League {
    private int leagueID;
    private String leagueName;

    public League(int leagueID, String leagueName) {
        this.leagueID = leagueID;
        this.leagueName = leagueName;
    }

    public League(JSONObject jsonObject) throws JSONException{
        this.leagueID = jsonObject.getInt("leagueID");
        this.leagueName = jsonObject.getString("leagueName");
    }

    public int getLeagueID() {
        return leagueID;
    }

    public void setLeagueID(int leagueID) {
        this.leagueID = leagueID;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }
}
