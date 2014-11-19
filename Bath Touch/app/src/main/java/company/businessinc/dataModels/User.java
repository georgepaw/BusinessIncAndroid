package company.businessinc.dataModels;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gp on 18/11/14.
 */
public class User {
    private int userID;
    private Status status;

    public User(int userID, boolean status) {
        this.userID = userID;
        this.status = new Status(status);
    }

    public User(JSONObject jsonObject) throws JSONException{
        try {
            this.userID = jsonObject.getInt("userID");
        } catch(JSONException e){
            this.userID = -1;
        }
        this.status = new Status(jsonObject.getBoolean("status"));
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public boolean getStatus() {
        return status.getStatus();
    }

    public void setStatus(boolean status) {
        this.status =  new Status(status);
    }
}
