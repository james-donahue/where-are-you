package edu.umw.cpsc.donahue.james.assignmentthree;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class MainActivity extends BaseActivity implements ItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 150;

    private static final int MY_PERMISSIONS_INTERNET = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        /*DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (metrics.heightPixels > metrics.widthPixels) {
            isDualPane = false;*/

        setContentView(R.layout.one_pane);
        /*} else {
            isDualPane = true;
            setContentView(R.layout.two_pane);
        }*/
        this.setupAppBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            ContactFragment cf = (ContactFragment) getSupportFragmentManager().findFragmentById(R.id.test_contact_list);
            cf.getContactList();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, MY_PERMISSIONS_INTERNET);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    ContactFragment cf = (ContactFragment) getSupportFragmentManager().findFragmentById(R.id.test_contact_list);
                    cf.getContactList();
                }
            }
        }
    }

    @Override
    public void itemSelected(int index) {
    }
}
