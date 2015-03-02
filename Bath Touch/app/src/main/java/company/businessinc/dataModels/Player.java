package company.businessinc.dataModels;

import android.content.ContentValues;
import android.database.Cursor;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Grzegorz on 02/02/2015.
 */
public class Player {
    private String name;
    private Integer userID;
    private Boolean isPlaying;
    private Boolean isGhostPlayer;
    private String email;
    private Boolean isMale;


    public static final String KEY_NAME = "name";
    public static final String KEY_USERID = "userID";
    public static final String KEY_ISPLAYING = "isPlaying";
    public static final String KEY_ISGHOSTPLAYER = "isGhostPlayer";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ISMALE = "isMale";
    public static final String[] COLUMNS = {KEY_NAME, KEY_USERID, KEY_ISPLAYING, KEY_ISGHOSTPLAYER,KEY_ISMALE};

    public static final String CREATE_TABLE =   KEY_USERID + "\tINTEGER,\n"+
                                                KEY_NAME + "\tTEXT,\n"+
                                                KEY_ISPLAYING + "\tINTEGER,\n"+
                                                KEY_ISGHOSTPLAYER + "\tINTEGER,\n"+
                                                KEY_EMAIL + "\tTEXT,\n"+
                                                KEY_ISMALE + "\tINTEGER";

    public Player(String name, Integer userID, Boolean isPlaying, Boolean isGhostPlayer, String email) {
        this.name = name;
        this.userID = userID;
        this.isPlaying = isPlaying;
        this.isGhostPlayer = isGhostPlayer;
        this.email = email;
    }

    public Player(Cursor cursor){
        try {
            this.name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
        } catch(Exception e) {
            this.name = null;
        }
        try {
            this.userID = cursor.getInt(cursor.getColumnIndex(KEY_USERID));
        } catch(Exception e) {
            this.userID = null;
        }
        try {
            this.isPlaying = cursor.getInt(cursor.getColumnIndex(KEY_ISPLAYING)) == 1;
        } catch(Exception e) {
            this.isPlaying = null;
        }
        try {
            this.isGhostPlayer = cursor.getInt(cursor.getColumnIndex(KEY_ISGHOSTPLAYER)) == 1;
        } catch(Exception e) {
            this.isGhostPlayer = null;
        }
        try {
            this.email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL));
        } catch(Exception e) {
            this.email = null;
        }


        try {
            this.isMale = cursor.getInt(cursor.getColumnIndex(KEY_ISMALE)) == 1;
        } catch(Exception e) {
            this.isMale = null;
        }
    }

    public Player(JSONObject jsonObject) throws JSONException {
        try {
            this.name = jsonObject.getString(KEY_NAME);
        } catch(JSONException e) {
            this.name = null;
        }
        try {
            this.userID = jsonObject.getInt(KEY_USERID);
        } catch(JSONException e) {
            this.userID = null;
        }
        try {
            this.isPlaying = jsonObject.getBoolean(KEY_ISPLAYING);
        } catch(JSONException e) {
            this.isPlaying = null;
        }
        try {
            this.isGhostPlayer = jsonObject.getBoolean(KEY_ISGHOSTPLAYER);
        } catch(JSONException e) {
            this.isGhostPlayer = null;
        }
        try {
            this.email = jsonObject.getString(KEY_EMAIL);
        } catch(JSONException e) {
            this.email = null;
        }

        try {
            this.isMale = jsonObject.getInt("gender") == 0;
        } catch(JSONException e) {
            this.isMale = null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Boolean getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(Boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public Boolean getIsGhostPlayer() {
        return isGhostPlayer;
    }

    public void setIsGhostPlayer(Boolean isGhostPlayer) {
        this.isGhostPlayer = isGhostPlayer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsMale() {
        return isMale;
    }

    public void setIsMale(Boolean isMale) {
        this.isMale = isMale;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_USERID, userID);
        values.put(KEY_ISPLAYING, isPlaying ? 1 : 0);
        values.put(KEY_ISGHOSTPLAYER, isGhostPlayer ? 1 : 0);
        values.put(KEY_EMAIL, email);
        values.put(KEY_ISMALE, isMale ? 1 : 0);
        return values;
    }

    public static List<Player> cursorToList(Cursor cursor){
        List<Player> output = new ArrayList<>();
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                output.add(new Player(cursor));
                cursor.moveToNext();
            }
        }
        return output;
    }

    public static List<Player> sortByName(List<Player> data){
        Collections.sort(data, new Comparator<Player>() {
            public int compare(Player p1, Player p2) {
                if (p1 == null && p2 == null) {
                    return 0;
                } else if (p1 == null) {
                    return 1;
                } else if (p2 == null) {
                    return -1;
                }
                return p1.getName().compareToIgnoreCase(p2.getName()) ;
            }
        });
        return data;
    }

    public static int getGenderCount(List<Player> data, boolean isMale){
        int i = 0;
        for(Player p : data){
            if(p.getIsMale() == isMale){
                i++;
            }
        }
        return i;
    }
}
