package com.learning.japstu.japstu;

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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.learning.japstu.japstu.objects.Kanji;

public class KanjiPresentationActivity extends AppCompatActivity {
    
    protected ImageButton btnPrev, btnNext;
    protected int iPath = 0;
    protected int iTotal = 0;
    
    public long sLessonID = Long.MIN_VALUE;
    public String slessonName = "";
    protected Kanji kanji;
    protected boolean[] bFinish;

    protected boolean relearn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_presentation);

        Bundle bundle = getIntent().getExtras();

        sLessonID = bundle.getLong("lessonID");
        slessonName = bundle.getString("lessonName");
        relearn = bundle.getBoolean("relearn");

        // set title
        ((TextView)findViewById(R.id.left_header_content))
                .setText(getResources().getString(R.string.title_activity_kanji_presentation));
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
                loadNextKanji(iPath);
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
                loadNextKanji(iPath);
            }
        });
        new KanjiLoadingAsyncTask().execute("");
    }
    
    public void loadNextKanji(int path){
        // TODO : change path 1 --> 2 --> 3 --> ... --> 1
        ((TextView)findViewById(R.id.tv_pagination)).setText(path + "/" + iTotal);
        ((TextView)findViewById(R.id.tv_pagination)).setTextColor(getResources()
                .getColor(R.color.items_name));

        // TODO : load next grammar to view
        loadKanjiToScreen();
        bFinish[path - 1] = true;

    }
    private class KanjiLoadingAsyncTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(KanjiPresentationActivity.this,
                    "Loading", "Loading Kanji list");
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                kanji = new Kanji(sLessonID);

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
                iTotal = kanji.listKanji.size();
                iPath = 1;
                ((TextView) findViewById(R.id.tv_pagination)).setText(iPath + "/" + iTotal);
                loadKanjiToScreen();
                bFinish = new boolean[kanji.listKanji.size()];
                for(int i = 0; i < bFinish.length; i++){
                    bFinish[i] = false;
                }
                bFinish[0] = true;
            }catch (Exception e){
                Commons.showAlertDialog("No kanji to learn.", getApplicationContext());
            }
        }
    };
    protected void loadKanjiToScreen(){

        ((TextView)findViewById(R.id.kanji_character))
                .setTextColor(getResources().getColor(R.color.title_lesson_color));
        ((TextView)findViewById(R.id.kanji_character))
                .setText(kanji.listKanji.get(iPath - 1).sWord);
        ((TextView)findViewById(R.id.kanji_meaning))
                .setTextColor(getResources().getColor(R.color.hint_color));
        ((TextView)findViewById(R.id.kanji_meaning))
                .setText(kanji.listKanji.get(iPath - 1).sMean);

        ((TextView)findViewById(R.id.onyomi_pronon))
                .setTextColor(getResources().getColor(R.color.items_name));
        ((TextView)findViewById(R.id.onyomi_pronon))
                .setText(kanji.listKanji.get(iPath - 1).sOnYomi);

        ((TextView)findViewById(R.id.onyomi_pronon_title))
                .setTextColor(getResources().getColor(R.color.items_name));

        ((TextView)findViewById(R.id.kunyomi_pronon))
                .setTextColor(getResources().getColor(R.color.items_name));
        ((TextView)findViewById(R.id.kunyomi_pronon))
                .setText(kanji.listKanji.get(iPath - 1).skunYomi);
        ((TextView)findViewById(R.id.kunyomi_pronon_title))
                .setTextColor(getResources().getColor(R.color.items_name));

        ((TextView)findViewById(R.id.kanji_example))
                .setTextColor(getResources().getColor(R.color.meaning_color));
        ((TextView)findViewById(R.id.kanji_example))
                .setText(kanji.listKanji.get(iPath - 1).sExample.replaceAll("\n"," \r\n "));
        ((TextView)findViewById(R.id.kanji_example_title))
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
        setResult(6);
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        kanji = null;
        super.onStop();
    }
}
