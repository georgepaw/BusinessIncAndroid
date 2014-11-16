package company.businessinc.networking;

import java.util.List;

import company.businessinc.database.Match;

/**
 * Created by gp on 16/11/14.
 */
public class RefGames {
    static private String endPoint = "/ref/games";

    public List<Match> getRefGames(Integer refID){
        return null;
    }

    public List<Match> getRefGames(){
        return this.getRefGames(null);
    }

    public String getEndPoint() {
        return endPoint;
    }
}
