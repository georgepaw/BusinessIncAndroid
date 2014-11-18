package company.businessinc.networking;

import java.util.Date;
import java.util.List;

import company.businessinc.dataModels.Match;

/**
 * Created by gp on 18/11/14.
 */
public class TeamHistory {
    private static final String endPoint = "/team/history";

    public static List<Match> get(int leagueID, int teamID, Date fromDate, Date toDate){
        return null;
    }
}
