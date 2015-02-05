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

import company.businessinc.dataModels.League;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Match;
import company.businessinc.networking.APICall;
import company.businessinc.networking.APICallType;

/**
 * Created by gp on 18/11/14.
 */
public class LeagueView extends AsyncTask<Void, Void, List<LeagueTeam>> {
    String TAG = "LeagueView";
    private LeagueViewInterface callback;
    private List<NameValuePair> parameters;
    private int leagueID;

    public LeagueView(LeagueViewInterface callback, int leagueID) {
        this.callback = callback;
        this.leagueID = leagueID;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("leagueID", Integer.toString(leagueID)));
    }

    @Override
    protected List<LeagueTeam> doInBackground(Void... a) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(APICall.call(APICallType.LeagueView, parameters));
        } catch (Exception e) {
            Log.e(TAG, "Couldn't parse String into JSON");
            return null;
        }
        List<LeagueTeam> list = new LinkedList<LeagueTeam>();
        for(int i = 0; i < jsonArray.length(); i++){
            try{
                list.add(new LeagueTeam(jsonArray.getJSONObject(i)));
            } catch (Exception e){
                Log.e(TAG, "Couldn't parse JSON into LeagueTeam object");
                return null;
            }
        }
        return list;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(List<LeagueTeam> result) {
        callback.leagueViewCallback(result, leagueID);
    }
}
