package company.businessinc.bathtouch.ApdaterData;

import java.util.ArrayList;
import java.util.Date;

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

        matches.add(new Match(1,1,1, "Autistics United", "Business Inc", 1, "JimRef", new Date(2006,11,1), "Home", 8, 3, false));
        matches.add(new Match(1,1,1, "Business Inc", "Another Team", 2, "JimRef", new Date(2006,11,9), "Home", 1, 27, false));
        matches.add(new Match(1,1,1, "Business Inc", "More Rugby Teams", 1, "JimRef", new Date(2006,11,4), "Home", 19, 11, false));
        matches.add(new Match(1,1,1, "Myle's Magnificents","Business Inc", 1, "JimRef", new Date(2006,11,4), "Home", 7, 7, false));
        matches.add(new Match(1,1,1, "George's Whipping Boys", "Business Inc", 1, "JimRef", new Date(2006,11,4), "Home", 5, 12, false));
        matches.add(new Match(1,1,1, "Autistics United", "Business Inc", 1, "JimRef", new Date(2006,11,1), "Home", 9, 12, false));
        matches.add(new Match(1,1,1,"Business Inc", "Another Team", 2, "JimRef", new Date(2006,11,9), "Home", 13, 13, false));
        matches.add(new Match(1,1,1,"More Rugby Teams", "Business Inc",  1, "JimRef", new Date(2006,11,4), "Home", 6, 13, false));
        matches.add(new Match(1,1,1,"Business Inc","Myle's Magnificents", 1, "JimRef", new Date(2006,11,4), "Home", 31, 1, false));
        matches.add(new Match(1,1,1,"Business Inc", "George's Whipping Boys", 1, "JimRef", new Date(2006,11,4), "Home", 10, 12, false));


    }

    public String getTeamName(){
        return teamName;
    }

    public Match getMatch(int i){
        return matches.get(i);
    }

    public int size(){
        return matches.size();
    }
}
