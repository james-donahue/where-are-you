package edu.umw.cpsc.donahue.james.assignmentthree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SentActivity extends BaseActivity implements ItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent);
        this.setupAppBar();

        /*ListView list = (ListView) findViewById(R..sent_list);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.test_sent_values, android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);*/
    }

    @Override
    public void itemSelected(int index) {
        /*Intent intent = new Intent(this, DetailActivity.class);
        String[] array = getResources().getStringArray(R.array.test_sent_values);
        String location = array[index];
        intent.putExtra("location", location);
        startActivity(intent);*/
    }
}
