package company.businessinc.bathtouch.ApdaterData;

import java.util.ArrayList;
import java.util.Date;

import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Match;

/**
 * Created by user on 22/11/14.
 */
public class LeagueTableData {

    public ArrayList<LeagueTeam> leagueTeams = new ArrayList<LeagueTeam>();
//    public ArrayList<String> cards = new ArrayList<String>();

    private String LeagueName = "Bath Touch Summer League 2015";

    public LeagueTableData() {
        //TODO API CALLS TO GET DATA FROM SERVER

        leagueTeams.add(new LeagueTeam("TeamA", 18, 10, 0, 0, 0 , 1, 5, 0));
        leagueTeams.add(new LeagueTeam("TeamB", 4, 1, 1, 0, 0 , 2, 3, 0));
        leagueTeams.add(new LeagueTeam("TeamC", 2, 0, 2, 1, 0, 3, 1, 0));
        leagueTeams.add(new LeagueTeam("TeamD", 1, 2, 0, 0, 0 , 4, 5, 0));
        leagueTeams.add(new LeagueTeam("TeamE", 4, 1, 1, 0, 0 , 5, 3, 0));
        leagueTeams.add(new LeagueTeam("TeamF", 1, 0, 2, 1, 0, 6, 1, 0));
        leagueTeams.add(new LeagueTeam("TeamG", 1, 2, 0, 0, 0 , 7, 5, 0));
        leagueTeams.add(new LeagueTeam("TeamH", 1, 1, 1, 0, 0 , 8, 3, 0));
        leagueTeams.add(new LeagueTeam("TeamI", 1, 0, 2, 1, 0, 9, 1, 0));



    }


    public LeagueTeam getTeam(int i) {
        return leagueTeams.get(i);
    }

    public int size() {
        return leagueTeams.size();
    }
}


