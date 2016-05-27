package com.learning.japstu.japstu;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.learning.japstu.japstu.objects.Exercise;

public class ExerciseActivity extends AppCompatActivity {
    static final int ONE_RIGHT_ANSWER = 1;
    static final int MANY_RIGHT_ANSWERS = 2;
    static final int WRITING_ANSWER = 3;
    static final int LISTENING_QUESTION = 4;
    protected String title = "";
    protected int iPath = 1;
    protected int iTotal = 50;
    protected int totalMark = 0;
    protected int countdownTick = 45000; // 45s

    public long sLessonID = Long.MIN_VALUE;
    public String slessonName = "";
    protected Exercise exercise;
    protected boolean[] bFinish;
    

    protected boolean relearn;
    
    protected CountDownTimer countDownTimer = new CountDownTimer(45000, 1000) {

        public void onTick(long millisUntilFinished) {
            String time = (millisUntilFinished/1000) + "s";
            ((TextView)findViewById(R.id.countdown_timer)).setText(time);
        }

        public void onFinish() {
            // TODO : show result and go next question.
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        

        Bundle bundle = getIntent().getExtras();

        sLessonID = bundle.getLong("lessonID");
        slessonName = bundle.getString("lessonName");
        relearn = bundle.getBoolean("relearn");
        
        // set title
        title = getResources()
                .getString(R.string.title_activity_exercise);
        ((TextView)findViewById(R.id.left_header_content)).setText(title);
        ((TextView)findViewById(R.id.left_header_content))
                .setTextColor(getResources().getColor(R.color.title_page_color));
        ((TextView)findViewById(R.id.right_header_content))
                .setText(slessonName);
        ((TextView)findViewById(R.id.right_header_content))
                .setTextColor(getResources().getColor(R.color.title_page_color));


        ((TextView)findViewById(R.id.counted_question))
                .setTextColor(getResources().getColor(R.color.meaning_color));
        ((TextView)findViewById(R.id.countdown_timer))
                .setTextColor(getResources().getColor(R.color.meaning_color));
        ((Button)findViewById(R.id.btn_show_result)).setTextColor(Color.WHITE);
        ((Button)findViewById(R.id.btn_next_question)).setTextColor(Color.WHITE);

        new ExerciseLoadingAsyncTask().execute();
    }

    private class ExerciseLoadingAsyncTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(ExerciseActivity.this,
                    "Loading", "Loading exercise.");
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                exercise = new Exercise(sLessonID);

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
                iPath = 1;
                iTotal = exercise.lQuestion.size();
                bFinish = new boolean[ exercise.lQuestion.size()];
                for(int i = 0; i < bFinish.length; i++){
                    bFinish[i] = false;
                }
                if(iPath > iTotal)
                    finish();
                else
                    doDisplayQuestion(iPath);

            }catch (Exception e){

                System.out.println("" + e.toString());
            }

        }
    };

    public void checkExerciseQuestionAndShowResult(int ipath){
        boolean correct = false;
        int iTypeQuestion = Integer.parseInt(exercise.lQuestion.get(ipath - 1).mAnswerType);
        String result = "";
        ColorStateList colostate = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_enabled}
                },
                new int[] {

                        Color.BLACK
                        ,Color.RED

                }
        );
        ColorStateList colostateQuestion = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled}, //disabled
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {

                        Color.BLACK //disabled
                        ,Color.GREEN //enabled

                }
        );
        switch (iTypeQuestion) {
            case MANY_RIGHT_ANSWERS:
                correct = ((RadioButton) findViewById(R.id.answer_type_2_1)).isChecked()^
                        exercise.lQuestion.get(ipath - 1).bResult[0];
                if(!correct && exercise.lQuestion.get(ipath - 1).bResult[0]){
                    // show result
                    ((RadioButton) findViewById(R.id.answer_type_2_1)).setTextColor(colostate);
                    ((RadioButton) findViewById(R.id.answer_type_2_1)).setChecked(true);
                }else{
                    ((RadioButton) findViewById(R.id.answer_type_2_1)).setTextColor(colostateQuestion);
                }
                correct &= ((RadioButton) findViewById(R.id.answer_type_2_2)).isChecked()^
                        exercise.lQuestion.get(ipath - 1).bResult[1];
                if(!correct && exercise.lQuestion.get(ipath - 1).bResult[0]){
                    // show result
                    ((RadioButton) findViewById(R.id.answer_type_2_2)).setTextColor(colostate);
                    ((RadioButton) findViewById(R.id.answer_type_2_2)).setChecked(true);
                }else
                    ((RadioButton) findViewById(R.id.answer_type_2_2)).setTextColor(colostateQuestion);
                correct &= ((RadioButton) findViewById(R.id.answer_type_2_3)).isChecked()^
                        exercise.lQuestion.get(ipath - 1).bResult[2];
                if(!correct && exercise.lQuestion.get(ipath - 1).bResult[0]){
                    // show result
                    ((RadioButton) findViewById(R.id.answer_type_2_3)).setTextColor(colostate);
                    ((RadioButton) findViewById(R.id.answer_type_2_3)).setChecked(true);
                }else
                    ((RadioButton) findViewById(R.id.answer_type_2_3)).setTextColor(colostateQuestion);
                correct &= ((RadioButton) findViewById(R.id.answer_type_2_4)).isChecked()^
                        exercise.lQuestion.get(ipath - 1).bResult[3];
                if(!correct && exercise.lQuestion.get(ipath - 1).bResult[0]){
                    // show result
                    ((RadioButton) findViewById(R.id.answer_type_2_4)).setTextColor(colostate);
                    ((RadioButton) findViewById(R.id.answer_type_2_4)).setChecked(true);
                }else{
                    ((RadioButton) findViewById(R.id.answer_type_2_4)).setTextColor(colostateQuestion);
                }
                break;
            case LISTENING_QUESTION:
                break;
            case WRITING_ANSWER:
                result = ((EditText)findViewById(R.id.answer_type_3)).getText().toString();
                correct = result.equals(exercise.lQuestion.get(ipath - 1).sAnswersContent[0]);
                break;
            case ONE_RIGHT_ANSWER:
                correct = ((RadioButton) findViewById(R.id.answer_type_1_1)).isChecked()^
                        exercise.lQuestion.get(ipath - 1).bResult[0];
                if(!correct && exercise.lQuestion.get(ipath - 1).bResult[0]){
                    // show result
                    ((RadioButton) findViewById(R.id.answer_type_1_1)).setTextColor(colostate);
                    ((RadioButton) findViewById(R.id.answer_type_1_1)).setChecked(true);
                }else{
                    ((RadioButton) findViewById(R.id.answer_type_1_1)).setTextColor(colostateQuestion);
                }
                correct &= ((RadioButton) findViewById(R.id.answer_type_1_2)).isChecked()^
                        exercise.lQuestion.get(ipath - 1).bResult[0];
                if(!correct && exercise.lQuestion.get(ipath - 1).bResult[0]){
                    // show result
                    ((RadioButton) findViewById(R.id.answer_type_1_2)).setTextColor(colostate);
                    ((RadioButton) findViewById(R.id.answer_type_1_2)).setChecked(true);
                }else{
                    ((RadioButton) findViewById(R.id.answer_type_1_2)).setTextColor(colostateQuestion);
                }
                correct &= ((RadioButton) findViewById(R.id.answer_type_1_3)).isChecked()^
                        exercise.lQuestion.get(ipath - 1).bResult[0];
                if(!correct && exercise.lQuestion.get(ipath - 1).bResult[0]){
                    // show result
                    ((RadioButton) findViewById(R.id.answer_type_1_3)).setTextColor(colostate);
                    ((RadioButton) findViewById(R.id.answer_type_1_3)).setChecked(true);
                }else{
                    ((RadioButton) findViewById(R.id.answer_type_1_3)).setTextColor(colostateQuestion);
                }
                correct &= ((RadioButton) findViewById(R.id.answer_type_1_4)).isChecked()^
                        exercise.lQuestion.get(ipath - 1).bResult[0];
                if(!correct && exercise.lQuestion.get(ipath - 1).bResult[0]){
                    // show result
                    ((RadioButton) findViewById(R.id.answer_type_1_4)).setTextColor(colostate);
                    ((RadioButton) findViewById(R.id.answer_type_1_4)).setChecked(true);
                }else {
                    ((RadioButton) findViewById(R.id.answer_type_1_4)).setTextColor(colostateQuestion);
                }
                break;
            default:
                break;
        }
        if(correct){
            totalMark += exercise.lQuestion.get(ipath - 1).iMark;
        }
    }

    public void doDisplayQuestion(final int ipath){
        bFinish[ipath - 1] = true;
        ((Button)findViewById(R.id.btn_show_result)).setEnabled(true);
        ((TextView)findViewById(R.id.counted_question)).setText("Question : " + ipath + "/" + iTotal);
        int iTypeQuestion = Integer.parseInt(exercise.lQuestion.get(ipath - 1).mAnswerType);
        countdownTick = exercise.lQuestion.get(ipath - 1).iCountDownTime * 1000;
        if(countDownTimer != null) {

            countDownTimer = null;
        }
        ColorStateList colorState = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{

                        Color.DKGRAY
                        , Color.GREEN,
                }
        );
        if(ipath < iTotal)
            ((Button)findViewById(R.id.btn_next_question)).setText("NEXT");
        else
            ((Button)findViewById(R.id.btn_next_question)).setText("FINISH");

        countDownTimer = new CountDownTimer(countdownTick, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String time = (millisUntilFinished/1000) + "s";
                ((TextView)findViewById(R.id.countdown_timer)).setText(time);
            }


            @Override
            public void onFinish() {
                checkExerciseQuestionAndShowResult(ipath);
                doCallNextQuestion(findViewById(R.id.btn_next_question));
            }
        };
        countDownTimer.start();
        switch (iTypeQuestion){
            case MANY_RIGHT_ANSWERS:
                findViewById(R.id.question_field_1).setVisibility(View.GONE);
                findViewById(R.id.question_field_2).setVisibility(View.VISIBLE);
                findViewById(R.id.question_field_3).setVisibility(View.GONE);
                ((TextView)findViewById(R.id.question_type_2_content)).setText(
                        exercise.lQuestion.get(ipath - 1).mQuestionContent
                );
                ((RadioButton)findViewById(R.id.answer_type_2_1)).setText(
                        exercise.lQuestion.get(ipath - 1).sAnswersContent[0]
                );
                ((RadioButton)findViewById(R.id.answer_type_2_1)).setChecked(false);
                ((RadioButton)findViewById(R.id.answer_type_2_2)).setChecked(false);
                ((RadioButton)findViewById(R.id.answer_type_2_3)).setChecked(false);
                ((RadioButton)findViewById(R.id.answer_type_2_4)).setChecked(false);
                ((RadioButton)findViewById(R.id.answer_type_2_1)).setTextColor(colorState);
                ((RadioButton)findViewById(R.id.answer_type_2_2)).setTextColor(colorState);
                ((RadioButton)findViewById(R.id.answer_type_2_3)).setTextColor(colorState);
                ((RadioButton)findViewById(R.id.answer_type_2_4)).setTextColor(colorState);
                ((RadioButton) findViewById(R.id.answer_type_2_2)).setText(
                        exercise.lQuestion.get(ipath - 1).sAnswersContent[1]
                );
                ((RadioButton)findViewById(R.id.answer_type_2_3)).setText(
                        exercise.lQuestion.get(ipath - 1).sAnswersContent[2]
                );
                ((RadioButton)findViewById(R.id.answer_type_2_4)).setText(
                        exercise.lQuestion.get(ipath - 1).sAnswersContent[3]
                );
                break;
            case LISTENING_QUESTION:
                break;
            case WRITING_ANSWER:
                findViewById(R.id.question_field_1).setVisibility(View.GONE);
                findViewById(R.id.question_field_2).setVisibility(View.GONE);
                findViewById(R.id.question_field_3).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.question_type_3_content)).setText(
                        exercise.lQuestion.get(ipath - 1).mQuestionContent
                );

                break;
            case ONE_RIGHT_ANSWER:
                findViewById(R.id.question_field_1).setVisibility(View.VISIBLE);
                findViewById(R.id.question_field_2).setVisibility(View.GONE);
                findViewById(R.id.question_field_3).setVisibility(View.GONE);
                ((RadioButton)findViewById(R.id.answer_type_1_1)).setTextColor(colorState);
                ((RadioButton)findViewById(R.id.answer_type_1_2)).setTextColor(colorState);
                ((RadioButton)findViewById(R.id.answer_type_1_3)).setTextColor(colorState);
                ((RadioButton)findViewById(R.id.answer_type_1_4)).setTextColor(colorState);
                ((RadioButton)findViewById(R.id.answer_type_1_1)).setChecked(false);
                ((RadioButton)findViewById(R.id.answer_type_1_2)).setChecked(false);
                ((RadioButton)findViewById(R.id.answer_type_1_3)).setChecked(false);
                ((RadioButton)findViewById(R.id.answer_type_1_4)).setChecked(false);
                ((TextView)findViewById(R.id.question_type_1_content)).setText(
                        exercise.lQuestion.get(ipath - 1).mQuestionContent
                );
                ((RadioButton)findViewById(R.id.answer_type_1_1)).setText(
                        exercise.lQuestion.get(ipath - 1).sAnswersContent[0]
                );
                ((RadioButton)findViewById(R.id.answer_type_1_2)).setText(
                        exercise.lQuestion.get(ipath - 1).sAnswersContent[1]
                );
                ((RadioButton)findViewById(R.id.answer_type_1_3)).setText(
                        exercise.lQuestion.get(ipath - 1).sAnswersContent[2]
                );
                ((RadioButton)findViewById(R.id.answer_type_1_4)).setText(
                        exercise.lQuestion.get(ipath - 1).sAnswersContent[3]
                );
                break;
                default:
                    break;
        }
    }

    public void doCallNextQuestion(View v){

        checkExerciseQuestionAndShowResult(iPath);

        iPath++;
        if(iPath > iTotal){
            // end test
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            dialog.cancel();
                            break;
                    }
                }
            };
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Congratgulation")
                    .setPositiveButton("Go Back", dialogClickListener);
            alertDialogBuilder.setMessage("Your mark is " + totalMark);
            alertDialogBuilder.show();
        }else{

            doDisplayQuestion(iPath);
        }
    }
    
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
        alertDialogBuilder.setTitle("Cancel this exercise ?")
                .setPositiveButton("Agree", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener);
        alertDialogBuilder.setMessage("You could not finish lesson yet.");
        alertDialogBuilder.show();
    }
    
    @Override
    public void onBackPressed() {

        int passscore = 0;
        for(int i = 0; i < exercise.lQuestion.size(); i++){
            passscore += exercise.lQuestion.get(i).iMark;
        }
        passscore = passscore/2;
        if (totalMark >= passscore)
            setResult(0);
        else
            setResult(7);
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
        super.onBackPressed();
    }

    public void doShowResult(View v){
        checkExerciseQuestionAndShowResult(iPath);
        v.setEnabled(false);
    }

    @Override
    protected void onStop() {
        exercise = null;
        super.onStop();
    }

}
