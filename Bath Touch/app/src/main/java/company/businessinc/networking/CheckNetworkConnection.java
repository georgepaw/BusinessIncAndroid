package company.businessinc.networking;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by gp on 19/11/14.
 */
public class CheckNetworkConnection {
    public static boolean check(Activity thisClass){
        boolean b = false;
        ConnectivityManager connMgr = (ConnectivityManager)
                thisClass.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            b = true;
        } else {
            Log.d("CheckNetworkConnection", "No network connection");
        }
        return b;
    }
}
