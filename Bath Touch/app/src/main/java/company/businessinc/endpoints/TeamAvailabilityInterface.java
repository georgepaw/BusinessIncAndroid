package company.businessinc.endpoints;

import company.businessinc.dataModels.ResponseStatus;

public interface TeamAvailabilityInterface {
    public void teamAvailabilityCallback(ResponseStatus responseStatus, int matchID);
}
