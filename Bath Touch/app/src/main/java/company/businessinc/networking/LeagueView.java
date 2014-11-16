package company.businessinc.networking;

import java.util.List;

import company.businessinc.database.LeagueTeam;

/**
 * Created by gp on 16/11/14.
 */
public class LeagueView {
    static private String endPoint = "/league/view";

    public List<LeagueTeam> getStandings(int leagueID){
        return null;
    }

    public String getEndPoint() {
        return endPoint;
    }
}
