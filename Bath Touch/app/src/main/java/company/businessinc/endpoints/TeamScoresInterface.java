package company.businessinc.endpoints;

import java.util.List;

import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.dataModels.User;

public interface TeamScoresInterface {
    public void teamScoresCallback(ResponseStatus responseStatus, int leagueID, int teamID);
}
