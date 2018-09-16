package com.abisheksundar.prozac;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.TimeUnit;

import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;

public class MainActivity extends AppCompatActivity {
    private static final int CONTACT_PICKER_RESULT = 1001;
    private MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

    public void doLaunchContactPicker(View view) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // TextView textView=findViewById(R.id.text);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    Cursor cursor = null;
                    String phone = "";

                    Uri result = data.getData();

                    String id = result.getLastPathSegment();

                    cursor = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);

                    if (cursor.moveToFirst()) {
                        String name="";
                        String phoneNum="";
                        name= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        phoneNum= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        if (!phoneNum.equals("") && !name.equals("")) {
                            Log.d("re:", "I am here");
//                            textView.setText(name+" "+ phoneNum);

                            PhoneList friend = new PhoneList(name, phoneNum);
                            //cursor.close();
                            dbHandler.addHandler(friend);
                            dbHandler.printAll();
                        }
                    }
                    break;
            }
        }
    }

    static private Boolean check = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("ONCREATE","ONCREATE TRIGGERED");
        if (!check) {
            OneTimeWorkRequest something = new OneTimeWorkRequest.Builder(CompressWorker.class).build();
            WorkManager.getInstance().enqueue(something);
            check = true;
        }

    }

}


