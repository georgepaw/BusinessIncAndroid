package company.businessinc.database;

/**
 * Created by gp on 16/11/14.
 */
public class League {
    private int leagueID;
    private String leagueName;

    public League(int leagueID, String leagueName){
        this.leagueID = leagueID;
        this.leagueName = leagueName;

    }

    //You can't create/edit the league through the app so there is no need for set functions

    public int getLeagueID(){
        return this.leagueID;
    }

    public String getLeagueName(){
        return this.leagueName;
    }
}
