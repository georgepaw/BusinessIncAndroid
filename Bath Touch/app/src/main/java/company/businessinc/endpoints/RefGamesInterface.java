package company.businessinc.endpoints;

import java.util.List;

import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.User;

public interface RefGamesInterface {
    public void refGamesCallback(List<Match> data);
}
