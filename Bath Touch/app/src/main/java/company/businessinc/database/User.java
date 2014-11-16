package company.businessinc.database;

/**
 * Created by gp on 16/11/14.
 */
public class User {
    private int userID;
    private String userName;
    private int accessRights;
    private String name;
    private String password;
    private String email;
    private boolean isPlayerGhost;
    private boolean isReferee;

    public User(int userID, String userName, int accessRights, String name, String password, String email, boolean isPlayerGhost, boolean isReferee) {
        this.userID = userID;
        this.userName = userName;
        this.accessRights = accessRights;
        this.name = name;
        this.password = password;
        this.email = email;
        this.isPlayerGhost = isPlayerGhost;
        this.isReferee = isReferee;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public int getAccessRights() {
        return accessRights;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public boolean isPlayerGhost() {
        return isPlayerGhost;
    }

    public boolean isReferee() {
        return isReferee;
    }
}
