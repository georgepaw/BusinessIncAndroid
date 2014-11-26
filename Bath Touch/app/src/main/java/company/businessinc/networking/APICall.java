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
import java.net.HttpCookie;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gp on 17/11/14.
 */
public class APICall {

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
        try {
            httpResponse = httpclient.execute(httpGet, localContext);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                Log.d(TAG, "Request wasn't successful, returning null");
                return null;
            }
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                output = EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            Log.d(TAG, "Couldn't execute the httpGet");
        }
        return output;
    }

    public static void clearCookies() {
        cookieStore.clear();
    }

    public static void setCookie(String cookieString) {
        cookieStore.addCookie(parseStringToCookie(cookieString));
    }

    public static String getCookie() {
        String result = null;
        try {
            result = cookieStore.getCookies().get(0).toString(); //right now we only care about 1 cookie
        } catch (Exception e){
        }
        return result;
    }

    private static Cookie parseStringToCookie(String input){
        input = input.substring(1,input.length() - 1).replace(" ", "");
        String cookieArray[] = input.split("]\\[");
        Map<String, String> cookieMap = new HashMap<String, String>();
        for(String part : cookieArray){
            String tuple[] = part.split(":", 2);
            cookieMap.put(tuple[0], tuple[1]);
        }
        BasicClientCookie cookie = new BasicClientCookie(cookieMap.get("name"), cookieMap.get("value"));
        cookie.setVersion(Integer.parseInt(cookieMap.get("version")));
        cookie.setDomain(cookieMap.get("domain"));
        cookie.setPath(cookieMap.get("path"));

        return cookie;
    }
}