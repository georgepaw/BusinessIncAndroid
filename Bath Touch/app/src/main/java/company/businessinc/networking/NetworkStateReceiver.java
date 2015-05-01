package company.businessinc.networking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import company.businessinc.bathtouch.data.DataStore;

/**
 * Created by Grzegorz on 03/03/2015.
 */
public class NetworkStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkStateReceiver";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        //The network state has changed
        if (intent.getExtras() != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
            //if the active network state has changed from disconnected to connected or connecting
            if (ni != null && ni.isConnectedOrConnecting()) {
                //then try to submit the scores
                DataStore.getInstance(context).internetIsBack();
            }
        }
    }
}
