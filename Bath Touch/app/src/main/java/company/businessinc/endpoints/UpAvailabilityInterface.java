package company.businessinc.endpoints;


import company.businessinc.dataModels.Status;

public interface UpAvailabilityInterface {
    public void upAvailabilityCallback(Status data, UpAvailability.CallType callType);
}
