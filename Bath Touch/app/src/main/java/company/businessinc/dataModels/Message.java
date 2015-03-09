package company.businessinc.dataModels;

import android.content.ContentValues;
import android.database.Cursor;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Grzegorz on 09/03/2015.
 */
public class Message {

    public static enum SortType{ASCENDING, DESCENDING}

    private String teamName;
    private Integer teamID;
    private Boolean isMale;
    private Integer matchID;
    private Date dateTime;


    public static final String KEY_TEAMNAME = "teamName";
    public static final String KEY_TEAMID = "teamID";
    public static final String KEY_ISMALE = "isMale";
    public static final String KEY_MATCHID = "matchID";
    public static final String KEY_DATETIME = "dateTime";

    public static final String[] COLUMNS = {KEY_TEAMID, KEY_MATCHID, KEY_TEAMNAME, KEY_DATETIME, KEY_ISMALE};
    public static final String CREATE_TABLE =   KEY_TEAMID + "\tINTEGER,\n" +
            KEY_MATCHID + "\tINTEGER,\n" +
            KEY_TEAMNAME + "\tTEXT,\n" +
            KEY_DATETIME + "\tTEXT,\n" +
            KEY_ISMALE + "\tTEXT";

    public Message(String teamName, Integer teamID, Boolean isMale, Integer matchID, Date dateTime) {
        this.teamName = teamName;
        this.teamID = teamID;
        this.isMale = isMale;
        this.matchID = matchID;
        this.dateTime = dateTime;
    }

    public Message(Cursor cursor){
        try {
            this.teamName = cursor.getString(cursor.getColumnIndex(KEY_TEAMNAME));
        } catch(Exception e) {
            this.teamName = null;
        }
        try {
            this.teamID = cursor.getInt(cursor.getColumnIndex(KEY_TEAMID));
        } catch(Exception e) {
            this.teamID = null;
        }
        try {
            this.isMale = cursor.getInt(cursor.getColumnIndex(KEY_ISMALE)) == 1;
        } catch(Exception e) {
            this.isMale = null;
        }
        try {
            this.matchID = cursor.getInt(cursor.getColumnIndex(KEY_MATCHID));
        } catch(Exception e) {
            this.matchID = null;
        }
        try {
            String dT = cursor.getString(cursor.getColumnIndex(KEY_DATETIME));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
            this.dateTime = sdf.parse(dT);
        } catch(Exception e) {
            this.dateTime = null;
        }
    }

    public Message(JSONObject jsonObject){
        try {
            this.teamName = jsonObject.getString(KEY_TEAMNAME);
        } catch(Exception e) {
            this.teamName = null;
        }
        try {
            this.teamID = jsonObject.getInt(KEY_TEAMID);
        } catch(Exception e) {
            this.teamID = null;
        }
        try {
            this.isMale = jsonObject.getInt(KEY_ISMALE) == 1;
        } catch(Exception e) {
            this.isMale = null;
        }
        try {
            this.matchID = jsonObject.getInt(KEY_MATCHID);
        } catch(Exception e) {
            this.matchID = null;
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
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(KEY_MATCHID,matchID);
        values.put(KEY_TEAMID, teamID);
        values.put(KEY_TEAMNAME, teamName);
        values.put(KEY_ISMALE, isMale ? 1 : 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
        values.put(KEY_DATETIME, sdf.format(dateTime));
        return values;
    }

    public static List<Message> cursorToList(Cursor cursor){
        List<Message> output = new ArrayList<>();
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                output.add(new Message(cursor));
                cursor.moveToNext();
            }
        }
        return output;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getTeamID() {
        return teamID;
    }

    public void setTeamID(Integer teamID) {
        this.teamID = teamID;
    }

    public Boolean getIsMale() {
        return isMale;
    }

    public void setIsMale(Boolean isMale) {
        this.isMale = isMale;
    }

    public Integer getMatchID() {
        return matchID;
    }

    public void setMatchID(Integer matchID) {
        this.matchID = matchID;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public static List<Message> sortList(List<Message> data, final SortType sortType){
        Collections.sort(data, new Comparator<Message>() {
            public int compare(Message m1, Message m2) {
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
                return sortType == SortType.ASCENDING ? result : result * -1;
            }
        });
        return data;
    }

}
