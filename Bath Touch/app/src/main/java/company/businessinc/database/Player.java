package company.businessinc.database;

/**
 * Created by gp on 16/11/14.
 */
public class Player {
    private int teamID;
    private int playerID;
    private int userID;
    private int playerAvailability;

    public Player(int teamID, int playerID, int userID, int playerAvailability) {
        this.teamID = teamID;
        this.playerID = playerID;
        this.userID = userID;
        this.playerAvailability = playerAvailability;
    }

    public int getTeamID() {
        return teamID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getUserID() {
        return userID;
    }

    public int getPlayerAvailability() {
        return playerAvailability;
    }
}
