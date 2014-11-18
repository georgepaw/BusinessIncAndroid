package company.businessinc.database;

/**
 * Created by gp on 16/11/14.
 */
public class Ref {
    private int userID;
    private int RefID;
    private int GamesRefereed;
    private String Qualification;

    public Ref(int userID, int refID, int gamesRefereed, String qualification) {
        this.userID = userID;
        this.RefID = refID;
        this.GamesRefereed = gamesRefereed;
        this.Qualification = qualification;
    }

    public int getUserID() {
        return userID;
    }

    public int getRefID() {
        return RefID;
    }

    public int getGamesRefereed() {
        return GamesRefereed;
    }

    public String getQualification() {
        return Qualification;
    }
}
