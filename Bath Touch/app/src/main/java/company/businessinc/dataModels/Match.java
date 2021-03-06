package company.businessinc.dataModels;

import android.content.ContentValues;
import android.database.Cursor;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by gp on 18/11/14.
 */
public class Match {

    public static enum SortType{ASCENDING, DESCENDING}

    private Integer leagueID;
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
    private Float longitude;
    private Float latitude;
    private String postCode;
    private String address;

    public static final String KEY_LEAGUEID = "leagueID";
    public static final String KEY_MATCHID = "matchID";
    public static final String KEY_TEAMONEID = "teamOneID";
    public static final String KEY_TEAMTWOID = "teamTwoID";
    public static final String KEY_TEAMONE = "teamOne";
    public static final String KEY_TEAMTWO = "teamTwo";
    public static final String KEY_REFID = "refID";
    public static final String KEY_REFNAME = "refName";
    public static final String KEY_DATETIME = "dateTime";
    public static final String KEY_PLACE = "place";
    public static final String KEY_TEAMONEPOINTS = "teamOnePoints";
    public static final String KEY_TEAMTWOPOINTS = "teamTwoPoints";
    public static final String KEY_ISFORFEIT = "isForfeit";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_POSTCODE = "postcode";
    public static final String KEY_ADDRESS = "address";
    public static final String[] COLUMNS = {KEY_LEAGUEID, KEY_MATCHID,KEY_TEAMONEID, KEY_TEAMTWOID, KEY_TEAMONE, KEY_TEAMTWO,
                                            KEY_REFID, KEY_REFNAME, KEY_DATETIME, KEY_PLACE, KEY_TEAMONEPOINTS, KEY_TEAMTWOPOINTS,
                                            KEY_ISFORFEIT, KEY_LONGITUDE, KEY_LATITUDE, KEY_POSTCODE, KEY_ADDRESS};
    public static final String CREATE_TABLE =   KEY_LEAGUEID + "\tINTEGER,\n" +
                                                KEY_MATCHID + "\tINTEGER,\n" +
                                                KEY_TEAMONEID + "\tINTEGER,\n" +
                                                KEY_TEAMTWOID + "\tINTEGER,\n" +
                                                KEY_TEAMONE + "\tTEXT,\n" +
                                                KEY_TEAMTWO + "\tTEXT,\n" +
                                                KEY_REFID + "\tINTEGER,\n" +
                                                KEY_LONGITUDE + "\tREAL,\n" +
                                                KEY_LATITUDE + "\tREAL,\n" +
                                                KEY_POSTCODE + "\tTEXT,\n" +
                                                KEY_ADDRESS + "\tTEXT,\n" +
                                                KEY_REFNAME + "\tTEXT,\n" +
                                                KEY_DATETIME + "\tTEXT,\n" +
                                                KEY_PLACE + "\tTEXT,\n" +
                                                KEY_TEAMONEPOINTS + "\tINTEGER,\n" +
                                                KEY_TEAMTWOPOINTS + "\tINTEGER,\n" +
                                                KEY_ISFORFEIT + "\tINTEGER";

    public Match(Integer leagueID, Integer matchID, Integer teamOneID, Integer teamTwoID, String teamOne, String teamTwo, Integer refID, String refName, Date dateTime, String place, Integer teamOnePoints, Integer teamTwoPoints, Boolean isForfeit, Float longitude, Float latitude, String postCode, String address) {
        this.leagueID = leagueID;
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
        this.longitude = longitude;
        this.latitude = latitude;
        this.postCode = postCode;
        this.address = address;
    }

