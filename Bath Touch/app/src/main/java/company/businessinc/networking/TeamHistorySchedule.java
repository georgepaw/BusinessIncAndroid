package company.businessinc.networking;

import java.util.Date;
import java.util.List;

import company.businessinc.database.Match;

/**
 * Created by gp on 16/11/14.
 */
public class TeamHistorySchedule {
    static private String endPoint = "/team/history/schedule";

    public List<Match> getGames(int leagueID, int teamID, Date dateFrom, Date dateTo){
        return null;
    }

    public String getEndPoint() {
        return endPoint;
    }
}
