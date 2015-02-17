package company.businessinc.endpoints;


import company.businessinc.dataModels.ResponseStatus;

public interface UpAvailabilityInterface {
    public void upAvailabilityCallback(ResponseStatus responseStatus, boolean isPlaying, UpAvailability.CallType callType, int matchID, int userID);
}
