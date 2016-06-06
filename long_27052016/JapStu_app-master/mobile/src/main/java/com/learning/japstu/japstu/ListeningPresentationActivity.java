package com.learning.japstu.japstu;

import android.annotation.TargetApi;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.learning.japstu.japstu.objects.Listening;

import java.io.File;
import java.io.IOException;

public class ListeningPresentationActivity extends AppCompatActivity {

    protected String title = "";
    protected MediaPlayer player;
    protected boolean bExistedTrack = false;
    
    public long sLessonID = Long.MIN_VALUE;
    public String slessonName = "";
    protected Listening listening;
    protected boolean[] bFinish;
    protected byte[] soundtrack;
    
    protected boolean relearn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_presentation);

        Bundle bundle = getIntent().getExtras();

        sLessonID = bundle.getLong("lessonID");
        slessonName = bundle.getString("lessonName");
        relearn = bundle.getBoolean("relearn");

        bFinish = new boolean[2];
        bFinish[0] = false; // for bunkei
        bFinish[1] = false; // for reibun


        title = getResources()
                .getString(R.string.title_activity_listening_presentation);
        ((TextView)findViewById(R.id.left_header_content)).setText(title);
        ((TextView)findViewById(R.id.left_header_content))
                .setTextColor(getResources().getColor(R.color.title_page_color));
        ((TextView)findViewById(R.id.right_header_content))
                .setText(slessonName);
        ((TextView)findViewById(R.id.right_header_content))
                .setTextColor(getResources().getColor(R.color.title_page_color));

        // TODO : query content from database and set to view
        new ListeningLoadingAsyncTask().execute("");

        ((TextView)findViewById(R.id.bunkei_title))
                .setTextColor(getResources().getColor(R.color.title_lesson_color));
        ((TextView)findViewById(R.id.reibun_title))
                .setTextColor(getResources().getColor(R.color.title_lesson_color));
        ((TextView)findViewById(R.id.bunkei_content_phg))
                .setTextColor(getResources().getColor(R.color.meaning_color));
        ((TextView)findViewById(R.id.reibun_content_phg))
                .setTextColor(getResources().getColor(R.color.meaning_color));
    }

    public void doSoundReibun(View v) throws IOException {
        Commons.playSoundTrack(listening.reibunContent.baSoundTrack, this);
        findViewById(R.id.bunkei_content).setVisibility(View.GONE);
        findViewById(R.id.reibun_content).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.reibun_content_phg)).setText(listening.reibunContent.sContent);
        bFinish[0] = true;
    }

    public void doSoundBunkei(View v) throws IOException {
        Commons.playSoundTrack(listening.bunkeiContent.baSoundTrack, this);
        findViewById(R.id.reibun_content).setVisibility(View.GONE);
        findViewById(R.id.bunkei_content).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.bunkei_content_phg)).setText(listening.bunkeiContent.sContent);
        bFinish[1] = true;
    }

    private class ListeningLoadingAsyncTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(ListeningPresentationActivity.this,
                    "Loading", "Loading content.");
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                listening = new Listening(sLessonID);

            }catch (Exception e){
                System.out.println(e.getMessage().toString());
                //finish();
            }
            return null;
        }

        protected void onPostExecute(Void unused){
            dialog.dismiss();
        }
    }

    protected void showConfirmDialog(){
        boolean bAgree = true;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        setResult(0);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        dialog.cancel();
                        break;
                }
            }
        };
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Cancel this course ?")
                .setPositiveButton("Agree", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener);
        alertDialogBuilder.setMessage("Course has not been finished yet.\nNext course won't be opened.");
        alertDialogBuilder.show();
    }

    @Override
    public void onBackPressed() {
        if(relearn){
            setResult(0);
            super.onBackPressed();
            return;
        }
        for(boolean b : bFinish){
            if(!b){
                showConfirmDialog();
                return;
            }
        }
        setResult(4);
        if(player != null){
            player.stop();
            player.release();
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        listening = null;
        soundtrack = null;
        super.onStop();
    }

}
