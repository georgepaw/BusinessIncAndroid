package company.businessinc.bathtouch.ApdaterData;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
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

    private String TAG = "HomeCardData";
    public List<LeagueTeam> teams;
    public List<String> cards = new ArrayList<String>();

    public HomeCardData(){

        getLeagueTeams();

        if(teams == null) {
            //Add dummy data if callback is returning nothing
            teams = new ArrayList<LeagueTeam>();
            teams.add(new LeagueTeam("CompSci Vipers", 6, 2, 0, 0, 0, 1, 5, 0));
            teams.add(new LeagueTeam("TeamB", 4, 1, 1, 0, 0, 2, 3, 0));
            teams.add(new LeagueTeam("TeamC", 2, 0, 2, 1, 0, 3, 1, 0));
            Log.e(TAG, "teams == null");
        }

        cards.add("Hello card");
        cards.add("Team Card");
    }

    public List<LeagueTeam> getTopTeams(){
        List<LeagueTeam> topTeams = new ArrayList<LeagueTeam>();
        Collections.sort(teams,new Comparator<LeagueTeam>() {
            @Override
            public int compare(LeagueTeam T1, LeagueTeam T2) {
                return T1.getPosition() - T2.getPosition();
            }
        });

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
        new LeagueView(this, 3).execute();
    }
    public void leagueViewCallback(List<LeagueTeam> data) {
        if(data != null)
            teams = data;
    }
}
