package company.businessinc.dataModels;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

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

    public League(JSONObject jsonObject){
        try {
            this.leagueID = jsonObject.getInt("leagueID");
        } catch (JSONException e){
            this.leagueID = null;
        }
        try {
            this.leagueName = jsonObject.getString("leagueName");
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
}
