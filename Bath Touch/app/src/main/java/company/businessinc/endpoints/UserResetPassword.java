package company.businessinc.endpoints;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import company.businessinc.dataModels.Status;
import company.businessinc.networking.APICall;
import company.businessinc.networking.APICallType;

/**
 * Created by gp on 18/11/14.
 */
public class UserResetPassword extends AsyncTask<Void, Void, Status> {
    String TAG = "UserResetPassword";
    private UserResetPasswordInterface callback;
    private List<NameValuePair> parameters;

    public UserResetPassword(UserResetPasswordInterface callback, String password, String token) {
        this.callback = callback;
        parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("password", password));
        parameters.add(new BasicNameValuePair("token", token));
    }

    @Override
    protected company.businessinc.dataModels.Status doInBackground(Void... a) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(APICall.call(APICallType.UserResetPassword, parameters));
        } catch (Exception e) {
            Log.d(TAG, "Couldn't parse String into JSON");
            return null;
        }

        company.businessinc.dataModels.Status bool = null;
        try{
            bool = new company.businessinc.dataModels.Status(jsonObject);
        } catch (JSONException e){
            Log.d(TAG, "Couldn't parse JSON into Status");
        }
        return bool;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(company.businessinc.dataModels.Status result) {
        callback.userResetPasswordCallback(result);
    }
}