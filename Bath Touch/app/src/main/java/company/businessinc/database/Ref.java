package company.businessinc.database;

/**
 * Created by gp on 16/11/14.
 */
public class Ref {
    private int userID;
    private int refID;
    private int gamesRefereed;
    private String qualification;

    public Ref(int userID, int refID, int gamesRefereed, String qualification) {
        this.userID = userID;
        this.refID = refID;
        this.gamesRefereed = gamesRefereed;
        this.qualification = qualification;
    }

    public int getUserID() {
        return userID;
    }

    public int getRefID() {
        return refID;
    }

    public int getGamesRefereed() {
        return gamesRefereed;
    }

    public String getQualification() {
        return qualification;
    }
}
