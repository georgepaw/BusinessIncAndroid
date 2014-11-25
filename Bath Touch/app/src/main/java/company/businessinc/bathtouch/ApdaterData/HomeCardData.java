package company.businessinc.bathtouch.ApdaterData;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import company.businessinc.bathtouch.R;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.endpoints.LeagueView;
import company.businessinc.endpoints.LeagueViewInterface;
import company.businessinc.endpoints.UserLogin;
import company.businessinc.networking.CheckNetworkConnection;

/**
 * Created by user on 20/11/14.
 */
public class HomeCardData implements LeagueViewInterface{

    public List<LeagueTeam> teams;
    public List<String> cards = new ArrayList<String>();

    public HomeCardData(){

        //TODO Uncomment this when we have data
//        getLeagueTeams();

        if(teams == null)
            teams = new ArrayList<LeagueTeam>();
        teams.add(new LeagueTeam("Business Inc United", 6, 2, 0, 0, 0 , 1, 5, 0));
        teams.add(new LeagueTeam("Autistics Athletic Club", 4, 1, 1, 0, 0 , 2, 3, 0));
        teams.add(new LeagueTeam("Dsylexis Tmea", 2, 0, 2, 1, 0, 3, 1, 0));
        cards.add("Hello card");
        cards.add("Team Card");
    }

    //TODO currently gets top 3 teams in list, not top 3 ranked by points etc.
    public List<LeagueTeam> getTopTeams(){
        List<LeagueTeam> topTeams = new ArrayList<LeagueTeam>();
        for(int i = 0; i < 3; i++) {
            topTeams.add(teams.get(i));
        }
        return topTeams;
    }

    public List<String> getCardNames(){
        return cards;
    }

    public int size(){
        return 4;
    }

    public void getLeagueTeams() {
        new LeagueView(this, 0).execute();
    }
    public void leagueViewCallback(List<LeagueTeam> data) {
        if(data != null) {
            teams = (ArrayList<LeagueTeam>) data;
        }
    }
}
