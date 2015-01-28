package company.businessinc.endpoints;

import java.util.List;

import company.businessinc.dataModels.Match;

public interface LeagueScheduleInterface {
    public void leagueScheduleCallback(List<Match> data);
}
