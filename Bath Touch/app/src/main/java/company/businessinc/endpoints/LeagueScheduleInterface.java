package company.businessinc.endpoints;

import java.util.List;

import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.ResponseStatus;

public interface LeagueScheduleInterface {
    public void leagueScheduleCallback(ResponseStatus responseStatus, int leagueID);
}
