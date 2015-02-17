package company.businessinc.endpoints;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.bathtouch.data.SQLiteManager;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.dataModels.Team;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import company.businessinc.dataModels.Match;
import company.businessinc.networking.APICall;
import company.businessinc.networking.APICallType;

/**
 * Created by gp on 18/11/14.
 */
public class TeamSchedule extends AsyncTask<Void, Void, ResponseStatus> {
    String TAG = "TeamSchedule";
    private TeamScheduleInterface callback;
    private List<NameValuePair> parameters;
    private int leagueID;
    private int teamID;
    private Context context;

    public TeamSchedule(TeamScheduleInterface callback, Context context, int leagueID, int teamID) {
        this.callback = callback;
        this.leagueID = leagueID;
        this.teamID = teamID;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("leagueID", Integer.toString(leagueID)));
        parameters.add(new BasicNameValuePair("teamID", Integer.toString(teamID)));
        this.context = context;
    }

    @Override
    protected ResponseStatus doInBackground(Void... a) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(APICall.call(APICallType.TeamSchedule, parameters));
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return new ResponseStatus(false);
        }

        LinkedList<ContentValues> cV = new LinkedList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try{
                Match m = new Match(jsonArray.getJSONObject(i));
                ContentValues dis = m.toContentValues();
                dis.put(League.KEY_LEAGUEID, leagueID);
                //check is this is a game that current user would play in, if yes add it to upcoming games
                if(m.getTeamOneID() == DataStore.getInstance(context).getUserTeamID()
                        || m.getTeamTwoID() == DataStore.getInstance(context).getUserTeamID()){
                    SQLiteManager.getInstance(context).insert(DBProviderContract.MYUPCOMINGGAMES_TABLE_NAME, dis);
                }
                dis.put(Team.KEY_TEAMID, teamID);
                cV.add(dis);
            } catch (Exception e){
                Log.d(TAG, "Couldn't parse JSON into Match object");
                return new ResponseStatus(false);
            }
        }
        ContentValues[] contentValues = cV.toArray(new ContentValues[cV.size()]);
        SQLiteManager.getInstance(context).bulkInsert(DBProviderContract.TEAMSFIXTURES_TABLE_NAME, contentValues);
        return new ResponseStatus(true);
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(ResponseStatus responseStatus) {
        callback.teamScheduleCallback(responseStatus, leagueID, teamID);
    }
}