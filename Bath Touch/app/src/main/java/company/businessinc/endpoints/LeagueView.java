package company.businessinc.endpoints;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.SQLiteManager;
import company.businessinc.dataModels.ResponseStatus;
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
public class LeagueView extends AsyncTask<Void, Void, ResponseStatus> {
    String TAG = "LeagueView";
    private LeagueViewInterface callback;
    private List<NameValuePair> parameters;
    private int leagueID;
    private Context context;

    public LeagueView(LeagueViewInterface callback, Context context, int leagueID) {
        this.callback = callback;
        this.leagueID = leagueID;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("leagueID", Integer.toString(leagueID)));
        this.context = context;
    }

    @Override
    protected ResponseStatus doInBackground(Void... a) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(APICall.call(APICallType.LeagueView, parameters));
        } catch (Exception e) {
            Log.e(TAG, "Couldn't parse String into JSON");
            return new ResponseStatus(false);
        }
        LinkedList<ContentValues> cV = new LinkedList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try{
                ContentValues dis = new LeagueTeam(jsonArray.getJSONObject(i)).toContentValues();
                dis.put(League.KEY_LEAGUEID, leagueID);
                cV.add(dis);
            } catch (Exception e){
                Log.e(TAG, "Couldn't parse JSON into LeagueTeam object");
                return new ResponseStatus(false);
            }
        }
        ContentValues[] contentValues = cV.toArray(new ContentValues[cV.size()]);
        SQLiteManager.getInstance(context).bulkInsert(DBProviderContract.LEAGUESSTANDINGS_TABLE_NAME, contentValues);
        return new ResponseStatus(true);
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(ResponseStatus responseStatus) {
        callback.leagueViewCallback(responseStatus, leagueID);
    }
}
