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
    private static String name = null;
    private static String teamName = null;
    private static Integer teamID = null;

    public User() {
        userID = null;
        isLoggedIn = false;
        name = null;
        teamName = null;
        teamID = null;
    }

    public User(JSONObject jsonObject) throws JSONException{
        try {
            this.userID = jsonObject.getInt("userID");
        } catch(JSONException e){
            this.userID = -1;
        }
        try {
            this.isLoggedIn = jsonObject.getBoolean("status");
        } catch (JSONException e){
            this.isLoggedIn = null;
        }
        try {
            this.name = jsonObject.getString("name");
        } catch(JSONException e){
            this.name = null;
        }
        try {
            this.teamName = jsonObject.getString("teamName");
        } catch (JSONException e){
            this.teamName = null;
        }
        try {
            this.teamID = jsonObject.getInt("teamID");
        } catch (JSONException e){
            this.teamID = null;
        }
    }

    public static Integer getUserID() {
        return userID;
    }

    public static Boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String Name){
        name = Name;
    }

    public static String getTeamName() {
        return teamName;
    }

    public static Integer getTeamID() {
        return teamID;
    }

    public String toString(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("status", isLoggedIn);
            jsonObject.put("name", name);
            if (isLoggedIn){
                jsonObject.put("userID", userID);
                jsonObject.put("teamName", teamName);
                jsonObject.put("teamID", teamID);

            }

        } catch (JSONException e){

        }
        return  jsonObject.toString();
    }
}
