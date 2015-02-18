package company.businessinc.endpoints;

import company.businessinc.dataModels.ResponseStatus;

public interface LeagueScheduleInterface {
    public void leagueScheduleCallback(ResponseStatus responseStatus, int leagueID);
}
