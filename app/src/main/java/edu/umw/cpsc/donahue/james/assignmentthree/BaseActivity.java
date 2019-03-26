package edu.umw.cpsc.donahue.james.assignmentthree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class BaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "BaseActivity";

    protected void setupAppBar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        if (myToolbar != null) {
            setSupportActionBar(myToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_menu, menu);

        MenuItem spinner = menu.findItem(R.id.menu_spinner);
        Spinner menuSpinner = (Spinner) spinner.getActionView();
        menuSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.menu_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menuSpinner.setAdapter(adapter);
        return true;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        Intent intent;
        switch (parent.getItemAtPosition(pos).toString()) {
            case "Sent":
                intent = new Intent(this, SentActivity.class);
                startActivity(intent);
                break;
            case "Received":
                intent = new Intent(this, ReceivedActivity.class);
                startActivity(intent);
                break;
            case "Settings":
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                Log.i(TAG, "no");
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
