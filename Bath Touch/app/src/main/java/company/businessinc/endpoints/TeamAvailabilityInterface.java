package company.businessinc.endpoints;

import java.util.List;

import company.businessinc.dataModels.Player;

public interface TeamAvailabilityInterface {
    public void teamAvailabilityCallback(List<Player> data, int matchID);
}
