package company.businessinc.bathtouch;

import java.util.ArrayList;
import java.util.Date;

import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Match;

/**
 * Created by user on 21/11/14.
 */
public class TeamResultsData {

    public ArrayList<Match> matches = new ArrayList<Match>();
//    public ArrayList<String> cards = new ArrayList<String>();

    private String teamName = "Business Inc";

    public TeamResultsData(){
        //TODO API CALLS TO GET DATA FROM SERVER

        matches.add(new Match("Business Inc", "Autistics United", 1, "JimRef", new Date(2006,11,1), "Home", 8, 3));
        matches.add(new Match("Business Inc", "Another Team", 2, "JimRef", new Date(2006,11,2), "Home", 5, 11));

    }

    public String getTeamName(){
        return teamName;
    }

    public int size(){
        return 8;
    }
}
