package com.learning.japstu.japstu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.learning.japstu.japstu.NewLessonPageActivity;
import com.learning.japstu.japstu.utils.User;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Handler;

public class TopPageActivity extends Activity {

    public boolean bHasHistory = false;
    public User mUser;

    private String username = "";
    private String nickname = "";
    private String userID = "";

    // handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // update text views
            if(mUser.iLearnedLesson > 0){
                ((TextView)findViewById(R.id.learned_lesson)).setText(""+mUser.iLearnedLesson);
                DecimalFormat df = new DecimalFormat("#.##");
                ((TextView)findViewById(R.id.average_Scores)).setText(""+df.format(mUser.avgMark));
            }else{
                TextView tv_learned_lesson = (TextView)findViewById(R.id.learned_lesson);
                tv_learned_lesson.setText("N/A");
                TextView tv_average_Scores = (TextView)findViewById(R.id.average_Scores);
                tv_average_Scores.setText("N/A");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // TODO: get information for requesting to server
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        nickname = bundle.getString("nickname");
        userID = bundle.getString("userId");
        ((TextView)findViewById(R.id.username_information)).setText(nickname);
        callWebService();
    }

    private class TopPageAsyncTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(TopPageActivity.this,
                    "Loading", "Loading user informations");
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                long uid = Long.parseLong(userID);
                mUser = new User(username, uid, nickname);
                if(mUser.iLearnedLesson > 0 || mUser.lastestLesson.has("status")){
                    if(!mUser.lastestLesson.get("status").getAsBoolean())
                        bHasHistory = true;
                    else
                        bHasHistory = false;
                }
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

    public void doNewLessonClicked(View v){
        new TopPageAsyncTask().execute();
        if( mUser.lNextLessonID == Long.MIN_VALUE || mUser.lNextLessonID < 0){
            String dialog_message = "Not found lesson need to be learnt.";
            showDialog(dialog_message);
            return;
        }
        Intent intent = new Intent(this, NewLessonPageActivity.class);
        intent.putExtra("lessonID", "" + mUser.lNextLessonID);
        intent.putExtra("lessonName", mUser.lNextLessonName);
        if (bHasHistory) {
            System.out.println(mUser.lNextLessonID);
            intent.putExtra("courseStatus", mUser.lastestLesson.get("courseStatus")
                    .getAsString());
            intent.putExtra("startDate", mUser.lastestLesson.get("startDate").getAsString());

        }else{
            intent.putExtra("courseStatus", "000000");
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
            Date cal = new Date();
            intent.putExtra("startDate", dateFormat.format(cal));
        }
        intent.putExtra("userID", userID);
        startActivity(intent);
    }
    public void doReviewTestClicked(View v){
        new TopPageAsyncTask().execute();
        if(mUser.iLearnedLesson > 0){
            Intent intent = new Intent(this, ExerciseActivity.class);
            intent.putExtra("lessonID", mUser.lNextLessonID);
            startActivity(intent);

        }else{
            String dialog_message = "No review test for user.\nPlease study first.";
            showDialog(dialog_message);
        }
    }
    public void doStudyAdviceClicked(View v){
        String dialog_message = "This function has not been supported yet.";
        showDialog(dialog_message);
    }

    public void doHistoryClicked(View v){
        new TopPageAsyncTask().execute();
        if(mUser.iLearnedLesson > 0){
            Intent intent = new Intent(this, HIstoryActivity.class);
            intent.putExtra("username",username);
            intent.putExtra("nickname",nickname);
            intent.putExtra("userId",userID);
            startActivity(intent);
        }else{
            String dialog_message = "No history of user.\nPlease study first.";
            showDialog(dialog_message);
        }
    }
    private void callWebService(){
        new TopPageAsyncTask().execute();
    }

    protected void showDialog(String msg){
        String dialog_message = msg;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(dialog_message)
                .setTitle("Warning");

        AlertDialog dialog = builder.create();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            dialog.dismiss();
            }
        });
        dialog.show();
    }
}
