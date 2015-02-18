package company.businessinc.endpoints;

import company.businessinc.dataModels.ResponseStatus;

public interface TeamScoresInterface {
    public void teamScoresCallback(ResponseStatus responseStatus, int leagueID, int teamID);
}
