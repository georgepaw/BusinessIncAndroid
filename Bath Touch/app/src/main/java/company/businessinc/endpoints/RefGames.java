package company.businessinc.endpoints;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.SQLiteManager;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.networking.APICall;
import company.businessinc.networking.APICallType;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gp on 18/11/14.
 */
public class RefGames extends AsyncTask<Void, Void, ResponseStatus> {
    String TAG = "RefGames";
    private RefGamesInterface callback;
    private List<NameValuePair> parameters;
    private Context context;

    public RefGames(RefGamesInterface callback, Context context, int userID) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("refID", Integer.toString(userID)));
        this.context = context;
    }

    public RefGames(RefGamesInterface callback, Context context) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        this.context = context;
    }

    @Override
    protected ResponseStatus doInBackground(Void... a) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(APICall.call(APICallType.RefGames, parameters));
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return new ResponseStatus(false);
        }

        List<Match> list = new LinkedList<Match>();
        for(int i = 0; i < jsonArray.length(); i++){
            try{
                list.add(new Match(jsonArray.getJSONObject(i)));
            } catch (Exception e){
                Log.d(TAG, "Couldn't parse JSON into Match object");
                return new ResponseStatus(false);
            }
        }
        //sort the games in ascending order
        list = Match.sortList(list, Match.SortType.ASCENDING);
        //Should probably replace this with a better search
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(GregorianCalendar.HOUR,-4); //minus 4 hours so that he can see the next game during and after it's being played
        for(Match m : list){
            if(m.getDateTime().compareTo(gc.getTime()) > 0){
                SQLiteManager.getInstance(context).insert(DBProviderContract.MYUPCOMINGREFEREEGAMES_TABLE_NAME, m.toContentValues());
            }
        }
        return new ResponseStatus(true);
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(ResponseStatus responseStatus) {
        callback.refGamesCallback(responseStatus);
    }
}