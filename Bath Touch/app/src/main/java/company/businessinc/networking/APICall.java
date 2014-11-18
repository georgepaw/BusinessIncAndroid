package company.businessinc.networking;

import android.net.Uri;
import android.util.Log;

import org.apache.http.NameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by gp on 17/11/14.
 */
public class APICall {

    private static final String TAG = "APICall";

    public static String call(String endPoint, List<NameValuePair> parameters) {
        Uri.Builder uri = Uri.parse("http://www.watchanysport.com").buildUpon();
        uri.path("api" + endPoint);
        String output;
        URL url;
        HttpURLConnection urlConnection;
        for (NameValuePair x : parameters) {
            uri.appendQueryParameter(x.getName(), x.getValue());
        }
        try {
            String a = uri.build().toString();
            url = new URL(a);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            output = total.toString();
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            Log.wtf(TAG, "Malformed url?!");
            return null;
        } catch (IOException e) {
            Log.d(TAG, "Exception when opening connection");
            return null;
        }
        return output;
    }
}


