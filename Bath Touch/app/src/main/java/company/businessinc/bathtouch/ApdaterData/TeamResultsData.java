package company.businessinc.bathtouch.ApdaterData;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import company.businessinc.dataModels.Match;

/**
 * Created by user on 21/11/14.
 */
public class TeamResultsData {

    public ArrayList<Match> matches = new ArrayList<Match>();
//    public ArrayList<String> cards = new ArrayList<String>();

    private String teamName = "CompSci Vipers";

    public TeamResultsData(){
        //TODO API CALLS TO GET DATA FROM SERVER

        matches.add(new Match(1,1,1, "CompSci Vipers", "EE Tigers", 1, "Ref1", new GregorianCalendar(2006,11,1).getTime(), "Home", 28, 4, false));
        matches.add(new Match(1,1,1, "CompSci Vipers", "HP All Stars", 2, "Ref1", new GregorianCalendar(2006,11,9).getTime(), "Home", 10, 10, false));
        matches.add(new Match(1,1,1, "CompSci Vipers", "EE Tigers", 1, "Ref1", new GregorianCalendar(2006,11,1).getTime(), "Home", 25, 2, false));
        matches.add(new Match(1,1,1,"CompSci Vipers", "EE Tigers", 2, "Ref1", new GregorianCalendar(2006,11,9).getTime(), "Home", 13, 13, false));
        matches.add(new Match(1,1,1,"HP All Stars", "CompSci Vipers",  1, "Ref1", new GregorianCalendar(2006,11,4).getTime(), "Home", 6, 19, false));
        matches.add(new Match(1,1,1,"EE Tigers","CompSci Vipers", 1, "Ref1", new GregorianCalendar(2006,11,4).getTime(), "Home", 31, 1, false));
        matches.add(new Match(1,1,1,"CompSci Vipers", "HP All Stars", 1, "Ref1", new GregorianCalendar(2006,11,4).getTime(), "Home", 7, 12, false));


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
