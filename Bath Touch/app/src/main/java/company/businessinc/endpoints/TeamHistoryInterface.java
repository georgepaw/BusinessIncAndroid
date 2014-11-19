package company.businessinc.endpoints;

import java.util.List;

import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.User;

public interface TeamHistoryInterface {
    public void teamHistoryCallback(List<Match> data);
}
