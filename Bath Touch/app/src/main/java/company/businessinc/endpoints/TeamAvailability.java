package company.businessinc.endpoints;

import android.os.AsyncTask;
import android.util.Log;

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
public class TeamAvailability extends AsyncTask<Void, Void, List<Player>> {
    String TAG = "TeamAvailability";
    private TeamAvailabilityInterface callback;
    private List<NameValuePair> parameters;
    private int matchID;

    public TeamAvailability(TeamAvailabilityInterface callback, int matchID) {
        this.callback = callback;
        parameters = new LinkedList<>();
        parameters.add(new BasicNameValuePair("matchID", Integer.toString(matchID)));
        this.matchID = matchID;
    }

    @Override
    protected List<Player> doInBackground(Void... a) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(APICall.call(APICallType.TeamAvailability, parameters));
        } catch (JSONException e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return null;
        }

        List<Player> list = new LinkedList<Player>();
        for(int i = 0; i < jsonArray.length(); i++){
            try{
                list.add(new Player(jsonArray.getJSONObject(i)));
            } catch (JSONException e){
                Log.d(TAG, "Couldn't parse JSON into League object");
                return null;
            }
        }
        return list;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(List<Player> result) {
        callback.teamAvailabilityCallback(result, matchID);
    }
}
