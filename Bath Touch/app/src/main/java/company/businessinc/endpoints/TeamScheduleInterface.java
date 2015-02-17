package company.businessinc.endpoints;

import java.util.List;

import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.dataModels.User;

public interface TeamScheduleInterface {
    public void teamScheduleCallback(ResponseStatus responseStatus, int leagueID, int teamID);
}
