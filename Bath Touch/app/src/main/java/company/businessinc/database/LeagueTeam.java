package company.businessinc.database;

/**
 * Created by gp on 16/11/14.
 */
public class LeagueTeam {
    private int LeagueID;
    private int TeamID;
    private int LeaguePoints;
    private int PointsFor;
    private int PointsAgainst;
    private int Position;

    public LeagueTeam(int leagueID, int teamID, int leaguePoints, int pointsFor, int pointsAgainst, int position) {
        LeagueID = leagueID;
        TeamID = teamID;
        LeaguePoints = leaguePoints;
        PointsFor = pointsFor;
        PointsAgainst = pointsAgainst;
        Position = position;
    }

    public int getLeagueID() {
        return LeagueID;
    }

    public int getTeamID() {
        return TeamID;
    }

    public int getLeaguePoints() {
        return LeaguePoints;
    }

    public int getPointsFor() {
        return PointsFor;
    }

    public int getPointsAgainst() {
        return PointsAgainst;
    }

    public int getPosition() {
        return Position;
    }
}
