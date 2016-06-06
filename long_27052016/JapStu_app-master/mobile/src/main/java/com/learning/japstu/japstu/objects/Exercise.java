package com.learning.japstu.japstu.objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.learning.japstu.japstu.services.APIService;

import java.util.ArrayList;

public class Exercise extends CourseLearning{

    public ArrayList<Question> lQuestion;
    public class Question{
        public String mQuestionContent = "";
        public String mQuestionType = ""; 
        public String mAnswerType = ""; 
        public int iMark = 0;
        public int iCountDownTime = 0;
        public boolean[] bResult; 
        public String[] sAnswersContent; 
        public Question(String questionType, String answerType, String questionContent, int mark, int countdown,
                        String answer1, String answer2, String answer3, String answer4, String answer5,
                        String result){
            
            mQuestionContent = questionContent;
            mQuestionType = questionType;
            mAnswerType = answerType;
            iMark = mark;
            iCountDownTime = countdown;
            char[] chrArray = result.toCharArray();
            bResult = new boolean[chrArray.length];
            for(int i = 0; i < chrArray.length; i++){
                bResult[i] =  (chrArray[i] == '1');
            }
            sAnswersContent = new String[5];
            sAnswersContent[0] = answer1;
            sAnswersContent[1] = answer2;
            sAnswersContent[2] = answer3;
            sAnswersContent[3] = answer4;
            sAnswersContent[4] = answer5;
        }
    }
    public boolean bIsTest = false;

    public Exercise(long lessonID){super(lessonID);}
    public Exercise(long lessonID, boolean isTest){
        bIsTest = isTest;
        lLessonID = lessonID;
        queryContent();
    }


    private boolean hasErrorOccured = false;
    private String err = "";
    private String errMsg = "";
    public boolean queryContent(){
        if(!bIsTest)
            content = APIService.searchExercise(lLessonID);
        else
            content = APIService.createTest(lLessonID, new Long(5), new Long(5),
                    new Long(5), new Long(5), new Long(5),
                    new Long(5));
        if(content.isJsonArray()) {
            lQuestion = new ArrayList<Question>(0);
            if( fillData()) {
                hasErrorOccured = true;
                return true;
            }
        }
        hasErrorOccured = false;
        return false;
    }

    public boolean fillData(){
        JsonArray arr  = content.getAsJsonArray();
        for(int i = 0; i < arr.size(); i++){
            JsonObject input = arr.get(i).getAsJsonObject();
            if(valiadateQuestionContent(input)){
                Question quest = new Question(input.get("questionType").getAsString(),
                                              input.get("answerType").getAsString(),
                                              input.get("questionContent").getAsString(),
                                              input.get("mark").getAsInt(),
                                              input.get("time").getAsInt(),
                                              input.get("answer1").getAsString(),
                                              input.get("answer2").getAsString(),
                                              input.get("answer3").getAsString(),
                                              input.get("answer4").getAsString(),
                                              input.get("answer5").getAsString(),
                                              input.get("answerChoose").getAsString());
                lQuestion.add(quest);
            }
        }
        return true;
    }
    
    public boolean valiadateQuestionContent(JsonObject input){
        boolean result = true;
        result &= (input.has("questionType") &
                  input.has("answerType") & 
                  input.has("questionContent") & 
                  input.has("mark") & 
                  input.has("time") & 
                  input.has("answer1") & 
                  input.has("answer2") & 
                  input.has("answer3") & 
                  input.has("answer4") & 
                  input.has("answer5") & 
                  input.has("answerChoose"));
        if(result){
            result &= (input.get("mark").getAsDouble() > 0);
            result &= (input.get("time").getAsInt() > 0);
            result &= (input.get("answerChoose").getAsString().toCharArray().length == 5);
        }
        return result;
    }
}
