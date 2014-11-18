package company.businessinc.database;

/**
 * Created by gp on 16/11/14.
 */
public class Player {
    private int TeamID;
    private int PlayerID;
    private int UserID;
    private int PlayerAvailability;

    public Player(int teamID, int playerID, int userID, int playerAvailability) {
        TeamID = teamID;
        PlayerID = playerID;
        UserID = userID;
        PlayerAvailability = playerAvailability;
    }

    public int getTeamID() {
        return TeamID;
    }

    public int getPlayerID() {
        return PlayerID;
    }

    public int getUserID() {
        return UserID;
    }

    public int getPlayerAvailability() {
        return PlayerAvailability;
    }
}
