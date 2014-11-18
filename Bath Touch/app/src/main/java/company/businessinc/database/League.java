package company.businessinc.database;

/**
 * Created by gp on 16/11/14.
 */
public class League {
    private int LeagueID;
    private String LeagueName;

    public League(int LeagueID, String LeagueName){
        this.LeagueID = LeagueID;
        this.LeagueName = LeagueName;

    }

    //You can't create/edit the league through the app so there is no need for set functions

    public int getLeagueID(){
        return this.LeagueID;
    }

    public String getLeagueName(){
        return this.LeagueName;
    }
}
