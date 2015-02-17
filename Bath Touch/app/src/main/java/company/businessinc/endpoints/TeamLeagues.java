package company.businessinc.endpoints;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import company.businessinc.bathtouch.data.DataStore;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.SQLiteManager;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.networking.APICall;
import company.businessinc.networking.APICallType;

/**
 * Created by gp on 18/11/14.
 */
public class TeamLeagues extends AsyncTask<Void, Void, ResponseStatus> {
    String TAG = "TeamLeagues";
    private TeamLeaguesInterface callback;
    private List<NameValuePair> parameters;
    private Context context;

    public TeamLeagues(TeamLeaguesInterface callback, int teamID, Context context) {
        this.callback = callback;
        parameters = new LinkedList<>();
        parameters.add(new BasicNameValuePair("teamID", Integer.toString(teamID)));
        this.context = context;
    }

    @Override
    protected ResponseStatus doInBackground(Void... a) {
        JSONArray jsonArray = null;
        //Parse the JSON
        try {
            jsonArray = new JSONArray(APICall.call(APICallType.LeagueList, parameters));
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return new ResponseStatus(false);
        }
        //Add to DB
        LinkedList<ContentValues> cV = new LinkedList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try{
                League league = new League(jsonArray.getJSONObject(i));
                cV.add(league.toContentValues());
                //for every league, get my scores and fixtures
                DataStore.getInstance(context).loadTeamsFixtures(league.getLeagueID(), DataStore.getInstance(context).getUserTeamID());
                DataStore.getInstance(context).loadTeamsLeagueScore(league.getLeagueID(), DataStore.getInstance(context).getUserTeamID());
                } catch (Exception e){
                Log.d(TAG, "Couldn't parse JSON into League object");
                return new ResponseStatus(false);
            }
        }
        ContentValues[] contentValues = cV.toArray(new ContentValues[cV.size()]);
        SQLiteManager.getInstance(context).bulkInsert(DBProviderContract.MYLEAGUES_TABLE_NAME, contentValues);
        return new ResponseStatus(true);
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(ResponseStatus responseStatus) {
        callback.teamLeaguesCallback(responseStatus);
    }
}
