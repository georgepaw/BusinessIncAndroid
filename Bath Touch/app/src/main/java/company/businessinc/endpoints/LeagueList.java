package company.businessinc.endpoints;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.SQLiteManager;
import company.businessinc.dataModels.League;
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
public class LeagueList extends AsyncTask<Void, Void, ResponseStatus> {
    String TAG = "LeagueList";
    private LeagueListInterface callback;
    private List<NameValuePair> parameters;
    private Context context;
    private boolean liveCall = false;


    public LeagueList(LeagueListInterface callback, Context context) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        this.context = context;
    }

    public LeagueList(LeagueListInterface callback, Context context, boolean live) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("live",""));
        this.context = context;
        liveCall = live;
    }

    @Override
    protected ResponseStatus doInBackground(Void... a) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(APICall.call(APICallType.LeagueList, parameters));
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return new ResponseStatus(false);
        }
        LinkedList<ContentValues> cV = new LinkedList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try{
                cV.add(new League(jsonArray.getJSONObject(i)).toContentValues());
            } catch (Exception e){
                Log.d(TAG, "Couldn't parse JSON into League object");
                return new ResponseStatus(false);
            }
        }
        ContentValues[] contentValues = cV.toArray(new ContentValues[cV.size()]);
        if(liveCall){
            SQLiteManager.getInstance(context).bulkInsert(DBProviderContract.LIVELEAGUE_TABLE_NAME, contentValues);
        } else {
            SQLiteManager.getInstance(context).bulkInsert(DBProviderContract.ALLLEAGUES_TABLE_NAME, contentValues);
        }
        return new ResponseStatus(true);
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(ResponseStatus responseStatus) {
        callback.leagueListCallback(responseStatus, liveCall);
    }
}
