package company.businessinc.endpoints;

import android.os.AsyncTask;
import android.util.Log;
import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.networking.APICall;
import company.businessinc.networking.APICallType;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gp on 18/11/14.
 */
public class UserNew extends AsyncTask<Void, Void, ResponseStatus> {
    String TAG = "UserEdit";
    private UserNewInterface callback;
    private List<NameValuePair> parameters;

    public UserNew(UserNewInterface callback, String username, String password, String email, String name, int teamID, boolean isGhost) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("username", username));
        parameters.add(new BasicNameValuePair("password", password));
        parameters.add(new BasicNameValuePair("email", email));
        parameters.add(new BasicNameValuePair("name", name));
        parameters.add(new BasicNameValuePair("teamID", Integer.toString(teamID)));
        parameters.add(new BasicNameValuePair("isGhost", Boolean.toString(isGhost)));
    }

    public UserNew(UserNewInterface callback, String name, int teamID, boolean isGhost) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("name", name));
        parameters.add(new BasicNameValuePair("teamID", Integer.toString(teamID)));
        parameters.add(new BasicNameValuePair("isGhost", Boolean.toString(isGhost)));
    }

    public UserNew(UserNewInterface callback, String username, String password, String email, String name, int teamID) {
        this(callback, username, password, email, name, teamID, false);
    }

    @Override
    protected ResponseStatus doInBackground(Void... a) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(APICall.call(APICallType.UserNew, parameters));
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
    protected void onPostExecute(ResponseStatus result) {
        callback.userNewCallback(result);
    }
}
