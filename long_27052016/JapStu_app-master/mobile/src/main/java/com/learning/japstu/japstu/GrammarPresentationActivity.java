package com.learning.japstu.japstu;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.DialogInterface;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.ImageButton;
import android.widget.TextView;

import com.learning.japstu.japstu.objects.Grammar;
import com.learning.japstu.japstu.utils.User;

public class GrammarPresentationActivity extends AppCompatActivity {

    protected int iPath = 0;
    protected int iTotal = 0;

    public long sLessonID = Long.MIN_VALUE;
    public String slessonName = "";
    protected Grammar grammar;
    protected boolean[] bFinish;
    
    protected boolean relearn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_presentation);
        Bundle bundle = getIntent().getExtras();

        sLessonID = bundle.getLong("lessonID");
        slessonName = bundle.getString("lessonName");
        relearn = bundle.getBoolean("relearn");

        // set title
        ((TextView)findViewById(R.id.left_header_content))
                .setText(getResources().getString(R.string.title_activity_grammar_presentation));
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
                if (iPath > iTotal){
                    iPath = 1;
                }
                doNextGrammarClicked(iPath);
            }
        });
        ImageButton btnPrev = (ImageButton) findViewById(R.id.btn_prev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iPath--;
                if (iPath < 1){
                    iPath = iTotal;
                }
                doNextGrammarClicked(iPath);
            }
        });
        new GrammarLoadingAsyncTask().execute();
    }

    public void doNextGrammarClicked(int path){
        // TODO : change path 1 --> 2 --> 3 --> ... --> 1
        ((TextView)findViewById(R.id.tv_pagination)).setText(path + "/" + iTotal);
        ((TextView)findViewById(R.id.tv_pagination)).setTextColor(getResources()
                .getColor(R.color.items_name));
        // index begin from 0 but path from 1
        bFinish[path - 1] = true;
        // TODO : load next grammar to view
        loadGrammarToScreen();
    }

    private class GrammarLoadingAsyncTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(GrammarPresentationActivity.this,
                    "Loading", "Loading grammars.");
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                grammar = new Grammar(sLessonID);

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
            // after loading grammar content, show on screen
            try {
                iTotal = grammar.listGrammar.size();
                iPath = 1;
                ((TextView) findViewById(R.id.tv_pagination)).setText(iPath + "/" + iTotal);
                loadGrammarToScreen();
                // set finish on learing to false all
                bFinish = new boolean[grammar.listGrammar.size()];
                for(int i = 0; i < bFinish.length; i++){
                    bFinish[i] = false;
                }
                // first one is learned.
                bFinish[0] = true;
            }catch (Exception e){
                Commons.showAlertDialog("No grammar to learn", getApplicationContext());
            }

        }
    };

    protected void showConfirmDialog(){
        boolean bAgree = true;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        setResult(0);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.cancel();
                        break;
                }
            }
        };
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Cancel this course ?")
                .setPositiveButton("Agree", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener);
        alertDialogBuilder.setMessage("Course has not been finished yet. \n Next course won't be opened.");
        alertDialogBuilder.show();
    }

    protected void loadGrammarToScreen(){
        ((TextView)findViewById(R.id.grammar_structure_content))
                .setText(grammar.listGrammar.get(iPath - 1).sSyntax);
        ((TextView)findViewById(R.id.grammar_explanation_content))
                .setText(grammar.listGrammar.get(iPath - 1).sMean.replaceAll("\n"," \r\n "));
        ((TextView)findViewById(R.id.grammar_example_content))
                .setText(grammar.listGrammar.get(iPath - 1).sExample.replaceAll("\n"," \r\n "));

        ((TextView)findViewById(R.id.grammar_structure_content))
                .setTextColor(getResources().getColor(R.color.meaning_color));
        ((TextView)findViewById(R.id.grammar_structure_content_title))
                .setTextColor(getResources().getColor(R.color.items_name));
        ((TextView)findViewById(R.id.grammar_explanation_content))
                .setTextColor(getResources().getColor(R.color.meaning_color));
        ((TextView)findViewById(R.id.grammar_explanation_content_title))
                .setTextColor(getResources().getColor(R.color.items_name));
        ((TextView)findViewById(R.id.grammar_example_content))
                .setTextColor(getResources().getColor(R.color.meaning_color));
        ((TextView)findViewById(R.id.grammar_example_content_title))
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
        setResult(2);
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        grammar = null;
        super.onStop();
    }

}
