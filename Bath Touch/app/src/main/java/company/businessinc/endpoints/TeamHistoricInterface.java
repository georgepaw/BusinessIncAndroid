package company.businessinc.endpoints;

import company.businessinc.dataModels.ResponseStatus;

public interface TeamHistoricInterface {
    public void teamHistoricCallback(ResponseStatus responseStatus, int leagueID, int teamID);
}