    public Match(Cursor cursor){
        try {
            this.leagueID = cursor.getInt(cursor.getColumnIndex(KEY_LEAGUEID));
        } catch(Exception e) {
            this.leagueID = null;
        }
        try {
            this.matchID = cursor.getInt(cursor.getColumnIndex(KEY_MATCHID));
        } catch(Exception e) {
            this.matchID = null;
        }
        try {
            this.teamOneID = cursor.getInt(cursor.getColumnIndex(KEY_TEAMONEID));
        } catch(Exception e) {
            this.teamOneID = null;
        }
        try {
            this.teamTwoID = cursor.getInt(cursor.getColumnIndex(KEY_TEAMTWOID));
        } catch(Exception e) {
            this.teamTwoID = null;
        }
        try {
            this.teamOne = cursor.getString(cursor.getColumnIndex(KEY_TEAMONE));
        } catch(Exception e) {
            this.teamOne = null;
        }
        try {
            this.teamTwo = cursor.getString(cursor.getColumnIndex(KEY_TEAMTWO));
        } catch(Exception e) {
            this.teamTwo = null;
        }
        try {
            this.refID = cursor.getInt(cursor.getColumnIndex(KEY_REFID));
        } catch(Exception e) {
            this.refID = null;
        }
        try {
            this.refName = cursor.getString(cursor.getColumnIndex(KEY_REFNAME));
        } catch(Exception e) {
            this.refName = null;
        }
        try {
            this.longitude = cursor.getFloat(cursor.getColumnIndex(KEY_LONGITUDE));
        } catch(Exception e) {
            this.longitude = null;
        }
        try {
            this.latitude = cursor.getFloat(cursor.getColumnIndex(KEY_LATITUDE));
        } catch(Exception e) {
            this.latitude = null;
        }
        try {
            this.address = cursor.getString(cursor.getColumnIndex(KEY_ADDRESS));
        } catch(Exception e) {
            this.address = null;
        }
        try {
            this.postCode = cursor.getString(cursor.getColumnIndex(KEY_POSTCODE));
        } catch(Exception e) {
            this.postCode = null;
        }
        try {
            String dT = cursor.getString(cursor.getColumnIndex(KEY_DATETIME));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
            this.dateTime = sdf.parse(dT);
        } catch(Exception e) {
            this.dateTime = null;
        }
        try {
            this.place = cursor.getString(cursor.getColumnIndex(KEY_PLACE));
        } catch(Exception e) {
            this.place = null;
        }
        try {
            this.teamOnePoints = cursor.getInt(cursor.getColumnIndex(KEY_TEAMONEPOINTS));
        } catch(Exception e) {
            this.teamOnePoints = null;
        }
        try {
            this.teamTwoPoints = cursor.getInt(cursor.getColumnIndex(KEY_TEAMTWOPOINTS));
        } catch(Exception e) {
            this.teamTwoPoints = null;
        }
        try {
            this.isForfeit = (cursor.getInt(cursor.getColumnIndex(KEY_ISFORFEIT)) == 1); //Convert int to boolean
        } catch(Exception e) {
            this.isForfeit = false;
        }
    }

    public Match(Integer leagueID, JSONObject jsonObject) throws JSONException {
        this.leagueID = leagueID;
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
            this.teamOne = jsonObject.getString("teamOneName");
        } catch(JSONException e) {
            this.teamOne = null;
        }
        try {
            this.teamTwo = jsonObject.getString("teamTwoName");
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
            this.longitude = (float)jsonObject.getDouble(KEY_LONGITUDE);
        } catch(Exception e) {
            this.longitude = null;
        }
        try {
            this.latitude = (float)jsonObject.getDouble(KEY_LATITUDE);
        } catch(Exception e) {
            this.latitude = null;
        }
        try {
            this.address = jsonObject.getString(KEY_ADDRESS);
        } catch(Exception e) {
            this.address = null;
        }
        try {
            this.postCode = jsonObject.getString(KEY_POSTCODE);
        } catch(Exception e) {
            this.postCode = null;
        }
        try {
            String dT = jsonObject.getString("dateTime");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
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
            this.isForfeit = false;
        }
    }

    public Integer getLeagueID() {
        return leagueID;
    }

    public void setLeagueID(Integer leagueID) {
        this.leagueID = leagueID;
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

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(KEY_LEAGUEID,leagueID);
        values.put(KEY_MATCHID,matchID);
        values.put(KEY_TEAMONEID, teamOneID);
        values.put(KEY_TEAMTWOID, teamTwoID);
        values.put(KEY_TEAMONE, teamOne);
        values.put(KEY_TEAMTWO, teamTwo);
        values.put(KEY_REFID, refID);
        values.put(KEY_REFNAME, refName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
        values.put(KEY_DATETIME, sdf.format(dateTime));
        values.put(KEY_PLACE, place);
        values.put(KEY_TEAMONEPOINTS, teamOnePoints);
        values.put(KEY_TEAMTWOPOINTS, teamTwoPoints);
        values.put(KEY_ISFORFEIT, isForfeit ? 1 : 0); //convert boolean to int
        values.put(KEY_LONGITUDE, longitude);
        values.put(KEY_LATITUDE, latitude);
        values.put(KEY_POSTCODE, postCode);
        values.put(KEY_ADDRESS, address);
        return values;
    }

    public static List<Match> sortList(List<Match> data, final SortType sortType){
        Collections.sort(data, new Comparator<Match>() {
            public int compare(Match m1, Match m2) {
                if (m1 == null && m2 == null) {
                    return 0;
                } else if (m1 == null) {
                    return 1;
                } else if (m2 == null) {
                    return -1;
                } else if (m1.getDateTime() == null && m2.getDateTime() == null) {
                    return 0;
                } else if (m1.getDateTime() == null) {
                    return 1;
                } else if (m2.getDateTime() == null) {
                    return -1;
                }
                int result = m1.getDateTime().compareTo(m2.getDateTime());
                return sortType == SortType.ASCENDING ? result : result*-1 ;
            }
        });
        return data;
    }

    public static List<Match> cursorToList(Cursor cursor){
        List<Match> output = new ArrayList<>();
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                output.add(new Match(cursor));
                cursor.moveToNext();
            }
        }
        return output;
    }
}
