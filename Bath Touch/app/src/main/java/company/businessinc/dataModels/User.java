package company.businessinc.dataModels;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gp on 18/11/14.
 */
public class User {
    private Integer userID;
    private Status status;

    public User(Integer userID, boolean status) {
        this.userID = userID;
        this.status = new Status(status);
    }

    public User(JSONObject jsonObject) throws JSONException{
        try {
            this.userID = jsonObject.getInt("userID");
        } catch(JSONException e){
            this.userID = null;
        }
        try {
            this.status = new Status(jsonObject.getBoolean("status"));
        } catch (JSONException e){
            this.status = new Status();
        }
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Boolean getStatus() {
        return status.getStatus();
    }

    public void setStatus(Boolean status) {
        this.status =  new Status(status);
    }
}
