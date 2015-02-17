package company.businessinc.endpoints;

import java.util.List;

import company.businessinc.dataModels.Player;
import company.businessinc.dataModels.ResponseStatus;

public interface TeamAvailabilityInterface {
    public void teamAvailabilityCallback(ResponseStatus responseStatus, int matchID);
}
