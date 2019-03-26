package edu.umw.cpsc.donahue.james.assignmentthree;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class ContactFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private ItemSelectedListener itemSelectedListener;

    private FusedLocationProviderClient fusedLocationClient;

    /*
    * Defines an array that contains column names to move from
    * the Cursor to the ListView.
    */
    @SuppressLint("InlinedApi")
    private final static String[] FROM_COLUMNS = {
            Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY :
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
    };
    /*
     * Defines an array that contains resource ids for the layout views
     * that get the Cursor column contents. The id is pre-defined in
     * the Android framework, so it is prefaced with "android.R.id"
     */
    private final static int[] TO_IDS = {
            android.R.id.text1
    };

    // An adapter that binds the result Cursor to the ListView
    private SimpleCursorAdapter mCursorAdapter;

    @SuppressLint("InlinedApi")
    private static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    Build.VERSION.SDK_INT
                            >= Build.VERSION_CODES.HONEYCOMB ?
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY :
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Email.DATA
            };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void getContactList() {

        // Initializes the loader
        getLoaderManager().initLoader(0, null, this);

        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.contacts, android.R.layout.simple_list_item_1);
        // Gets a CursorAdapter
        mCursorAdapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_1,
                null,
                FROM_COLUMNS, TO_IDS,
                0);

        this.setListAdapter(mCursorAdapter);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        SimpleCursorAdapter cursorAdapter = (SimpleCursorAdapter) this.getListAdapter();
        Cursor cursor = cursorAdapter.getCursor();
        // Move to the selected contact
        cursor.moveToPosition(position);
        //final String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
        final String contactEmail = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

        String myEmail = DataManager.readUsername(this.getContext());
        Log.d("ContactFragment", myEmail);
        Log.d("ContactFragment", contactEmail);

        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        String url = "https://cpsc470amobiledevelopment.appspot.com/messaging?type=request&id=" +
                myEmail + "&to=" + contactEmail;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ContactFragment", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                Toast toast = Toast.makeText(ContactFragment.this.getContext(), "Something went wrong", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        /*if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED)
        {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>()
            {
                @Override
                public void onSuccess(Location location)
                {
// Got last known location. In some rare situations this can be null.
                    if (location != null)
                    {
// Logic to handle location object
                        String data = contactName + ":"
                            + location.getLatitude() + ":" + location.getLongitude() + "\n";
                        DataManager.writeToFile(data, false, ContactFragment.this.getContext());
                    } else {
                        Toast toast = Toast.makeText(ContactFragment.this.getContext(), "No location", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
        }*/

        //itemSelectedListener.itemSelected(position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            itemSelectedListener = (ItemSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ItemSelectedListener");
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        /*
         * Makes search string into pattern and
         * stores it in the selection array
         */
        // Starts the query
        return new CursorLoader(
                getActivity(),
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                PROJECTION,
                null,
                null,
                null
        );
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Put the result Cursor in the adapter for the ListView
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Delete the reference to the existing Cursor
        mCursorAdapter.swapCursor(null);
    }
}