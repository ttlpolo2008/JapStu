package com.learning.japstu.japstu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import com.google.gson.JsonObject;
import com.learning.japstu.japstu.services.APIService;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewLessonPageActivity extends AppCompatActivity {

    public final static char VOCABULARY = '1';
    public final static char GRAMMAR = '2';
    public final static char READING = '3';
    public final static char LISTENING = '4';
    public final static char CONVERSATION = '5';
    public final static char KANJI = '6';

    static final int FINISH_COURSE_STATUS = 0;
    static final int UNSUCCESSFUL = 0;
    static final int FINISH_VOCABULARY = 1;
    static final int FINISH_READING = 3;
    static final int FINISH_LISTENING = 4;
    static final int FINISH_KANJI = 6;
    static final int FINISH_CONVERSATION = 5;
    static final int FINISH_GRAMMAR = 2;
    static final int FINISH_EXERCISE = 7;

    // TODO: declare parameters
    long iLessonID = -1L;
    String sLessonName = "";
    char[] iCourseStatus;
    int iScores = -1;
    char[] iSequenceStatus;
    protected String current = "";
    

    protected long userID = -1L;
    protected String crrDate = "";
    protected String endDate = "";
    protected long examMark = 0;

    private boolean bComplete = false;
    private boolean[] bFinishCourse = new boolean[7];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lesson_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        if(!bundle.getString("lessonID").toString().equals("null"))
            iLessonID = Long.parseLong(bundle.getString("lessonID"));
        else
            iLessonID = -1;
        String crStatus = bundle.getString("courseStatus");
        crrDate = bundle.getString("startDate");
        userID = Long.parseLong(bundle.getString("userID"));
        // set invisible to all
        setInvisibleAllItems();
        if(crStatus.equals("000000")) {
            iCourseStatus = Commons.chooseDirection4Learning();
            iSequenceStatus = ("000000").toCharArray();
            for (int i = 0; i < 7; i++) {
                bFinishCourse[i] = false;
            }
        }
        else {
            iCourseStatus = crStatus.toCharArray();
            iSequenceStatus = iCourseStatus.clone();
            // check status of each button
            for (int i = 0; i < 6; i++) {
                if(iSequenceStatus[i] != '0') {
                    setVisibleLessonCourseButton(iCourseStatus[i]);
                    bFinishCourse[iSequenceStatus[i] - 49] = true;
                }else{
                    iCourseStatus[i] = Commons.chooseNextPart(iCourseStatus, i);
                    bFinishCourse[iCourseStatus[i] - 49] = false;
                }
            }
            bFinishCourse[6] = false;
        }
        

        ((TextView) findViewById(R.id.new_lesson_title))
                .setTextColor(getResources().getColor(R.color.title_page_color));
        bComplete = false;
        sLessonName = bundle.getString("lessonName");
        ((TextView) findViewById(R.id.lbl_lesson_name)).setText(sLessonName);
        openNextCourse();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(bComplete){
            setVisibleLessonCourseButton('0');
        }
    }

    @Override
    public void onBackPressed() {
        new RegisterNewLessonToServerAsyncTask().execute();
    }

    public void doGrammarCourseLearning(View v){
        Intent intent = new Intent(this, GrammarPresentationActivity.class);
        intent.putExtra("lessonID", iLessonID);
        intent.putExtra("lessonName", sLessonName);
        intent.putExtra("relearn", bFinishCourse[FINISH_GRAMMAR - 1]);
        startActivityForResult(intent, FINISH_COURSE_STATUS);
    }

    public void doVocabularyCourseLearning(View v){
        Intent intent = new Intent(this, VocabularyPresentationActivity.class);
        intent.putExtra("lessonID", iLessonID);
        intent.putExtra("lessonName", sLessonName);
        intent.putExtra("relearn", bFinishCourse[FINISH_VOCABULARY - 1]);
        startActivityForResult(intent, FINISH_COURSE_STATUS);
    }

    public void doConversationCourseLearning(View v){
        Intent intent = new Intent(this, ConversationPresentationActivity.class);
        intent.putExtra("lessonID", iLessonID);
        intent.putExtra("lessonName", sLessonName);
        intent.putExtra("relearn", bFinishCourse[FINISH_CONVERSATION - 1]);
        startActivityForResult(intent, FINISH_COURSE_STATUS);
    }
    /**
     * Open listening course learning page.
     * @param v
     */
    public void doListeningCourseLearning(View v){
        Intent intent = new Intent(this, ListeningPresentationActivity.class);
        intent.putExtra("lessonID", iLessonID);
        intent.putExtra("lessonName", sLessonName);
        intent.putExtra("relearn", bFinishCourse[FINISH_LISTENING - 1]);
        startActivityForResult(intent, FINISH_COURSE_STATUS);
    }/**
     * Open Speaking course learning page.
     * @param v
     */
    public void doSpeakingCourseLearning(View v){
        Intent intent = new Intent(this, SpeakingPresentationActivity.class);
        intent.putExtra("lessonID", iLessonID);
        intent.putExtra("lessonName", sLessonName);
        intent.putExtra("relearn", bFinishCourse[FINISH_READING - 1]);
        startActivityForResult(intent, FINISH_COURSE_STATUS);
    }/**
     * Open kanji course learning page.
     * @param v
     */
    public void doKanjiCourseLearning(View v){
        Intent intent = new Intent(this, KanjiPresentationActivity.class);
        intent.putExtra("lessonID", iLessonID);
        intent.putExtra("lessonName", sLessonName);
        intent.putExtra("relearn", bFinishCourse[FINISH_KANJI - 1]);
        startActivityForResult(intent, FINISH_COURSE_STATUS);
    }/**
     * Open Exercise course learning page.
     * @param v
     */
    public void doExerciseCourseLearning(View v){
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra("lessonID", iLessonID);
        intent.putExtra("lessonName", sLessonName);
        intent.putExtra("relearn", bFinishCourse[FINISH_EXERCISE - 1]);
        startActivityForResult(intent, FINISH_COURSE_STATUS);
    }

    // set visible and invisible to image button
    public void setVisibleLessonCourseButton(char caseButton){
        switch (caseButton){
            case VOCABULARY:
                ((ImageButton)findViewById(R.id.btn_vocabulary)).setClickable(true);
                ((ImageButton)findViewById(R.id.btn_vocabulary))
                        .setBackgroundResource(R.drawable.ibtn_vocabulary);
                current = "Vocabulary";
                break;
            case GRAMMAR:
                current = "Grammar";
                ((ImageButton)findViewById(R.id.btn_grammar)).setClickable(true);
                ((ImageButton)findViewById(R.id.btn_grammar))
                        .setBackgroundResource(R.drawable.ibtn_grammar);
                break;
            case KANJI:
                current = "Kanji";
                ((ImageButton)findViewById(R.id.btn_kanji)).setClickable(true);
                ((ImageButton)findViewById(R.id.btn_kanji))
                        .setBackgroundResource(R.drawable.ibtn_kanji);
                break;
            case READING:
                current = "Reading";
                ((ImageButton)findViewById(R.id.btn_reading)).setClickable(true);
                ((ImageButton)findViewById(R.id.btn_reading))
                        .setBackgroundResource(R.drawable.ibtn_reading);
                break;
            case CONVERSATION:
                current = "Conversation";
                ((ImageButton)findViewById(R.id.btn_conversation)).setClickable(true);
                ((ImageButton)findViewById(R.id.btn_conversation))
                        .setBackgroundResource(R.drawable.ibtn_conversation);
                break;
            case LISTENING:
                current = "Listening";
                ((ImageButton)findViewById(R.id.btn_listening)).setClickable(true);
                ((ImageButton)findViewById(R.id.btn_listening))
                        .setBackgroundResource(R.drawable.ibtn_listening);
                break;
            default:
                if(bComplete)
                    ((ImageButton)findViewById(R.id.btn_exercise)).setVisibility(View.VISIBLE);
                else {

                    for(int i = 0; i < 6; i++) {
                        if(!bFinishCourse[i]){
                            ((ImageButton) findViewById(R.id.btn_exercise)).setVisibility(View.GONE);
                            break;
                        }else{
                            ((ImageButton)findViewById(R.id.btn_exercise)).setVisibility(View.VISIBLE);
                        }
                    }

                }
                break;
        }
    }

    protected void setInvisibleAllItems(){
        ((ImageButton)findViewById(R.id.btn_vocabulary)).setClickable(false);
        ((ImageButton)findViewById(R.id.btn_vocabulary))
                .setBackgroundResource(R.drawable.btn_unavailable);
        ((ImageButton)findViewById(R.id.btn_grammar)).setClickable(false);
        ((ImageButton)findViewById(R.id.btn_grammar))
                .setBackgroundResource(R.drawable.btn_unavailable);
        ((ImageButton)findViewById(R.id.btn_kanji)).setClickable(false);
        ((ImageButton)findViewById(R.id.btn_kanji))
                .setBackgroundResource(R.drawable.btn_unavailable);
        ((ImageButton)findViewById(R.id.btn_reading)).setClickable(false);
        ((ImageButton)findViewById(R.id.btn_reading))
                .setBackgroundResource(R.drawable.btn_unavailable);
        ((ImageButton)findViewById(R.id.btn_conversation)).setClickable(false);
        ((ImageButton)findViewById(R.id.btn_conversation))
                .setBackgroundResource(R.drawable.btn_unavailable);
        ((ImageButton)findViewById(R.id.btn_listening)).setClickable(false);
        ((ImageButton)findViewById(R.id.btn_listening))
                .setBackgroundResource(R.drawable.btn_unavailable);
        ((ImageButton)findViewById(R.id.btn_exercise)).setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean goNext = false;
        // if learning completely, go next if lastest course finished.
        // if not, dont need do anything else.
        if( requestCode == FINISH_COURSE_STATUS && resultCode != UNSUCCESSFUL){
            switch (resultCode){
                case FINISH_VOCABULARY : // id = 0
                    if(!bFinishCourse[0])
                        goNext = true;
                    bFinishCourse[0] = true;
                    break;
                case FINISH_READING: // id = 2;
                    if(!bFinishCourse[2])
                        goNext = true;
                    bFinishCourse[2] = true;
                    break;
                case FINISH_LISTENING: // id = 3;
                    if(!bFinishCourse[3])
                        goNext = true;
                    bFinishCourse[3] = true;
                    break;
                case FINISH_GRAMMAR: // id = 1;
                    if(!bFinishCourse[1])
                        goNext = true;
                    bFinishCourse[1] = true;
                    break;
                case FINISH_CONVERSATION: // id = 4;
                    if(!bFinishCourse[4])
                        goNext = true;
                    bFinishCourse[4] = true;
                    break;
                case FINISH_KANJI: // id = 5;
                    if(!bFinishCourse[5])
                        goNext = true;
                    bFinishCourse[5] = true;
                    break;
                case FINISH_EXERCISE: // id = 6;
                    if(!bFinishCourse[6]) {
                        bComplete = true;
                        goNext = true;
                    }
                    break;
                default:
                    break;
            }
            if(bComplete){
                // end activity
                String dialog_message = "Finish learning.\n Press back 2 times to end learning.";
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(dialog_message)
                        .setTitle("Warning");

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        dialog.dismiss();
                    }
                });
                dialog.show();
                
            }
            //set end date when user finish learning each course
            Date cal = (Date) Calendar.getInstance().getTime();
            endDate = cal.toLocaleString();
            
            // open next course on screen.
            if(goNext)
                openNextCourse();
        }

    }

    // open next course to user
    protected boolean openNextCourse(){
        String hightLight = "Finish learning ";
        if(!bComplete) {
            for (int i = 0; i < 6; i++) {
                if(iCourseStatus[i] == '0'){
                    return false;
                }
                if(iSequenceStatus[i] == '0'){
                    iSequenceStatus[i] = iCourseStatus[i];
                    setVisibleLessonCourseButton(iCourseStatus[i]);
                    hightLight += current;
                    hightLight += " to open next course.";
                    ((TextView)findViewById(R.id.current_course)).setText(hightLight);
                    return true;
                }
            }
        }
        // when all courses have been opened.
        // open exercise .
        setVisibleLessonCourseButton('0');
        
        hightLight = "Finish exercise to complete lesson.";
        ((TextView)findViewById(R.id.current_course)).setText(hightLight);
        return true;
    }
    
    private class RegisterNewLessonToServerAsyncTask extends AsyncTask<String, Void, Void> {
        // dialog for showing progress
        ProgressDialog pg;
        JsonObject content;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pg = ProgressDialog.show(NewLessonPageActivity.this,
                    "Submit Lesson Course", "Updating user informations");

        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                // call API to register New Lesson
                boolean stt = true;
                for(int i = 0; i < 7; i++){
                    stt &= bFinishCourse[i];
                }
                DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
                Date startDate = dateFormat.parse(crrDate);
                endDate = dateFormat.format(new Date()); // dung` de chi lay ngay thang nam, bo~ gio`
                Date endDates = dateFormat.parse(endDate); // chi save lai ngay` hoc ko save lai gio` hoc.
                for(int i = 0; i < 6; i++){
                    if(iSequenceStatus[i] != '0' && !bFinishCourse[iSequenceStatus[i] - 49]){  // truong hop chua hoc xong course thi remove ra khoi sequence truoc khi update database
                        iSequenceStatus[i] = '0';
                    }
                }
                String courseStt = String.valueOf(iSequenceStatus);
                if(APIService.registerUsersLesson(userID, iLessonID,
                        startDate, endDates,
                        stt, courseStt, examMark)){
                    mHandler.sendEmptyMessage(1);
                }else{
                    mHandler.sendEmptyMessage(2);
                }
            }catch (Exception e){
                mHandler.sendEmptyMessage(0);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pg.dismiss();
        }
    }
    
    // handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:  // successfully update database
                    System.out.println("SYSTEM OUT : register successfully" );
                    Toast.makeText(NewLessonPageActivity.this,
                            "New status has been registered to system.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 2:  // unsuccessfully update database
                    Toast.makeText(NewLessonPageActivity.this,
                            "New status hasnot been registered to system.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 0:  // disconnect from server
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
}
