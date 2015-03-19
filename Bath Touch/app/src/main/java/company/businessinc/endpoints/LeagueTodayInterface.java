package company.businessinc.endpoints;

import company.businessinc.dataModels.ResponseStatus;

public interface LeagueTodayInterface {
    public void leagueTodayCallback(ResponseStatus responseStatus, int leagueID);
}
