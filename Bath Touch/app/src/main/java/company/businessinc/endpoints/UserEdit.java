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
public class UserEdit extends AsyncTask<Void, Void, ResponseStatus> {
    String TAG = "UserEdit";
    private UserEditInterface callback;
    private List<NameValuePair> parameters;

    public UserEdit(UserEditInterface callback, String email, String name, boolean isReferee, boolean isMale, int teamID) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("email", email));
        parameters.add(new BasicNameValuePair("name", name));
        parameters.add(new BasicNameValuePair("teamID", Integer.toString(teamID)));
        parameters.add(new BasicNameValuePair("notifications", isReferee ? "1" : "0"));
        parameters.add(new BasicNameValuePair("gender", isMale ? "0" : "1"));
    }

    @Override
    protected ResponseStatus doInBackground(Void... a) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(APICall.call(APICallType.UserEdit, parameters));
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return new ResponseStatus(false);
        }
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
        callback.userEditCallback(result);
    }
}
