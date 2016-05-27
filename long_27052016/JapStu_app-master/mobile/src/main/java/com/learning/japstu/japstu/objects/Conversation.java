package com.learning.japstu.japstu.objects;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.learning.japstu.japstu.services.APIService;

import java.io.File;
import java.io.FileOutputStream;

public class Conversation extends CourseLearning{
    public String[] sConversation;
    public byte[] soundtrack;
    public String title;

    private String err = "";
    private String errMsg = "";
    private boolean hasError = false;

    public Conversation(long lessonID){super(lessonID);}
    @Override

    public boolean queryContent(){
        content = APIService.searchConversation(lLessonID);
        if(content.isJsonArray()) {
           if( fillData())
                return true;
        }
        return false;
    }

    @Override
    public boolean fillData(){
        try {
            JsonObject context = new JsonObject();
            if(content.isJsonArray()) {
                context = content.getAsJsonArray().get(0).getAsJsonObject();
            }
            if (content.isJsonObject()){
                context = content.getAsJsonObject();
            }
            if (!context.isJsonNull()) {
                if(context.has("title")){
                    title = context.get("title").getAsString();
                }
                if (context.has("content") && context.has("contentFileStream")) {
                    Gson gson = new Gson();
                    sConversation = context.get("content").getAsString().split("\n");
                    soundtrack = gson.fromJson(context.get("contentFileStream"), byte[].class);

                } else {
                    if(context.has("ERR002")){
                        err = "ERR002";
                        errMsg =  context.get("ERR002").getAsString();
                    }else{
                        err = "ERR999";
                        errMsg =  "Not existed data on server";
                    }
                    hasError = true;
                    return false;
                }
            }
        }catch (Exception e){
            hasError = true;
            err = "ERROR000";
            errMsg =  e.getMessage();
        }finally {
            content = null;
        }
        return true;
    }

    public String logErrorMsg(){
        if(hasError){
            return err + " : " + errMsg;
        }else{
            return "";
        }
    }
}
