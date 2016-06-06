package com.learning.japstu.japstu.utils;

import com.learning.japstu.japstu.services.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class User {
    // user id
    public long iUserID = -1;
    // user pwd
    public String sPwd = "";
    // user role
    public int iRole = 0;

    // user name
    public String sUsername = "";

    // history of user
    public JsonElement userHistory;

    // lastest lesson
    public JsonObject lastestLesson;

    // number learned lesson
    public int iLearnedLesson = 0;
    public double avgMark = 0.0;
    public long lNextLessonID = -1;
    public String lNextLessonName = "";


    public User(){}

    public User(String username, long userID, String nickname){
        sUsername = username;
        iUserID = userID;
        sPwd = "";
        sUsername = nickname;
        iRole = 0;
        loadAllHistory(0);
        lastestLesson = loadLatestHistory(0);
        avgMark = calculateAverageScore();
        lNextLessonID = findNextLessonID();

    }


    public User(String username, String pwd, long userID, String nickname, int role){
        sUsername = username;
        iUserID = userID;
        sPwd = pwd;
        sUsername = nickname;
        iRole = role;
    }


    public double calculateAverageScore(){

        try {
            double aveMark = APIService.selectAvgMark(iUserID);
            if (aveMark >= 0.0 && aveMark <= 100.0)
                return aveMark;
            return 0.0;
        }catch (Exception e){
            return -1.0;
        }
    }


    public void loadAllHistory(int retryCount){
        if(iUserID != -1 && !sUsername.isEmpty() && retryCount < 3){
            // TODO: load history
            try {
                userHistory = APIService.searchStudyHistoryAll(iUserID);
                if (userHistory.isJsonArray()) {
                    iLearnedLesson = userHistory.getAsJsonArray().size();
                }
            }catch (Exception e){

                loadAllHistory(retryCount + 1);
            }finally {
                if (retryCount >= 3){
                    JsonObject args = new JsonObject();
                    args.addProperty("ERROR", "CONNECTION_FAILED");
                    userHistory = (JsonElement)args;
                }
            }
        }
    }


    public JsonObject loadLatestHistory(int retryCount){
        JsonObject temp = new JsonObject();
        if(iUserID != -1 && !sUsername.isEmpty() && retryCount < 3){
            // TODO: load history
            try {
                temp = APIService.searchStudyHistoryLast(iUserID);
                if(iLearnedLesson > 0 && !temp.get("status").getAsBoolean()){
                    iLearnedLesson = iLearnedLesson - 1;
                }
            }catch (Exception e){

                temp = loadLatestHistory(retryCount + 1);
            }finally {
                if (retryCount >= 3){
                    JsonObject args = new JsonObject();
                    args.addProperty("ERROR", "CONNECTION_FAILED");
                    temp = args;
                }
            }
        }
        return temp;
    }
    public long findNextLessonID(){

        JsonObject content;
        long lessonID = Long.MIN_VALUE;
        try {
            if (userHistory.getAsJsonArray().size() == 0) {
                content = APIService.findNextLesson(null);
                lessonID = content.get("lessonId").getAsLong();
                lNextLessonName = content.get("lessonName").getAsString();
            } else {
                if (lastestLesson.has("status")) {
                    if (lastestLesson.get("status").getAsBoolean()) {
                        content = APIService.findNextLesson(lastestLesson.get("lessonId").getAsLong());
                        lessonID = content.get("lessonId").getAsLong();
                    } else {
                        if (lastestLesson.get("lessonId").isJsonNull())
                            return Long.MIN_VALUE;
                        return lastestLesson.get("lessonId").getAsLong();
                    }
                }
            }
        }catch (Exception e){
            lessonID = Long.MIN_VALUE;
        }
        return lessonID;

    }
};

