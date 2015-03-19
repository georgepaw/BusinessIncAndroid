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
public class LeagueToday extends AsyncTask<Void, Void, ResponseStatus> {
    String TAG = "LeagueScores";
    private LeagueScoresInterface callback;
    private List<NameValuePair> parameters;
    private int leagueID;
    private Context context;

    public LeagueToday(LeagueScoresInterface callback, Context context, int leagueID) {
        this.callback = callback;
        this.leagueID = leagueID;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("leagueID", Integer.toString(leagueID)));
        this.context = context;
    }

    @Override
    protected ResponseStatus doInBackground(Void... a) {
        JSONArray jsonArray = null;
        //convert to JSON
        try {
            jsonArray = new JSONArray(APICall.call(APICallType.LeagueToday, parameters));
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return new ResponseStatus(false);
        }
        //Add to DB
        LinkedList<ContentValues> cV = new LinkedList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try{
                ContentValues dis = new Match(leagueID, jsonArray.getJSONObject(i)).toContentValues();
                dis.put(Match.KEY_LEAGUEID, leagueID);
                cV.add(dis);
            } catch (Exception e){
                Log.d(TAG, "Couldn't parse JSON into Match object");
                return new ResponseStatus(false);
            }
        }
        ContentValues[] contentValues = cV.toArray(new ContentValues[cV.size()]);
        SQLiteManager.getInstance(context).bulkInsert(DBProviderContract.LEAGUETODAY_TABLE_NAME, contentValues);
        return new ResponseStatus(true);
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(ResponseStatus responseStatus) {
        callback.leagueScoresCallback(responseStatus, leagueID);
    }
}
