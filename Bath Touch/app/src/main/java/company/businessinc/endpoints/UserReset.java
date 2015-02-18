package company.businessinc.endpoints;

import android.os.AsyncTask;
import android.util.Log;
import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.networking.APICall;
import company.businessinc.networking.APICallType;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gp on 18/11/14.
 */
public class UserReset extends AsyncTask<Void, Void, ResponseStatus> {
    String TAG = "UserReset";
    private UserResetInterface callback;
    private List<NameValuePair> parameters;

    public UserReset(UserResetInterface callback, String input, boolean isUsername) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        if(isUsername) {
            parameters.add(new BasicNameValuePair("username", input));
        } else {
            parameters.add(new BasicNameValuePair("email", input));
        }
    }

    @Override
    protected ResponseStatus doInBackground(Void... a) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(APICall.call(APICallType.UserReset, parameters));
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return new ResponseStatus(false);
        }

        ResponseStatus bool = null;
        try{
            return new ResponseStatus(jsonObject);
        } catch (JSONException e){
            Log.d(TAG, "Couldn't parse JSON into Status");
            return new ResponseStatus(false);
        }
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(ResponseStatus result) {
        callback.userResetCallback(result);
    }
}