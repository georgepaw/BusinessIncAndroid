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

        matches.add(new Match( "Autistics United", "Business Inc", 1, "JimRef", new Date(2006,11,1), "Home", 8, 3));
        matches.add(new Match("Business Inc", "Another Team", 2, "JimRef", new Date(2006,11,9), "Home", 1, 27));
        matches.add(new Match("Business Inc", "More Rugby Teams", 1, "JimRef", new Date(2006,11,4), "Home", 19, 11));
        matches.add(new Match("Myle's Magnificents","Business Inc", 1, "JimRef", new Date(2006,11,4), "Home", 7, 7));
        matches.add(new Match("George's Whipping Boys", "Business Inc", 1, "JimRef", new Date(2006,11,4), "Home", 5, 12));
        matches.add(new Match( "Autistics United", "Business Inc", 1, "JimRef", new Date(2006,11,1), "Home", 9, 12));
        matches.add(new Match("Business Inc", "Another Team", 2, "JimRef", new Date(2006,11,9), "Home", 13, 13));
        matches.add(new Match("More Rugby Teams", "Business Inc",  1, "JimRef", new Date(2006,11,4), "Home", 6, 13));
        matches.add(new Match("Business Inc","Myle's Magnificents", 1, "JimRef", new Date(2006,11,4), "Home", 31, 1));
        matches.add(new Match("Business Inc", "George's Whipping Boys", 1, "JimRef", new Date(2006,11,4), "Home", 10, 12));


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
