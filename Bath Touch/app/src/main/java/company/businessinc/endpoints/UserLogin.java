package company.businessinc.endpoints;

import android.os.AsyncTask;
import android.util.Log;
import company.businessinc.dataModels.User;
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
public class UserLogin extends AsyncTask<Void, Void, User> {
    String TAG = "UserLogin";
    private UserLoginInterface callback;
    private List<NameValuePair> parameters;

    public UserLogin(UserLoginInterface callback, String username, String password) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("username", username));
        parameters.add(new BasicNameValuePair("password", password));
    }

    @Override
    protected User doInBackground(Void... a) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(APICall.call(APICallType.UserLogin, parameters));
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return null;
        }
        User user = null;
        try {
            user = new User(jsonObject);
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse parameters");
        }
        return user;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(User result) {
        callback.userLoginCallback(result);
    }
}
