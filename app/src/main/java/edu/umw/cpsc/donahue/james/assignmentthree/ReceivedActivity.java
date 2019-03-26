package edu.umw.cpsc.donahue.james.assignmentthree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ReceivedActivity extends BaseActivity implements ItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received);
        this.setupAppBar();
    }

    @Override
    public void itemSelected(int index) {
        /*Intent intent = new Intent(this, MapsActivity.class);
        String[] array = getResources().getStringArray(R.array.test_sent_values);
        String location = array[index];
        intent.putExtra("location", location);
        startActivity(intent);*/
    }
}
