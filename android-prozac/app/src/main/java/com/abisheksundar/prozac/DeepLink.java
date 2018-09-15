package com.abisheksundar.prozac;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class DeepLink extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);

        Uri data = this.getIntent().getData();
        if (data != null && data.isHierarchical()) {
            String uri = this.getIntent().getDataString();
            Log.i("MyApp", "Deep link clicked " + uri);
        }
    }
}
