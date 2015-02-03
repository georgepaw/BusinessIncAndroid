package company.businessinc.endpoints;


import company.businessinc.dataModels.Status;

public interface UpAvailabilityInterface {
    public void upAvailabilityCallback(boolean isPlaying, UpAvailability.CallType callType, int matchID, int userID);
}
