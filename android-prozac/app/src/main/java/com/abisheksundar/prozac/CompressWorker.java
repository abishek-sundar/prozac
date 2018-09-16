package com.abisheksundar.prozac;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;

import java.util.List;

import androidx.work.Worker;


public class CompressWorker extends Worker {
    public String[] messages= new String[2]; //last 5 messages
    public Boolean hasItChanged = true;
    @Override
    public Worker.Result doWork() {
//        MainActivity mActive = new MainActivity();
//
//        mActive.displaySmsLog();
        Context cont = this.getApplicationContext();
        String[] temp = messages;
        Uri msgs = Uri.parse("content://sms/sent");
        Boolean in = false;
        //Cursor cursor = managedQuery(allMessages, null, null, null, null); Both are same
//        while (!in) {
//            if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
        Cursor cursor = cont.getContentResolver().query(msgs, null,
                null, null, null);
        in = true;
        Integer count = 0;
        while (cursor.moveToNext() && count<2) {

            for (int i = 0; i < cursor.getColumnCount(); i++) {
                if (i == 12) {
                    Log.d(cursor.getColumnName(i) + "", cursor.getString(i));
                    messages[count] = cursor.getString(i);
                }
            }
            count++;
            Log.d("**************","*******************************************");
//                }
//            }
//            else {
//
//                final int REQUEST_CODE_ASK_PERMISSIONS = 123;
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
//            }
        }
        cursor.close();
        hasItChanged = true;
        if (temp == messages) hasItChanged = false;

        ToneAnalysis toneAnalysis = null;
        String res = "";
        Sentiment ret_sentiment = null;
        double sad_score = 0;
        double fear_score = 0;
        double tentative_score = 0;
        double nscores = 0;
        String uname = cont.getString(R.string.walton_api_username);
        String upass = cont.getString(R.string.walton_api_password);
        ToneAnalyzer analyzer = new ToneAnalyzer("2017-09-21", uname, upass);
        analyzer.setEndPoint("https://gateway.watsonplatform.net/tone-analyzer/api");

        for (String text: messages) {
            ToneOptions toneOptions = new ToneOptions.Builder().sentences(false).text(text).build();

            toneAnalysis = analyzer.tone(toneOptions).execute();

            List<ToneScore> scores = toneAnalysis.getDocumentTone().getTones();

            nscores += 1.0;

            for (int i = 0; i < scores.size(); ++i) {
                ToneScore score = scores.get(i);
                String id = score.getToneId();
                double scoreValue = score.getScore();

                if (id.equals("sadness")) {
                    sad_score += scoreValue;
                }
                else if (id.equals("fear"))
                {
                    fear_score += scoreValue;
                }
                else if (id.equals("tentative"))
                {
                    tentative_score += scoreValue;
                }
                res += id + " " + scoreValue + " ";
            }

        }
        sad_score /= nscores;
        fear_score /= nscores;
        tentative_score /= nscores;

        if (sad_score >= 0.65 && tentative_score >= 0.65)
            ret_sentiment = new Sentiment(Sentiment.ANXIOUS);
        else if (sad_score >= 0.65)
            ret_sentiment = new Sentiment(Sentiment.SAD);
        else
            ret_sentiment = new Sentiment(Sentiment.NOT_RELEVANT);

        Log.d("SENT ANALYSIS", res);

        switch(ret_sentiment.get_type())
        {
            case Sentiment.ANXIOUS:
                Log.d("Parsed analysis:","ANXIOUS");
                break;
            case Sentiment.SAD:
                Log.d("Parsed analysis:","SAD");
                break;
            default:
                Log.d("Parsed analysis:","dont' care");

        }

        sendMessage("7788728011", "U sed",  cont);
        return Result.SUCCESS;
    }
    public static void sendMessage(String number, String message, Context context){
        SmsManager sms = SmsManager.getDefault();
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent("SMS_INTENT"), 0);
        sms.sendTextMessage(number, null, message, pi, null);
    }

}
