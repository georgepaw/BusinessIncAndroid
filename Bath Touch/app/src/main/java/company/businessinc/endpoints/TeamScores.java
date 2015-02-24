package company.businessinc.endpoints;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.SQLiteManager;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.dataModels.Team;
import company.businessinc.networking.APICall;
import company.businessinc.networking.APICallType;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gp on 18/11/14.
 */
public class TeamScores extends AsyncTask<Void, Void, ResponseStatus> {
    String TAG = "TeamScores";
    private TeamScoresInterface callback;
    private List<NameValuePair> parameters;
    private int leagueID;
    private int teamID;
    private Context context;

    public TeamScores(TeamScoresInterface callback, Context context, int leagueID, int teamID) {
        this.callback = callback;
        this.leagueID = leagueID;
        this.teamID = teamID;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("leagueID", Integer.toString(leagueID)));
        parameters.add(new BasicNameValuePair("teamID", Integer.toString(teamID)));
        this.context= context;
    }

    @Override
    protected ResponseStatus doInBackground(Void... a) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(APICall.call(APICallType.TeamScores, parameters));
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return new ResponseStatus(false);
        }
        LinkedList<ContentValues> teamScores = new LinkedList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try{
                ContentValues dis = new Match(leagueID, jsonArray.getJSONObject(i)).toContentValues();
                dis.put(League.KEY_LEAGUEID, leagueID);
                //all the team scores should also be stored as league scores
                SQLiteManager.getInstance(context).insert(DBProviderContract.LEAGUESSCORE_TABLE_NAME, dis);
                dis.put(Team.KEY_TEAMID, teamID);
                teamScores.add(dis);
            } catch (Exception e){
                Log.d(TAG, "Couldn't parse JSON into Match object");
                return new ResponseStatus(false);
            }
        }
        ContentValues[] contentValues = teamScores.toArray(new ContentValues[teamScores.size()]);
        SQLiteManager.getInstance(context).bulkInsert(DBProviderContract.TEAMSSCORES_TABLE_NAME, contentValues);

        return new ResponseStatus(true);
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(ResponseStatus responseStatus) {
        callback.teamScoresCallback(responseStatus, leagueID, teamID);
    }
}