package company.businessinc.endpoints;

import java.util.List;

import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.User;

public interface TeamScoresInterface {
    public void teamScoresCallback(List<Match> data, int leagueID, int teamID);
}
