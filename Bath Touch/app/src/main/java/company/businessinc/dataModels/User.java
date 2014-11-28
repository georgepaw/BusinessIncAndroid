package company.businessinc.dataModels;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gp on 18/11/14.
 */
public class User {
    private static Integer userID = null;
    private static Boolean isLoggedIn = false;

    public User(Integer userID, boolean status) {
        this.userID = userID;
        this.isLoggedIn = status;
    }

    public User(JSONObject jsonObject) throws JSONException{
        try {
            this.userID = jsonObject.getInt("userID");
        } catch(JSONException e){
            this.userID = 2;
        }
        try {
            this.isLoggedIn = jsonObject.getBoolean("status");
        } catch (JSONException e){
            this.isLoggedIn = null;
        }
    }

    public static Integer getUserID() {
        return userID;
    }

    public static Boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String toString(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("status", isLoggedIn);
            if (isLoggedIn){
                jsonObject.put("userID", userID);
            }

        } catch (JSONException e){

        }
        return  jsonObject.toString();
    }
}
