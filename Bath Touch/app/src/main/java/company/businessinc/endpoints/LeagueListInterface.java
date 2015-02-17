package company.businessinc.endpoints;

import java.util.List;

import company.businessinc.dataModels.League;
import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.dataModels.User;

public interface LeagueListInterface {
    public void leagueListCallback(ResponseStatus responseStatus);
}
