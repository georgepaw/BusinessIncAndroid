package company.businessinc.endpoints;

import company.businessinc.dataModels.ResponseStatus;

public interface LeagueViewInterface {
    public void leagueViewCallback(ResponseStatus responseStatus, int leagueID);
}
