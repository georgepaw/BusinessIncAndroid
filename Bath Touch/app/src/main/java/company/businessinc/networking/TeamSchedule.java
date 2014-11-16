package company.businessinc.networking;

import java.util.List;

import company.businessinc.database.Match;

/**
 * Created by gp on 16/11/14.
 */
public class TeamSchedule {
    static private String endPoint = "/team/schedule";

    public List<Match> getGames(int leagueID, int teamID){
        return null;
    }

    public String getEndPoint() {
        return endPoint;
    }
}
