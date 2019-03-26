package edu.umw.cpsc.donahue.james.assignmentthree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        this.setupAppBar();

        Intent intent = getIntent();
        TextView textView = findViewById(R.id.detail_text);
        String location = intent.getStringExtra(KeyStrings.LOCATION);
        textView.setText(location);
    }
}
