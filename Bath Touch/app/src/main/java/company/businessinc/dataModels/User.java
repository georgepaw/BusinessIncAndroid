package company.businessinc.dataModels;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gp on 18/11/14.
 */
public class User {
    private Integer userID;
    private Boolean status;

    public User(Integer userID, boolean status) {
        this.userID = userID;
        this.status = status;
    }

    public User(JSONObject jsonObject) throws JSONException{
        try {
            this.userID = jsonObject.getInt("userID");
        } catch(JSONException e){
            this.userID = 2;
        }
        try {
            this.status = jsonObject.getBoolean("status");
        } catch (JSONException e){
            this.status = null;
        }
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status =  status;
    }

    public String toString(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("status", status);
            if (status){
                jsonObject.put("userID", userID);
            }

        } catch (JSONException e){

        }
        return  jsonObject.toString();
    }
}
