package company.businessinc.endpoints;

import company.businessinc.dataModels.ResponseStatus;

public interface LeagueHistoricInterface {
    public void leagueHistoricCallback(ResponseStatus responseStatus, int leagueID);
}
