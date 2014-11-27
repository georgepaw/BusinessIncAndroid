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
    private Integer matchID;
    private Integer teamOneID;
    private Integer teamTwoID;
    private String teamOne;
    private String teamTwo;
    private Integer refID;
    private String refName;
    private Date dateTime;
    private String place;
    private Integer teamOnePoints;
    private Integer teamTwoPoints;
    private Boolean isForfeit;

    public Match(Integer matchID, Integer teamOneID, Integer teamTwoID, String teamOne, String teamTwo, Integer refID, String refName, Date dateTime, String place, Integer teamOnePoints, Integer teamTwoPoints, Boolean isForfeit) {
        this.matchID = matchID;
        this.teamOneID = teamOneID;
        this.teamTwoID = teamTwoID;
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        this.refID = refID;
        this.refName = refName;
        this.dateTime = dateTime;
        this.place = place;
        this.teamOnePoints = teamOnePoints;
        this.teamTwoPoints = teamTwoPoints;
        this.isForfeit = isForfeit;
    }

    public Match(JSONObject jsonObject) throws JSONException {
        try {
            this.matchID = jsonObject.getInt("matchID");
        } catch(JSONException e) {
            this.matchID = null;
        }
        try {
            this.teamOneID = jsonObject.getInt("teamOneID");
        } catch(JSONException e) {
            this.teamOneID = null;
        }
        try {
            this.teamTwoID = jsonObject.getInt("teamTwoID");
        } catch(JSONException e) {
            this.teamTwoID = null;
        }
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.UK);
            this.dateTime = sdf.parse(dT);
        } catch(JSONException e) {
            this.dateTime = null;
        } catch (ParseException e){
            this.dateTime = null;
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
        try {
            this.isForfeit = jsonObject.getBoolean("isForfeit");
        } catch(JSONException e) {
            this.isForfeit = null;
        }
    }

    public Integer getMatchID() {
        return matchID;
    }

    public void setMatchID(Integer matchID) {
        this.matchID = matchID;
    }

    public Integer getTeamOneID() {
        return teamOneID;
    }

    public void setTeamOneID(Integer teamOneID) {
        this.teamOneID = teamOneID;
    }

    public Integer getTeamTwoID() {
        return teamTwoID;
    }

    public void setTeamTwoID(Integer teamTwoID) {
        this.teamTwoID = teamTwoID;
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

    public Boolean getIsForfeit() {
        return isForfeit;
    }

    public void setIsForfeit(Boolean isForfeit) {
        this.isForfeit = isForfeit;
    }
}
