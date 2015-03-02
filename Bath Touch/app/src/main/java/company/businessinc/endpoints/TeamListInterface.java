package company.businessinc.endpoints;

import company.businessinc.dataModels.ResponseStatus;

public interface TeamListInterface {
    public void teamListCallback(ResponseStatus successful, TeamList.CallType callType, int leagueID);
}
