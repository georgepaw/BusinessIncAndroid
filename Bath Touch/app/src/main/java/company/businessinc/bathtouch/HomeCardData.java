package company.businessinc.bathtouch;

import java.util.ArrayList;
import java.util.HashMap;

import company.businessinc.dataModels.LeagueTeam;

/**
 * Created by user on 20/11/14.
 */
public class HomeCardData {

    public ArrayList<LeagueTeam> teams = new ArrayList<LeagueTeam>();
    public ArrayList<String> cards = new ArrayList<String>();

    public HomeCardData(){
        //TODO API CALLS TO GET DATA FROM SERVER

        teams.add(new LeagueTeam("Business Inc United", 6, 2, 0, 0, 0 , 1, 5, 0));
        teams.add(new LeagueTeam("Autistics Athletic Club", 4, 1, 1, 0, 0 , 2, 3, 0));
        teams.add(new LeagueTeam("Dsylexis Tmea", 2, 0, 2, 1, 0, 3, 1, 0));
        cards.add("Hello card");
        cards.add("Team Card");
    }

    ArrayList<LeagueTeam> getTopTeams(){
        return teams;
    }

    ArrayList<String> getCardNames(){
        return cards;
    }

    public int size(){
        return 4;
    }

}
