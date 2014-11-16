package company.businessinc.database;

/**
 * Created by gp on 16/11/14.
 */
public class Team {
    private int teamID;
    private String teamName;
    private int teamCaptainID;
    private int teamColorPrimaryR;
    private int teamColorPrimaryG;
    private int teamColorPrimaryB;
    private int teamColorSecondaryR;
    private int teamColorSecondaryG;
    private int teamColorSecondaryB;

    public Team(int teamID, String teamName, int teamCaptainID, int teamColorPrimaryR, int teamColorPrimaryG, int teamColorPrimaryB, int teamColorSecondaryR, int teamColorSecondaryG, int teamColorSecondaryB) {
        this.teamID = teamID;
        this.teamName = teamName;
        this.teamCaptainID = teamCaptainID;
        this.teamColorPrimaryR = teamColorPrimaryR;
        this.teamColorPrimaryG = teamColorPrimaryG;
        this.teamColorPrimaryB = teamColorPrimaryB;
        this.teamColorSecondaryR = teamColorSecondaryR;
        this.teamColorSecondaryG = teamColorSecondaryG;
        this.teamColorSecondaryB = teamColorSecondaryB;
    }


    public int getTeamID() {
        return teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getTeamCaptainID() {
        return teamCaptainID;
    }

    public int getTeamColorPrimaryR() {
        return teamColorPrimaryR;
    }

    public int getTeamColorPrimaryG() {
        return teamColorPrimaryG;
    }

    public int getTeamColorPrimaryB() {
        return teamColorPrimaryB;
    }

    public int getTeamColorSecondaryR() {
        return teamColorSecondaryR;
    }

    public int getTeamColorSecondaryG() {
        return teamColorSecondaryG;
    }

    public int getTeamColorSecondaryB() {
        return teamColorSecondaryB;
    }
}
