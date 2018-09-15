package com.abisheksundar.prozac;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.work.Worker;


public class CompressWorker extends Worker {
    @Override
    public Worker.Result doWork() {
//        MainActivity mActive = new MainActivity();
//
//        mActive.displaySmsLog();
        Context cont = this.getApplicationContext();

        Uri msgs = Uri.parse("content://sms/sent");
        Boolean in = false;
        //Cursor cursor = managedQuery(allMessages, null, null, null, null); Both are same
//        while (!in) {
//            if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
        Cursor cursor = cont.getContentResolver().query(msgs, null,
                null, null, null);
        in = true;

        while (cursor.moveToNext()) {
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                        Log.d(cursor.getColumnName(i) + "", cursor.getString(i) + "");
            }

//                }
//            }
//            else {
//
//                final int REQUEST_CODE_ASK_PERMISSIONS = 123;
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
//            }
        }
        cursor.close();

        return Result.SUCCESS;
    }


}
