package edu.umw.cpsc.donahue.james.assignmentthree.services;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static String TAG = "MyFirebaseInstanceIdService";

    public String getToken(){
        return FirebaseInstanceId.getInstance().getToken();
    }

       @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken,this);
    }

    public void sendRegistrationToServer(String token, Context context) {
 
    }
}
