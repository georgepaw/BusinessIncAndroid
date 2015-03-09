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
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gp on 18/11/14.
 */
public class DeviceRegister extends AsyncTask<Void, Void, ResponseStatus> {
    String TAG = "LeagueList";
    private DeviceRegisterInterface callback;
    private List<NameValuePair> parameters;
    private Context context;


    public DeviceRegister(DeviceRegisterInterface callback, Context context, String regID) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("deviceID", regID));
        this.context = context;
    }

    @Override
    protected ResponseStatus doInBackground(Void... a) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(APICall.call(APICallType.DeviceRegister, parameters));
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return new ResponseStatus(false);
        }
        try{
            return new ResponseStatus(jsonObject);
        } catch (Exception e){
            Log.d(TAG, "Couldn't parse JSON into Status");
            return new ResponseStatus(false);
        }
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(ResponseStatus responseStatus) {
        callback.deviceRegisterCallback(responseStatus);
    }
}
