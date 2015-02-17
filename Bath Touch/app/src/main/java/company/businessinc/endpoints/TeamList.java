package company.businessinc.endpoints;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.LinkedList;
import java.util.List;

import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.SQLiteManager;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.dataModels.Team;
import company.businessinc.networking.APICall;
import company.businessinc.networking.APICallType;

/**
 * Created by gp on 18/11/14.
 */
public class TeamList extends AsyncTask<Void, Void, ResponseStatus> {
    String TAG = "TeamList";
    private TeamListInterface callback;
    private List<NameValuePair> parameters;
    public enum CallType{GETALLTEAMS, GETLEAGUETEAMS}
    private CallType callType;
    private int leagueID;
    private Context context;

    public TeamList(TeamListInterface callback, Context context) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        callType = CallType.GETALLTEAMS;
        this.context = context;
    }

    public TeamList(TeamListInterface callback, int leagueID, Context context) {
        this.callback = callback;
        this.leagueID = leagueID;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("leagueID", Integer.toString(leagueID)));
        callType = CallType.GETLEAGUETEAMS;
        this.context = context;
    }

    @Override
    protected ResponseStatus doInBackground(Void... a) {
        JSONArray jsonArray = null;
        //First parse JSON
        try {
            jsonArray = new JSONArray(APICall.call(APICallType.TeamList, parameters));
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return new ResponseStatus(false);
        }
        //Add them to the DB
        LinkedList<ContentValues> cV = new LinkedList<>();
        List<Integer> teamIdsAlreadyAdded = new LinkedList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try{
                Team t = new Team(jsonArray.getJSONObject(i));
                if(!teamIdsAlreadyAdded.contains(t.getTeamID())) {
                    //TODO will web team fix this
                    //only add unique teams
                    //this is due to TeamList returning duplicates of teams that are in mote than one league
                    //when getting all the teams from the endpoitns
                    ContentValues dis = t.toContentValues();
                    if(TeamList.CallType.GETLEAGUETEAMS == callType){
                        dis.put(League.KEY_LEAGUEID, leagueID);
                    }
                    cV.add(dis);
                    teamIdsAlreadyAdded.add(t.getTeamID());
                }
            } catch (Exception e){
                Log.d(TAG, "Couldn't parse JSON into Team object");
                return new ResponseStatus(false);
            }
        }
        ContentValues[] contentValues = cV.toArray(new ContentValues[cV.size()]);
        switch(callType){
            case GETALLTEAMS:
                SQLiteManager.getInstance(context).bulkInsert(DBProviderContract.ALLTEAMS_TABLE_NAME, contentValues);
                break;
            case GETLEAGUETEAMS:
                SQLiteManager.getInstance(context).bulkInsert(DBProviderContract.LEAGUETEAMS_TABLE_NAME,contentValues);
                break;
        }
        return new ResponseStatus(true);
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(ResponseStatus successful) {
        callback.teamListCallback(successful, callType);
    }
}