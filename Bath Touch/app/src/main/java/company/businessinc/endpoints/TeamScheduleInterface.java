package company.businessinc.endpoints;

import company.businessinc.dataModels.ResponseStatus;

public interface TeamScheduleInterface {
    public void teamScheduleCallback(ResponseStatus responseStatus, int leagueID, int teamID);
}
