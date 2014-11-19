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

import company.businessinc.networking.APICall;
import company.businessinc.networking.APICallType;

/**
 * Created by gp on 18/11/14.
 */
public class ScoreSubmit extends AsyncTask<Void, Void, Boolean> {
    String TAG = "ScoreSubmit";
    private ScoreSubmitInterface callback;
    private List<NameValuePair> parameters;

    public ScoreSubmit(ScoreSubmitInterface callback, int matchID, int teamOneScore, int teamTwoScore) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("matchID", Integer.toString(matchID)));
        parameters.add(new BasicNameValuePair("teamOneScore", Integer.toString(teamOneScore)));
        parameters.add(new BasicNameValuePair("teamTwoScore", Integer.toString(teamTwoScore)));
    }

    @Override
    protected Boolean doInBackground(Void... a) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(APICall.call(APICallType.ScoreSubmit, parameters));
        } catch (JSONException e) {
            Log.d(TAG, "Couldn't parse String into JSON");
        }
        //TODO: PARSE JSON

        Boolean bool = null;
        return bool;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(Boolean result) {
        callback.scoreSubmitCallback(result);
    }
}
