package company.businessinc.dataModels;

import android.text.format.DateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by gp on 18/11/14.
 */
public class Match {
    private String teamOne;
    private String teamTwo;
    private Integer refID;
    private String refName;
    private Date dateTime;
    private String place;
    private Integer teamOnePoints;
    private Integer teamTwoPoints;

    public Match(String teamOne, String teamTwo, Integer refID, String refName, Date dateTime, String place, Integer teamOnePoints, Integer teamTwoPoints) {
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        this.refID = refID;
        this.refName = refName;
        this.dateTime = dateTime;
        this.place = place;
        this.teamOnePoints = teamOnePoints;
        this.teamTwoPoints = teamTwoPoints;
    }

    public Match(JSONObject jsonObject) throws JSONException {
        try {
            this.teamOne = jsonObject.getString("teamOne");
        } catch(JSONException e) {
            this.teamOne = null;
        }
        try {
            this.teamTwo = jsonObject.getString("teamTwo");
        } catch(JSONException e) {
            this.teamTwo = null;
        }
        try {
            this.refID = jsonObject.getInt("refID");
        } catch(JSONException e) {
            this.refID = null;
        }
        try {
            this.refName = jsonObject.getString("refName");
        } catch(JSONException e) {
            this.refName = null;
        }
        try {
            String dT = jsonObject.getString("dateTime");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
            this.dateTime = sdf.parse(dT);
        } catch(JSONException e) {
            this.refName = null;
        } catch (ParseException e){
            throw new JSONException("couldn't parse dateTime");
        }
        try {
            this.place = jsonObject.getString("place");
        } catch(JSONException e) {
            this.place = null;
        }
        try {
            this.teamOnePoints = jsonObject.getInt("teamOnePoints");
        } catch(JSONException e) {
            this.teamOnePoints = null;
        }
        try {
            this.teamTwoPoints = jsonObject.getInt("teamTwoPoints");
        } catch(JSONException e) {
            this.teamTwoPoints = null;
        }
    }

    public String getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(String teamOne) {
        this.teamOne = teamOne;
    }

    public String getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(String teamTwo) {
        this.teamTwo = teamTwo;
    }

    public Integer getRefID() {
        return refID;
    }

    public void setRefID(Integer refID) {
        this.refID = refID;
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getTeamOnePoints() {
        return teamOnePoints;
    }

    public void setTeamOnePoints(Integer teamOnePoints) {
        this.teamOnePoints = teamOnePoints;
    }

    public Integer getTeamTwoPoints() {
        return teamTwoPoints;
    }

    public void setTeamTwoPoints(Integer teamTwoPoints) {
        this.teamTwoPoints = teamTwoPoints;
    }
}
