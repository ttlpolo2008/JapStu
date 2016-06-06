package com.learning.japstu.japstu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.google.gson.JsonObject;
import com.learning.japstu.japstu.objects.Reading;

public class SpeakingPresentationActivity extends AppCompatActivity {

    protected String title = "";

    public long sLessonID = Long.MIN_VALUE;
    public String slessonName = "";
    protected Reading reading;
    protected boolean bFinish;
    
    protected boolean relearn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking_presentation);
        // get data from other screen
        Bundle bundle = getIntent().getExtras();

        sLessonID = bundle.getLong("lessonID");
        slessonName = bundle.getString("lessonName");
        relearn = bundle.getBoolean("relearn");

        // set title
        title = getResources()
                .getString(R.string.title_activity_speaking_presentation);
        ((TextView)findViewById(R.id.left_header_content)).setText(title);
        ((TextView)findViewById(R.id.left_header_content))
                .setTextColor(getResources().getColor(R.color.title_page_color));
        ((TextView)findViewById(R.id.right_header_content))
                .setText(slessonName);
        ((TextView)findViewById(R.id.right_header_content))
                .setTextColor(getResources().getColor(R.color.title_page_color));


        ((TextView)findViewById(R.id.reading_title))
                .setTextColor(getResources().getColor(R.color.title_lesson_color));
        ((TextView)findViewById(R.id.reading_content))
                .setTextColor(getResources().getColor(R.color.meaning_color));
        ((TextView)findViewById(R.id.reading_hiragana))
                .setTextColor(getResources().getColor(R.color.hint_color));
        ((TextView)findViewById(R.id.reading_meaning))
                .setTextColor(getResources().getColor(R.color.hint_color));
        // TODO : query content from database and set to view
        new ReadingLoadingAsyncTask().execute("");
    }

    private class ReadingLoadingAsyncTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(SpeakingPresentationActivity.this,
                    "Loading", "Load reading");
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                reading = new Reading(sLessonID);

            }catch (Exception e){
                System.out.println(e.getMessage().toString());
                //finish();
            }
            return null;
        }

        protected void onPostExecute(Void unused){
            dialog.dismiss();
            mHandler.sendEmptyMessage(0);
        }
    }


    // handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            try {
                loadReadingToScreen();

            }catch (Exception e){
                // no data found
                Commons.showAlertDialog("No reading to learn.", getApplicationContext());
            }

        }
    };
    @Override
    public void onBackPressed() {
        if(relearn){
            setResult(0);
            super.onBackPressed();
            return;
        }
        setResult(3);
        super.onBackPressed();
    }

    protected void loadReadingToScreen(){
        JsonObject content = reading.content.getAsJsonArray().get(0).getAsJsonObject();
        String[] readingContent = content.get("content").getAsString().split("\n");

        String[] readingContentTranslate = content.get("contentTranslate").getAsString().split("\n");

        String textContent = "";
        for(int i = 0; i < readingContent.length; i++){
            textContent += "<font color=\"" + ("red") + "\">" + readingContent[i] + "</font><br/>";
            textContent += "<font color=\"" + ("black") + "\">" + readingContentTranslate[i] + "</font><br/>";;
        }
        if(content.get("title").getAsString().isEmpty()) {
            ((TextView) findViewById(R.id.reading_title)).setText(slessonName);
        }else{
            ((TextView) findViewById(R.id.reading_title)).setText(
                    content.get("title").getAsString());
        }
        ((TextView)findViewById(R.id.reading_content)).setText(Html.fromHtml(textContent));
        ((TextView)findViewById(R.id.reading_hiragana)).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.reading_meaning)).setVisibility(View.GONE);
    }

    @Override
    protected void onStop() {
        reading = null;
        super.onStop();
    }
}
