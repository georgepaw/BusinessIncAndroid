package company.businessinc.database;

/**
 * Created by gp on 16/11/14.
 */
public class LeagueTeam {
    private int leagueID;
    private int teamID;
    private int leaguePoints;
    private int pointsFor;
    private int pointsAgainst;

    public LeagueTeam(int leagueID, int teamID, int leaguePoints, int pointsFor, int pointsAgainst) {
        this.leagueID = leagueID;
        this.teamID = teamID;
        this.leaguePoints = leaguePoints;
        this.pointsFor = pointsFor;
        this.pointsAgainst = pointsAgainst;
    }

    public int getLeagueID() {
        return leagueID;
    }

    public int getTeamID() {
        return teamID;
    }

    public int getLeaguePoints() {
        return leaguePoints;
    }

    public int getPointsFor() {
        return pointsFor;
    }

    public int getPointsAgainst() {
        return pointsAgainst;
    }
}
