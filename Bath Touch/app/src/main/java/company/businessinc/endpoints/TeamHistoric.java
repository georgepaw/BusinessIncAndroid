package company.businessinc.endpoints;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.SQLiteManager;
import company.businessinc.dataModels.*;
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
public class TeamHistoric extends AsyncTask<Void, Void, ResponseStatus> {
    String TAG = "TeamScores";
    private TeamHistoricInterface callback;
    private List<NameValuePair> parameters;
    private int leagueID;
    private int teamID;
    private Context context;

    public TeamHistoric(TeamHistoricInterface callback, Context context, int leagueID, int teamID) {
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
            jsonArray = new JSONArray(APICall.call(APICallType.TeamHistoric, parameters));
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return new ResponseStatus(false);
        }
        LinkedList<ContentValues> cV = new LinkedList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try{
                ContentValues dis = new HistoricTeam(jsonArray.getJSONObject(i)).toContentValues();
                dis.put(League.KEY_LEAGUEID, leagueID);
                cV.add(dis);
            } catch (Exception e){
                Log.d(TAG, "Couldn't parse JSON into Match object");
                return new ResponseStatus(false);
            }
        }
        ContentValues[] contentValues = cV.toArray(new ContentValues[cV.size()]);
        SQLiteManager.getInstance(context).bulkInsert(DBProviderContract.TEAMHISTORIC_TABLE_NAME, contentValues);

        return new ResponseStatus(true);
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(ResponseStatus responseStatus) {
        callback.teamHistoricCallback(responseStatus, leagueID, teamID);
    }
}