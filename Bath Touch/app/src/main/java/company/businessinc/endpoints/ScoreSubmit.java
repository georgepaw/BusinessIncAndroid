package company.businessinc.endpoints;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.networking.APICall;
import company.businessinc.networking.APICallType;

/**
 * Created by gp on 18/11/14.
 */
public class ScoreSubmit extends AsyncTask<Void, Void, ResponseStatus> {
    String TAG = "ScoreSubmit";
    private ScoreSubmitInterface callback;
    private List<NameValuePair> parameters;

    public ScoreSubmit(ScoreSubmitInterface callback, int matchID, int teamOneScore, int teamTwoScore, boolean isForfeit) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("matchID", Integer.toString(matchID)));
        parameters.add(new BasicNameValuePair("teamOneScore", Integer.toString(teamOneScore)));
        parameters.add(new BasicNameValuePair("teamTwoScore", Integer.toString(teamTwoScore)));
        parameters.add(new BasicNameValuePair("isForfeit", Boolean.toString(isForfeit)));
    }

    @Override
    protected ResponseStatus doInBackground(Void... a) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(APICall.call(APICallType.ScoreSubmit, parameters));
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return new ResponseStatus(false);
        }
        ResponseStatus bool = new ResponseStatus(false);
        try{
            bool = new ResponseStatus(jsonObject);
        } catch (Exception e){
            Log.d(TAG, "Couldn't parse JSON into Status");
        }
        return bool;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(ResponseStatus result) {
        callback.scoreSubmitCallback(result);
    }
}
