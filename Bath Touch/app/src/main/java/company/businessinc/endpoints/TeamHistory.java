package company.businessinc.endpoints;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import company.businessinc.dataModels.Match;
import company.businessinc.networking.APICall;
import company.businessinc.networking.APICallType;

/**
 * Created by gp on 18/11/14.
 */
public class TeamHistory extends AsyncTask<Void, Void, List<Match>> {
    String TAG = "TeamHistory";
    private TeamHistoryInterface callback;
    private List<NameValuePair> parameters;

    public TeamHistory(TeamHistoryInterface callback, int leagueID, int teamID, Date dateFrom, Date dateTo) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("leagueID", Integer.toString(leagueID)));
        parameters.add(new BasicNameValuePair("teamID", Integer.toString(teamID)));
        //TODO how to pass date range as a parameter
    }

    @Override
    protected List<Match> doInBackground(Void... a) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(APICall.call(APICallType.TeamHistory, parameters));
        } catch (JSONException e) {
            Log.d(TAG, "Couldn't parse String into JSON");
        }
        //TODO: PARSE JSON

        List<Match> list = null;
        return list;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(List<Match> result) {
        callback.teamHistoryCallback(result);
    }
}