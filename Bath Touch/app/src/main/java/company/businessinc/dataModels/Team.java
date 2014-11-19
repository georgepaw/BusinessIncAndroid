package company.businessinc.dataModels;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gp on 18/11/14.
 */
public class Team {
    private int teamID;
    private String teamName;
    private int captainID;
    private String captainName;
    private int teamColorPrimary;
    private int teamColorSecondary;


    public Team(int teamID, String teamName, int captainID, String captainName, int teamColorPrimary, int teamColorSecondary) {
        this.teamID = teamID;
        this.teamName = teamName;
        this.captainID = captainID;
        this.captainName = captainName;
        this.teamColorPrimary = teamColorPrimary;
        this.teamColorSecondary = teamColorSecondary;
    }

    public Team (JSONObject jsonObject) throws JSONException{
        this.teamID = jsonObject.getInt("teamID");
        this.teamName = jsonObject.getString("teamName");
        this.captainID = jsonObject.getInt("captainID");
        this.captainName = jsonObject.getString("captainName");
        this.teamColorPrimary = jsonObject.getInt("teamColorPrimary");
        this.teamColorSecondary = jsonObject.getInt("teamColorSecondary");
    }

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getCaptainID() {
        return captainID;
    }

    public void setCaptainID(int captainID) {
        this.captainID = captainID;
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public int getTeamColorPrimary() {
        return teamColorPrimary;
    }

    public void setTeamColorPrimary(int teamColorPrimary) {
        this.teamColorPrimary = teamColorPrimary;
    }

    public int getTeamColorSecondary() {
        return teamColorSecondary;
    }

    public void setTeamColorSecondary(int teamColorSecondary) {
        this.teamColorSecondary = teamColorSecondary;
    }
}
