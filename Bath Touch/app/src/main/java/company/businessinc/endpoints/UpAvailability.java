package company.businessinc.endpoints;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.SQLiteManager;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.Player;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.networking.APICall;
import company.businessinc.networking.APICallType;

/**
 * Created by gp on 18/11/14.
 */
public class UpAvailability extends AsyncTask<Void, Void, ResponseStatus> {
    String TAG = "UpAvailability";
    private UpAvailabilityInterface callback;
    private List<NameValuePair> parameters;
    private int matchID;
    private int userID = -1;
    private boolean isPlaying = false;
    public enum CallType{GETMYAVAILABILITY, SETMYAVAILABILITY, SETPLAYERSAVAILABILITY}
    private CallType callType;
    private Context context;

    public UpAvailability(UpAvailabilityInterface callback, Context context, int isPlaying, int matchID, int userID) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("isPlaying", Integer.toString(isPlaying)));
        parameters.add(new BasicNameValuePair("matchID", Integer.toString(matchID)));
        parameters.add(new BasicNameValuePair("userID", Integer.toString(userID)));
        callType = CallType.SETPLAYERSAVAILABILITY;
        this.matchID = matchID;
        this.userID = userID;
        this.isPlaying = isPlaying == 1;
        this.context = context;
    }

    public UpAvailability(UpAvailabilityInterface callback, Context context, int isPlaying, int matchID) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("isPlaying", Integer.toString(isPlaying)));
        parameters.add(new BasicNameValuePair("matchID", Integer.toString(matchID)));
        callType = CallType.SETMYAVAILABILITY;
        this.matchID = matchID;
        this.isPlaying = isPlaying == 1;
        this.context = context;
    }

    public UpAvailability(UpAvailabilityInterface callback, Context context, int matchID) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("matchID", Integer.toString(matchID)));
        callType = CallType.GETMYAVAILABILITY;
        this.matchID = matchID;
        this.context = context;
    }

    @Override
    protected ResponseStatus doInBackground(Void... a) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(APICall.call(APICallType.UpAvailability, parameters));
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return new ResponseStatus(false);
        }
        ResponseStatus responseStatus = new ResponseStatus(false);
        try {
            if(callType == CallType.GETMYAVAILABILITY){
                this.isPlaying = jsonObject.getBoolean("isPlaying");
                responseStatus = new ResponseStatus(true);
            }else {
                responseStatus = new ResponseStatus(jsonObject);
            }
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse parameters");
        }
        if(responseStatus.getStatus()) {
            ContentValues cv;
            switch (callType) {
                case GETMYAVAILABILITY:
                    cv = new ContentValues();
                    cv.put(Match.KEY_MATCHID, matchID);
                    cv.put(Player.KEY_ISPLAYING, isPlaying ? 1 : 0);
                    SQLiteManager.getInstance(context).insert(DBProviderContract.MYUPCOMINGGAMESAVAILABILITY_TABLE_NAME, cv);
                    break;
                case SETMYAVAILABILITY:
                    cv = new ContentValues();
                    cv.put(Match.KEY_MATCHID, matchID);
                    cv.put(Player.KEY_ISPLAYING, isPlaying ? 1 : 0);
                    SQLiteManager.getInstance(context).update(DBProviderContract.MYUPCOMINGGAMESAVAILABILITY_TABLE_NAME, cv, DBProviderContract.SELECTION_MATCHID, new String[]{Integer.toString(matchID)});
                    break;
                case SETPLAYERSAVAILABILITY:
                    cv = new ContentValues();
                    cv.put(Match.KEY_MATCHID, matchID);
                    cv.put(Player.KEY_ISPLAYING, isPlaying ? 1 : 0);
                    SQLiteManager.getInstance(context).update(DBProviderContract.MYTEAMPLAYERSAVAILABILITY_TABLE_NAME, cv, DBProviderContract.SELECTION_MATCHIDANDUSERID, new String[]{Integer.toString(matchID), Integer.toString(userID)});
                    break;
            }
        }
        return responseStatus;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(ResponseStatus responseStatus) {
        callback.upAvailabilityCallback(responseStatus,isPlaying, callType, matchID, userID);
    }
}
