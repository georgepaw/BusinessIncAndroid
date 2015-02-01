package company.businessinc.endpoints;

import java.util.List;

import company.businessinc.dataModels.Team;
import company.businessinc.dataModels.User;

public interface TeamListInterface {
    public void teamListCallback(List<Team> data, TeamList.CallType callType, int leagueID);
}
