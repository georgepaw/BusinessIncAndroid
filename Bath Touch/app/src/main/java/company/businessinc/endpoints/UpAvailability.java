package company.businessinc.endpoints;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import company.businessinc.dataModels.Status;
import company.businessinc.dataModels.User;
import company.businessinc.networking.APICall;
import company.businessinc.networking.APICallType;

/**
 * Created by gp on 18/11/14.
 */
public class UpAvailability extends AsyncTask<Void, Void, Status> {
    String TAG = "UpAvailability";
    private UpAvailabilityInterface callback;
    private List<NameValuePair> parameters;
    private int matchID;
    private int userID = -1;
    private boolean isPlaying = false;
    public enum CallType{GETMYAVAILABILITY, SETMYAVAILABILITY, SETPLAYERSAVAILABILITY}
    private CallType callType;

    public UpAvailability(UpAvailabilityInterface callback, int isPlaying, int matchID, int userID) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("isPlaying", Integer.toString(isPlaying)));
        parameters.add(new BasicNameValuePair("matchID", Integer.toString(matchID)));
        parameters.add(new BasicNameValuePair("userID", Integer.toString(userID)));
        callType = CallType.SETPLAYERSAVAILABILITY;
        this.matchID = matchID;
        this.userID = userID;
        this.isPlaying = isPlaying == 1;
    }

    public UpAvailability(UpAvailabilityInterface callback, int isPlaying, int matchID) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("isPlaying", Integer.toString(isPlaying)));
        parameters.add(new BasicNameValuePair("matchID", Integer.toString(matchID)));
        callType = CallType.SETMYAVAILABILITY;
        this.matchID = matchID;
        this.isPlaying = isPlaying == 1;
    }

    public UpAvailability(UpAvailabilityInterface callback, int matchID) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("matchID", Integer.toString(matchID)));
        callType = CallType.GETMYAVAILABILITY;
        this.matchID = matchID;
    }

    @Override
    protected company.businessinc.dataModels.Status doInBackground(Void... a) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(APICall.call(APICallType.UpAvailability, parameters));
        } catch (JSONException e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return null;
        }
        company.businessinc.dataModels.Status status = null;
        try {
            status = new company.businessinc.dataModels.Status(jsonObject);
            if(callType == CallType.GETMYAVAILABILITY){
                this.isPlaying = status.getStatus();
            }
        } catch (JSONException e) {
            Log.d(TAG, "Couldn't parse parameters");
        }
        return status;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(company.businessinc.dataModels.Status result) {
        callback.upAvailabilityCallback(isPlaying, callType, matchID, userID);
    }
}
