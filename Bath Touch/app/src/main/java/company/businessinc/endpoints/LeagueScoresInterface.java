package company.businessinc.endpoints;

import company.businessinc.dataModels.ResponseStatus;

public interface LeagueScoresInterface {
    public void leagueScoresCallback(ResponseStatus responseStatus, int leagueID);
}
