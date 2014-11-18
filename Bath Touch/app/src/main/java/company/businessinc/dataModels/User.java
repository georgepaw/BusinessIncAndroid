package company.businessinc.dataModels;

/**
 * Created by gp on 18/11/14.
 */
public class User {
    private int userID;

    public User(int userID) {

        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
