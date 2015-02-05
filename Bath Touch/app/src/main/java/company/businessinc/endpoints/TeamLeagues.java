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
import company.businessinc.networking.APICall;
import company.businessinc.networking.APICallType;

/**
 * Created by gp on 18/11/14.
 */
public class TeamLeagues extends AsyncTask<Void, Void, List<League>> {
    String TAG = "TeamLeagues";
    private TeamLeaguesInterface callback;
    private List<NameValuePair> parameters;

    public TeamLeagues(TeamLeaguesInterface callback, int teamID) {
        this.callback = callback;
        parameters = new LinkedList<>();
        parameters.add(new BasicNameValuePair("teamID", Integer.toString(teamID)));
    }

    @Override
    protected List<League> doInBackground(Void... a) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(APICall.call(APICallType.LeagueList, parameters));
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return null;
        }

        List<League> list = new LinkedList<League>();
        for(int i = 0; i < jsonArray.length(); i++){
            try{
                list.add(new League(jsonArray.getJSONObject(i)));
            } catch (Exception e){
                Log.d(TAG, "Couldn't parse JSON into League object");
                return null;
            }
        }
        return list;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(List<League> result) {
        callback.teamLeaguesCallback(result);
    }
}
