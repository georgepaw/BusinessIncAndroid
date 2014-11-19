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

    public LeagueView(LeagueViewInterface callback, int leagueID) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("leagueID", Integer.toString(leagueID)));
    }

    @Override
    protected List<LeagueTeam> doInBackground(Void... a) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(APICall.call(APICallType.LeagueView, parameters));
        } catch (JSONException e) {
            Log.d(TAG, "Couldn't parse String into JSON");
        }
        //TODO: PARSE JSON

        List<LeagueTeam> list = null;
        return list;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(List<LeagueTeam> result) {
        callback.leagueViewCallback(result);
    }
}
