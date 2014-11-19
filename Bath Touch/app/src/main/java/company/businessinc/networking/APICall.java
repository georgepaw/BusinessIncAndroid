package company.businessinc.networking;

import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by gp on 17/11/14.
 */
public class APICall{

    private static final String url = "http://www.watchanysport.com";
    private static final String TAG = "APICall";
    private static HttpClient httpclient = new DefaultHttpClient();

    public static String call(APICallType apiCallType, List<NameValuePair> parameters) {
        Uri.Builder uri = Uri.parse(url).buildUpon();
        uri.path("api" + apiCallType.getEndPoint());
        String output = null;
        for (NameValuePair x : parameters) {
            uri.appendQueryParameter(x.getName(), x.getValue());
        }
        HttpGet httpGet = new HttpGet(uri.build().toString());
        HttpResponse httpResponse;
        try{
            //TODO cookies!
            httpResponse = httpclient.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode() != 200){
                Log.d(TAG, "Request wasn't successful, returning null");
                return null;
            }
            HttpEntity entity = httpResponse.getEntity();
            if(entity != null){
                output = EntityUtils.toString(entity);
            }
        } catch (IOException e){
            Log.d(TAG,"Couldn't execute the httpGet");
        }
        return output;
    }
}


