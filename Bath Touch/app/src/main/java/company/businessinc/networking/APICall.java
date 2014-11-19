package company.businessinc.networking;

import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by gp on 17/11/14.
 */
public class APICall{

    private static final String url = "http://www.watchanysport.com";
    private static final String TAG = "APICall";
    private static HttpClient httpclient = AndroidHttpClient.newInstance("Android");
    private static CookieStore cookieStore = new BasicCookieStore();

    public static String call(APICallType apiCallType, List<NameValuePair> parameters) {

        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        Uri.Builder uri = Uri.parse(url).buildUpon();
        uri.path("api" + apiCallType.getEndPoint());
        String output = null;
        for (NameValuePair x : parameters) {
            uri.appendQueryParameter(x.getName(), x.getValue());
        }
        HttpGet httpGet = new HttpGet(uri.build().toString());
        httpGet.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.NETSCAPE);
        HttpResponse httpResponse;
        try{
            httpResponse = httpclient.execute(httpGet, localContext);
            if(httpResponse.getStatusLine().getStatusCode() != 200){
                Log.d(TAG, "Request wasn't successful, returning null");
                return null;
            }
            if(apiCallType == APICallType.UserLogin){ //if this is a login then add cookies
                Header[] headers = httpResponse.getHeaders("Set-Cookie");
                for(Header a : headers){
                    String[] getParts = a.getValue().split(";");
                    BasicClientCookie cookie = new BasicClientCookie("Cookie", getParts[0]);
                    String[] path = getParts[1].split("=");
                    cookie.setPath(path[1]);
                    cookieStore.addCookie(cookie);
                }
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


