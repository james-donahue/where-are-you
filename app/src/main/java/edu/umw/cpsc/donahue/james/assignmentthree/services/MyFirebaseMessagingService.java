package edu.umw.cpsc.donahue.james.assignmentthree.services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import edu.umw.cpsc.donahue.james.assignmentthree.DataManager;
import edu.umw.cpsc.donahue.james.assignmentthree.MainActivity;
import edu.umw.cpsc.donahue.james.assignmentthree.MapsActivity;
import edu.umw.cpsc.donahue.james.assignmentthree.MyBroadcastReceiver;
import edu.umw.cpsc.donahue.james.assignmentthree.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static String TAG = "MyFirebaseMessagingService";

    private FusedLocationProviderClient fusedLocationClient;
    
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            handleNow(remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    public void handleNow(Map<String,String> messages){
        if (messages.get("type").equals("request")) {
            Intent declineIntent = new Intent(this, MyBroadcastReceiver.class);
            declineIntent.setAction("edu.umw.cpsc.donahue.james.assignmentthree.DECLINE");
            declineIntent.putExtra("TO", messages.get("requestor"));
            PendingIntent declinePendingIntent =
                    PendingIntent.getBroadcast(this, 0, declineIntent, 0);

            Intent acceptIntent = new Intent(this, MyBroadcastReceiver.class);
            acceptIntent.setAction("edu.umw.cpsc.donahue.james.assignmentthree.ACCEPT");
            acceptIntent.putExtra("TO", messages.get("requestor"));
            PendingIntent acceptPendingIntent =
                    PendingIntent.getBroadcast(this, 0, acceptIntent, 0);

            String name = getName(messages.get("requestor"));

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationChannel.DEFAULT_CHANNEL_ID);
            builder.setSmallIcon(R.mipmap.ic_launcher_round);
            builder.setContentTitle("Where you at");
            builder.setContentText("Location requested by " + name);
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            builder.addAction(R.mipmap.ic_launcher, "Decline", declinePendingIntent);
            builder.addAction(R.mipmap.ic_launcher, "Accept", acceptPendingIntent);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, builder.build());
        } else if (messages.get("type").equals("response")) {
            String name = getName(messages.get("responder"));
            if (messages.get("denied").equals("true")) {
                Intent contactsIntent = new Intent(this, MainActivity.class);
                PendingIntent contactsPendingIntent = PendingIntent.getActivity(this, 0, contactsIntent, 0);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationChannel.DEFAULT_CHANNEL_ID);
                builder.setSmallIcon(R.mipmap.ic_launcher_round);
                builder.setContentTitle("Where you at");
                builder.setContentText(name + " declined");
                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                builder.setContentIntent(contactsPendingIntent);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(2, builder.build());
            } else if (messages.get("denied").equals("false")) {
                Intent mapIntent = new Intent(this, MapsActivity.class);
                mapIntent.putExtra("LAT", messages.get("lat"));
                mapIntent.putExtra("LONG", messages.get("lon"));
                mapIntent.putExtra("CONTACT", name);
                PendingIntent mapPendingIntent = PendingIntent.getActivity(this, 0, mapIntent, 0);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationChannel.DEFAULT_CHANNEL_ID);
                builder.setSmallIcon(R.mipmap.ic_launcher_round);
                builder.setContentTitle("Where you at");
                builder.setContentText(name + " accepted");
                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                builder.setContentIntent(mapPendingIntent);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(3, builder.build());

                DataManager.writeToFile(name + ":" + messages.get("lat") + ":" + messages.get("lon"), false, this);
            }
        }
    }

    @Override
    public void onDeletedMessages() {
//        In some situations, FCM may not deliver a message. This occurs when there are too many
//        messages (>100) pending for your app on a particular device at the time it connects or
//        if the device hasn't connected to FCM in more than one month. In these cases, you may
//        receive a callback to FirebaseMessagingService.onDeletedMessages() When the app instance
//        receives this callback, it should perform a full sync with your app server. If you
//        haven't sent a message to the app on that device within the last 4 weeks, FCM won't
//        call onDeletedMessages().
    }

    private String getName(String email) {
        String name = "";

        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                Build.VERSION.SDK_INT
                        >= Build.VERSION_CODES.HONEYCOMB ?
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY :
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Email.DATA
        };

        Uri contactUri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Email.CONTENT_FILTER_URI, Uri.encode(email));

        Cursor cursor = this.getContentResolver().query(contactUri, projection, null, null, null);

        if(cursor != null) {
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(
                        Build.VERSION.SDK_INT
                                >= Build.VERSION_CODES.HONEYCOMB ?
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY :
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                ));
                Log.d(TAG, "Found " + name);
            } else {
                Log.d(TAG, "Contact Not Found");
            }
            cursor.close();
        }

        return name;
    }
}
