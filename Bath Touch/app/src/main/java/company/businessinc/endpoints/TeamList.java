package company.businessinc.endpoints;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.Team;
import company.businessinc.dataModels.Team;
import company.businessinc.networking.APICall;
import company.businessinc.networking.APICallType;

/**
 * Created by gp on 18/11/14.
 */
public class TeamList extends AsyncTask<Void, Void, List<Team>> {
    String TAG = "TeamList";
    private TeamListInterface callback;
    private List<NameValuePair> parameters;
    public enum CallType{GETALLLTEAMS, GETLEAGUETEAMS}
    private CallType callType;
    private int leagueID;

    public TeamList(TeamListInterface callback) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        callType = CallType.GETALLLTEAMS;
    }

    public TeamList(TeamListInterface callback, int leagueID) {
        this.callback = callback;
        this.leagueID = leagueID;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("leagueID", Integer.toString(leagueID)));
        callType = CallType.GETLEAGUETEAMS;
    }

    @Override
    protected List<Team> doInBackground(Void... a) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(APICall.call(APICallType.TeamList, parameters));
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return null;
        }

        List<Team> list = new LinkedList<Team>();
        for(int i = 0; i < jsonArray.length(); i++){
            try{
                list.add(new Team(jsonArray.getJSONObject(i)));
            } catch (Exception e){
                Log.d(TAG, "Couldn't parse JSON into Match object");
                return null;
            }
        }
        return list;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(List<Team> result) {
        callback.teamListCallback(result, callType, leagueID);
    }
}