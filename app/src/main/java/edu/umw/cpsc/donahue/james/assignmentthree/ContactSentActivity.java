package edu.umw.cpsc.donahue.james.assignmentthree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ContactSentActivity extends BaseActivity implements ItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_sent);
        this.setupAppBar();
    }

    @Override
    public void itemSelected(int index) {
        Intent intent = new Intent(this, DetailActivity.class);
        String[] array = getResources().getStringArray(R.array.test_sent_values);
        String location = array[index];
        intent.putExtra("location", location);
        startActivity(intent);
    }
}
