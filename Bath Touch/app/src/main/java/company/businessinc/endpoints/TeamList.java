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

    public TeamList(TeamListInterface callback) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
    }

    @Override
    protected List<Team> doInBackground(Void... a) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(APICall.call(APICallType.TeamList, parameters));
        } catch (JSONException e) {
            Log.d(TAG, "Couldn't parse String into JSON");
        }
        //TODO: PARSE JSON

        List<Team> list = null;
        return list;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(List<Team> result) {
        callback.teamListCallback(result);
    }
}