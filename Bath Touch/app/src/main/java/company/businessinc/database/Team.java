package company.businessinc.database;

/**
 * Created by gp on 16/11/14.
 */
public class Team {
    private int TeamID;
    private String TeamName;
    private int TeamCaptainID;
    private int TeamColorPrimaryR;
    private int TeamColorPrimaryG;
    private int TeamColorPrimaryB;
    private int TeamColorSecondaryR;
    private int TeamColorSecondaryG;
    private int TeamColorSecondaryB;

    public Team(int teamID, String teamName, int teamCaptainID, int teamColorPrimaryR, int teamColorPrimaryG, int teamColorPrimaryB, int teamColorSecondaryR, int teamColorSecondaryG, int teamColorSecondaryB) {
        TeamID = teamID;
        TeamName = teamName;
        TeamCaptainID = teamCaptainID;
        TeamColorPrimaryR = teamColorPrimaryR;
        TeamColorPrimaryG = teamColorPrimaryG;
        TeamColorPrimaryB = teamColorPrimaryB;
        TeamColorSecondaryR = teamColorSecondaryR;
        TeamColorSecondaryG = teamColorSecondaryG;
        TeamColorSecondaryB = teamColorSecondaryB;
    }

    public int getTeamID() {
        return TeamID;
    }

    public String getTeamName() {
        return TeamName;
    }

    public int getTeamCaptainID() {
        return TeamCaptainID;
    }

    public int getTeamColorPrimaryR() {
        return TeamColorPrimaryR;
    }

    public int getTeamColorPrimaryG() {
        return TeamColorPrimaryG;
    }

    public int getTeamColorPrimaryB() {
        return TeamColorPrimaryB;
    }

    public int getTeamColorSecondaryR() {
        return TeamColorSecondaryR;
    }

    public int getTeamColorSecondaryG() {
        return TeamColorSecondaryG;
    }

    public int getTeamColorSecondaryB() {
        return TeamColorSecondaryB;
    }
}
