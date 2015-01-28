package company.businessinc.endpoints;

import java.util.List;

import company.businessinc.dataModels.League;

public interface TeamLeaguesInterface {
    public void teamLeaguesCallback(List<League> data);
}
