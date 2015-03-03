package company.businessinc.endpoints;

import company.businessinc.dataModels.ResponseStatus;

public interface LeagueListInterface {
    public void leagueListCallback(ResponseStatus responseStatus, boolean liveCall);
}
