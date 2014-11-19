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
    private int refID;
    private String refName;
    private Date dateTime;
    private String place;
    private Integer teamOnePoints;
    private Integer teamTwoPoints;

    public Match(String teamOne, String teamTwo, int refID, String refName, Date dateTime, String place, Integer teamOnePoints, Integer teamTwoPoints) {
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
        this.teamOne = jsonObject.getString("teamOne");
        this.teamTwo = jsonObject.getString("teamTwo");
        this.refID = jsonObject.getInt("refID");
        this.refName = jsonObject.getString("refName");
        String dT = jsonObject.getString("date");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
        try {
            this.dateTime = sdf.parse(dT);
        } catch (ParseException e){
            throw new JSONException(dT);
        }
        this.place = jsonObject.getString("place");
        this.teamOnePoints = jsonObject.getInt("teamOnePoints");
        this.teamTwoPoints = jsonObject.getInt("teamTwoPoints");
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

    public int getrefID() {
        return refID;
    }

    public void setrefID(int refID) {
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
