package company.businessinc.dataModels;

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
    private Integer teamColorPrimary;
    private Integer teamColorSecondary;


    public Team(Integer teamID, String teamName, Integer captainID, String captainName, Integer teamColorPrimary, Integer teamColorSecondary) {
        this.teamID = teamID;
        this.teamName = teamName;
        this.captainID = captainID;
        this.captainName = captainName;
        this.teamColorPrimary = teamColorPrimary;
        this.teamColorSecondary = teamColorSecondary;
    }

    public Team (JSONObject jsonObject) throws JSONException{
        try {
            this.teamID = jsonObject.getInt("teamID");
        } catch(JSONException e) {
            this.teamID = null;
        }
        try {
            this.teamName = jsonObject.getString("teamName");
        } catch(JSONException e) {
            this.teamName = null;
        }
        try {
            this.captainID = jsonObject.getInt("captainID");
        } catch(JSONException e) {
            this.captainID = null;
        }
        try {
            this.captainName = jsonObject.getString("captainName");
        } catch(JSONException e) {
            this.captainName = null;
        }
        try {
            this.teamColorPrimary = jsonObject.getInt("teamColorPrimary");
        } catch(JSONException e) {
            this.teamColorPrimary = null;
        }
        try {
            this.teamColorSecondary = jsonObject.getInt("teamColorSecondary");
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

    public Integer getTeamColorPrimary() {
        return teamColorPrimary;
    }

    public void setTeamColorPrimary(Integer teamColorPrimary) {
        this.teamColorPrimary = teamColorPrimary;
    }

    public Integer getTeamColorSecondary() {
        return teamColorSecondary;
    }

    public void setTeamColorSecondary(Integer teamColorSecondary) {
        this.teamColorSecondary = teamColorSecondary;
    }
}
