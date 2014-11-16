package company.businessinc.database;

import java.util.Date;

/**
 * Created by gp on 16/11/14.
 */
public class Match {
    private int matchId;
    private int teamOneID;
    private int teamTwoID;
    private int refID;
    private int teamOnePoints;
    private int teamTwoPoints;
    private Date matchDate;
    private int leagueId;

    public Match(int matchId, int teamOneID, int teamTwoID, int refID, int teamOnePoints, int teamTwoPoints, Date matchDate, int leagueId) {
        this.matchId = matchId;
        this.teamOneID = teamOneID;
        this.teamTwoID = teamTwoID;
        this.refID = refID;
        this.teamOnePoints = teamOnePoints;
        this.teamTwoPoints = teamTwoPoints;
        this.matchDate = matchDate;
        this.leagueId = leagueId;
    }

    public int getMatchId() {
        return matchId;
    }

    public int getTeamOneID() {
        return teamOneID;
    }

    public int getTeamTwoID() {
        return teamTwoID;
    }

    public int getRefID() {
        return refID;
    }

    public int getTeamOnePoints() {
        return teamOnePoints;
    }

    public int getTeamTwoPoints() {
        return teamTwoPoints;
    }

    public Date getMatchDate() {
        return matchDate;
    }

    public int getLeagueId() {
        return leagueId;
    }
}
