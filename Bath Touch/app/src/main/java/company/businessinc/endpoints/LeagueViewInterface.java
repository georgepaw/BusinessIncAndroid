package company.businessinc.endpoints;

import java.util.List;

import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.User;

public interface LeagueViewInterface {
    public void leagueViewCallback(List<LeagueTeam> data);
}
