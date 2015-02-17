package company.businessinc.endpoints;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.SQLiteManager;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.ResponseStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

import company.businessinc.dataModels.League;
import company.businessinc.dataModels.Player;
import company.businessinc.networking.APICall;
import company.businessinc.networking.APICallType;

/**
 * Created by gp on 18/11/14.
 */
public class TeamAvailability extends AsyncTask<Void, Void, ResponseStatus> {
    String TAG = "TeamAvailability";
    private TeamAvailabilityInterface callback;
    private List<NameValuePair> parameters;
    private int matchID;
    private Context context;

    public TeamAvailability(TeamAvailabilityInterface callback, Context context, int matchID) {
        this.callback = callback;
        parameters = new LinkedList<>();
        parameters.add(new BasicNameValuePair("matchID", Integer.toString(matchID)));
        this.matchID = matchID;
        this.context = context;
    }

    @Override
    protected ResponseStatus doInBackground(Void... a) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(APICall.call(APICallType.TeamAvailability, parameters));
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return new ResponseStatus(false);
        }

        LinkedList<ContentValues> cV = new LinkedList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try{
                ContentValues dis = new Player(jsonArray.getJSONObject(i)).toContentValues();
                dis.put(Match.KEY_MATCHID, matchID);
                cV.add(dis);
            } catch (Exception e){
                Log.d(TAG, "Couldn't parse JSON into League object");
                return new ResponseStatus(false);
            }
        }
        ContentValues[] contentValues = cV.toArray(new ContentValues[cV.size()]);
        SQLiteManager.getInstance(context).bulkInsert(DBProviderContract.MYTEAMPLAYERSAVAILABILITY_TABLE_NAME, contentValues);
        return new ResponseStatus(true);
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(ResponseStatus responseStatus) {
        callback.teamAvailabilityCallback(responseStatus, matchID);
    }
}
