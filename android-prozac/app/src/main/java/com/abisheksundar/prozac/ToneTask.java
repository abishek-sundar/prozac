package com.abisheksundar.prozac;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;

import java.util.List;

/**
 * Created by devin_w2uj42a on 2018-09-15.
 */

public class ToneTask extends AsyncTask<String,Void,Sentiment> {
    private Context _c;

    public ToneTask(Context c)
    {
        _c = c;
    }

    @Override
    protected Sentiment doInBackground(String... texts)
    {
        ToneAnalysis toneAnalysis = null;
        String res = "";
        Sentiment ret_sentiment = null;
        double sad_score = 0;
        double fear_score = 0;
        double tentative_score = 0;
        double nscores = 0;
        String uname = _c.getString(R.string.walton_api_username);
        String upass = _c.getString(R.string.walton_api_password);
        ToneAnalyzer analyzer = new ToneAnalyzer("2017-09-21", uname, upass);
        analyzer.setEndPoint("https://gateway.watsonplatform.net/tone-analyzer/api");

        for (String text: texts) {
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

        return ret_sentiment;
    }

    @Override
    protected  void onPostExecute(Sentiment res)
    {
        if (res.get_type() == Sentiment.SAD)
            Log.d("SIMPLE ANALYSIS", "SAD");
        else if (res.get_type() == Sentiment.ANXIOUS)
            Log.d("SIMPLE ANALYSIS", "ANXIOUS");
        else if (res.get_type() == Sentiment.NOT_RELEVANT)
            Log.d("SIMPLE ANALYSIS", "NOT RELEVANT");
    }
}

