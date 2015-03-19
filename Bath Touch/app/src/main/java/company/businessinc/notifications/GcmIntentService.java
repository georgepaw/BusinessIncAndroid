package company.businessinc.notifications;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import company.businessinc.bathtouch.DateFormatter;
import company.businessinc.bathtouch.MainActivity;
import company.businessinc.bathtouch.R;
import company.businessinc.bathtouch.data.DataStore;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Grzegorz on 09/03/2015.
 */
public class GcmIntentService extends IntentService {
    public static final String TAG = "GcmIntentService";
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            switch (messageType) {
                case GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR:
                case GoogleCloudMessaging.MESSAGE_TYPE_DELETED:
                    break;
                case GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE:
                    sendNotification(intent.getExtras());
                    break;
            }
            GCMMessageReceiver.completeWakefulIntent(intent);
        }
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(Bundle bundle) {
        String messageType = bundle.getString("gcmType");
        String notificationTitle = "";
        String notificationBody = "";
        Date dateTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
        switch(messageType){ //All the parameters are descrbed in the API file in the SPE folder
            case "1": //Player requests
                notificationTitle = "Player request";
                String teamName = bundle.getString("teamName");
                String gender = bundle.getString("gender").equals("0") ? "male" : "female";
                try {
                    dateTime = sdf.parse(bundle.getString("date"));
                } catch (Exception e){

                }
                notificationBody = teamName + " are looking for " + gender + " players, can you help?";
                DataStore.getInstance(this).refreshRequests();
                break;
            case "2": //ref assigment
                notificationTitle = "New referee allocation";
                String location = bundle.getString("location");

                try {
                    dateTime = sdf.parse(bundle.getString("date"));
                    notificationBody = "You have been assigned to referee on " + new DateFormatter().format(dateTime) + " at " + location;
                } catch (Exception e){
                }
                DataStore.getInstance(this).refreshRefGames();
                break;

        }
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(notificationTitle)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(notificationBody))
                        .setContentText(notificationBody);

        mBuilder.setContentIntent(contentIntent);

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }
}
