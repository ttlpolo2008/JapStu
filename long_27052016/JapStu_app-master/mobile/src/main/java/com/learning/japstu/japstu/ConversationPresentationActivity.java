package com.learning.japstu.japstu;

import android.media.MediaDataSource;
import android.media.MediaPlayer;
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
import com.learning.japstu.japstu.objects.Conversation;

import java.io.IOException;

public class ConversationPresentationActivity extends AppCompatActivity {

    protected String title = "";
    protected MediaPlayer player;
    protected boolean bExistedTrack = false;
    
    public long sLessonID = Long.MIN_VALUE;
    public String slessonName = "";
    protected Conversation conversation;
    protected byte[] soundtrack;
    

    protected boolean relearn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_presentation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

        sLessonID = bundle.getLong("lessonID");
        slessonName = bundle.getString("lessonName");
        relearn = bundle.getBoolean("relearn");
        

        title = getResources()
                .getString(R.string.title_activity_conversation_presentation);
        ((TextView)findViewById(R.id.left_header_content)).setText(title);
        ((TextView)findViewById(R.id.left_header_content))
                .setTextColor(getResources().getColor(R.color.title_page_color));
        ((TextView)findViewById(R.id.right_header_content))
                .setText(slessonName);
        ((TextView)findViewById(R.id.right_header_content))
                .setTextColor(getResources().getColor(R.color.title_page_color));


        ((TextView)findViewById(R.id.lesson_title_conversation))
                .setTextColor(getResources().getColor(R.color.title_lesson_color));

        new ConversationLoadingAsyncTask().execute("");
    }

    public void doSoundConversation(View v) throws IOException {
        if(conversation.soundtrack.length >= 2){
            Commons.playSoundTrack(conversation.soundtrack, this);
        }
    }

    private class ConversationLoadingAsyncTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(ConversationPresentationActivity.this,
                    "Loading", "Loading content.");
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                conversation = new Conversation(sLessonID);

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
                title = conversation.title;

                ((TextView)findViewById(R.id.lesson_title_conversation)).setText(title);
                String textContent = "";
                for(int i = 0; i < conversation.sConversation.length; i++){
                    textContent += "<font color=\"" + ("blue") + "\">" + conversation.sConversation[i] + "</font><br/>";
                }

                ((TextView)findViewById(R.id.conversation_content)).setText(Html.fromHtml(textContent));

            }catch (Exception e){

                System.out.println("" + e.toString());
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
        if(player != null){
            player.stop();
            player.release();
        }
        setResult(5);
        super.onBackPressed();
    }


    @Override
    protected void onStop() {
        conversation = null;
        soundtrack = null;
        super.onStop();
    }
}
