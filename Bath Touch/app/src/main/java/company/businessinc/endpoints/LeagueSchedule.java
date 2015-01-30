package company.businessinc.endpoints;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

import company.businessinc.dataModels.Match;
import company.businessinc.networking.APICall;
import company.businessinc.networking.APICallType;

/**
 * Created by gp on 18/11/14.
 */
public class LeagueSchedule extends AsyncTask<Void, Void, List<Match>> {
    String TAG = "LeagueSchedule";
    private LeagueScheduleInterface callback;
    private List<NameValuePair> parameters;
    private int leagueID;

    public LeagueSchedule(LeagueScheduleInterface callback, int leagueID) {
        this.callback = callback;
        this.leagueID = leagueID;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("leagueID", Integer.toString(leagueID)));
    }

    @Override
    protected List<Match> doInBackground(Void... a) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(APICall.call(APICallType.LeagueSchedule, parameters));
        } catch (JSONException e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return null;
        }
        List<Match> list = new LinkedList<Match>();
        for(int i = 0; i < jsonArray.length(); i++){
            try{
                list.add(new Match(jsonArray.getJSONObject(i)));
            } catch (JSONException e){
                Log.d(TAG, "Couldn't parse JSON into Match object");
                return null;
            }
        }
        return list;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(List<Match> result) {
        callback.leagueScheduleCallback(result, leagueID);
    }
}
