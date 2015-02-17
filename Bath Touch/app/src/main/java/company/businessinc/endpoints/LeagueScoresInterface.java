package company.businessinc.endpoints;

import java.util.List;

import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.dataModels.User;

public interface LeagueScoresInterface {
    public void leagueScoresCallback(ResponseStatus responseStatus, int leagueID);
}
