package com.abisheksundar.prozac;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Anxious extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anxious);

        Uri data = this.getIntent().getData();
        if (data != null && data.isHierarchical()) {
            String uri = this.getIntent().getDataString();
            TextView dlb = findViewById(R.id.anxious_id);
            dlb.setText("Your friend is anxious. Please talk to them");
            Log.i("MyApp", "Deep link clicked " + uri);
        }
    }
}
