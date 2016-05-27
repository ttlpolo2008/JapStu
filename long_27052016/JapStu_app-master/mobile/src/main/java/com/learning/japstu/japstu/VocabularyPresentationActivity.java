package com.learning.japstu.japstu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.learning.japstu.japstu.objects.Vocabulary;

import java.io.IOException;

public class VocabularyPresentationActivity extends AppCompatActivity {

    protected ImageButton btnPrev, btnNext;
    protected int iPath = 0;
    protected int iTotal = 0;
    protected boolean bExistedTrack = false;

    public long sLessonID = Long.MIN_VALUE;
    public String slessonName = "";
    protected Vocabulary vocabulary;
    protected boolean[] bFinish;
    protected byte[] soundtrack;
    
    protected boolean relearn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_presentation);

        Bundle bundle = getIntent().getExtras();

        sLessonID = bundle.getLong("lessonID");
        slessonName = bundle.getString("lessonName");
        relearn = bundle.getBoolean("relearn");

        this.setResult(0);
        // set title
        ((TextView)findViewById(R.id.left_header_content))
                .setText(getResources().getString(R.string.title_activity_vocabulary_presentation));
        ((TextView)findViewById(R.id.left_header_content))
                .setTextColor(getResources().getColor(R.color.title_page_color));
        ((TextView)findViewById(R.id.right_header_content))
                .setText(slessonName);
        ((TextView)findViewById(R.id.right_header_content))
                .setTextColor(getResources().getColor(R.color.title_page_color));

        ImageButton btnNext = (ImageButton) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iPath++;
                if (iPath > iTotal) {
                    iPath = 1;
                }
                loadNextVocabulary(iPath);
            }
        });
        ImageButton btnPrev = (ImageButton) findViewById(R.id.btn_prev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iPath--;
                if (iPath < 1) {
                    iPath = iTotal;
                }
                loadNextVocabulary(iPath);
            }
        });

        new VocabularyLoadingAsyncTask().execute();
    }

    private class VocabularyLoadingAsyncTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(VocabularyPresentationActivity.this,
                    "Loading", "Loading vocabularies");
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                vocabulary = new Vocabulary(sLessonID);

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
                iTotal = vocabulary.listVocabulary.size();
                iPath = 1;
                ((TextView) findViewById(R.id.tv_pagination)).setText(iPath + "/" + iTotal);
                loadVocabularyToScreen();

                bFinish = new boolean[vocabulary.listVocabulary.size()];
                for(int i = 0; i < bFinish.length; i++){
                    bFinish[i] = false;
                }

                bFinish[0] = true;
            }catch (Exception e){
                // no data found
                Commons.showAlertDialog("No vocabulary to learn.", getApplicationContext());
            }

        }
    };

    public void doSoundVocabulary(View v) throws IOException {
        Commons.playSoundTrack(soundtrack, this);
    }

    public void loadNextVocabulary(int path){
        // TODO : change path 1 --> 2 --> 3 --> ... --> 1
        ((TextView)findViewById(R.id.tv_pagination)).setText(path + "/" + iTotal);
        ((TextView)findViewById(R.id.tv_pagination)).setTextColor(getResources()
                .getColor(R.color.items_name));

        // index begin from 0 but path from 1
        bFinish[path - 1] = true;
        // TODO : load next grammar to view
        loadVocabularyToScreen();

        // Load structure
        ((TextView)findViewById(R.id.voc_character))
                .setTextColor(getResources().getColor(R.color.title_lesson_color));
        // Load meaning
        ((TextView)findViewById(R.id.voc_meaning))
                .setTextColor(getResources().getColor(R.color.hint_color));

        ((TextView)findViewById(R.id.voc_kanji_title))
                .setTextColor(getResources().getColor(R.color.items_name));
        ((TextView)findViewById(R.id.voc_kanji))
                .setTextColor(getResources().getColor(R.color.items_name));

        // Load example
        ((TextView)findViewById(R.id.voc_example_title))
                .setTextColor(getResources().getColor(R.color.meaning_color));
        ((TextView)findViewById(R.id.voc_example))
                .setTextColor(getResources().getColor(R.color.items_name));
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

    protected void loadVocabularyToScreen(){
        ((TextView)findViewById(R.id.voc_character))
                .setText(vocabulary.listVocabulary.get(iPath - 1).sVoc);
        // Load meaning
        ((TextView)findViewById(R.id.voc_meaning))
                .setText(vocabulary.listVocabulary.get(iPath - 1).sMean.replaceAll("\n", " \r\n "));
        // Load kanji
        ((TextView)findViewById(R.id.voc_kanji))
                .setText(vocabulary.listVocabulary.get(iPath - 1).sKanji);
        // load example
        ((TextView)findViewById(R.id.voc_example))
                .setText(vocabulary.listVocabulary.get(iPath - 1).sExample.replaceAll("\n"," \r\n "));

        // load sound track

        soundtrack = vocabulary.listVocabulary.get(iPath - 1).baSoundTrack;
        if(soundtrack.length <= 1){
            ((ImageButton)findViewById(R.id.voc_play_sound_btn)).setVisibility(View.GONE);
        }else{
            ((ImageButton)findViewById(R.id.voc_play_sound_btn)).setVisibility(View.VISIBLE);
        }

        // Load structure
        ((TextView)findViewById(R.id.voc_character))
                .setTextColor(getResources().getColor(R.color.title_lesson_color));
        // Load meaning
        ((TextView)findViewById(R.id.voc_meaning))
                .setTextColor(getResources().getColor(R.color.hint_color));

        ((TextView)findViewById(R.id.voc_kanji_title))
                .setTextColor(getResources().getColor(R.color.items_name));
        ((TextView)findViewById(R.id.voc_kanji))
                .setTextColor(getResources().getColor(R.color.items_name));

        // Load example
        ((TextView)findViewById(R.id.voc_example_title))
                .setTextColor(getResources().getColor(R.color.meaning_color));
        ((TextView)findViewById(R.id.voc_example))
                .setTextColor(getResources().getColor(R.color.items_name));
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
        setResult(1);
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        vocabulary = null;
        soundtrack = null;
        super.onStop();
    }
}
