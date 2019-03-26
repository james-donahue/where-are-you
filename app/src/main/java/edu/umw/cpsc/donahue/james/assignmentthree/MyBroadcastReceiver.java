package edu.umw.cpsc.donahue.james.assignmentthree;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("edu.umw.cpsc.donahue.james.assignmentthree.DECLINE")) {
            String username = DataManager.readUsername(context);
            String to = intent.getStringExtra("TO");
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = "https://cpsc470amobiledevelopment.appspot.com/messaging?type=response&id=" +
                    username + "&to=" + to + "&denied=true";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("MyBroadcastReceiver", response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast toast = Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        } else if (action.equals("edu.umw.cpsc.donahue.james.assignmentthree.ACCEPT")) {
            String username = DataManager.readUsername(context);
            String to = intent.getStringExtra("TO");
            double latitude = 0.0;
            double longitude = 0.0;
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                String locationProvider = LocationManager.GPS_PROVIDER;
                Location location = locationManager.getLastKnownLocation(locationProvider);
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            DataManager.writeToFile(getName(context, to) + ":" + latitude + ":" + longitude,
                    true, context);

            RequestQueue queue = Volley.newRequestQueue(context);
            String url = "https://cpsc470amobiledevelopment.appspot.com/messaging?type=response&id=" +
                    username + "&to=" + to + "&denied=false" + "&lat=" + latitude + "&lon=" + longitude;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("MyBroadcastReceiver", response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast toast = Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }

    private String getName(Context context, String email) {
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

        Cursor cursor = context.getContentResolver().query(contactUri, projection, null, null, null);

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