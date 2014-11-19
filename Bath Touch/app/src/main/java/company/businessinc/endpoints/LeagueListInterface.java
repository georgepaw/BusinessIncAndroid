package company.businessinc.endpoints;

import java.util.List;

import company.businessinc.dataModels.League;
import company.businessinc.dataModels.User;

public interface LeagueListInterface {
    public void leagueListCallback(List<League> data);
}
