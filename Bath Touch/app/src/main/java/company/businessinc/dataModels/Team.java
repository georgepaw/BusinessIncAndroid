package company.businessinc.dataModels;

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
