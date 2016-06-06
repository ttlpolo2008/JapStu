package com.learning.japstu.japstu.objects;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.learning.japstu.japstu.services.APIService;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class Listening extends CourseLearning{
    // vocabulary attributes

    public class ListeningContent{
        public String sListeningType = "";
        public byte[] baSoundTrack;
        public String sContent = "";


        ListeningContent(String listeningType, String content, byte[] soundtrack){
            sListeningType = listeningType;
            baSoundTrack = soundtrack.clone();
            sContent = content;
        }
    }


    private boolean hasErrorOccured = false;
    private String err = "";
    private String errMsg = "";

    public ListeningContent bunkeiContent;
    public ListeningContent reibunContent;

    public Listening(long lessonID){super(lessonID);}

    @Override

    public boolean queryContent(){
        content = APIService.searchListening(lLessonID);
        if(content.isJsonArray()) {
            if( fillData()) {
                hasErrorOccured = true;
                return true;
            }
        }
        hasErrorOccured = false;
        return false;
    }

    @Override
    public boolean fillData(){
        JsonArray arr  = content.getAsJsonArray();
        try {
            Gson gson = new Gson();
            for (int i = 0; i < arr.size(); i++) {
                if (validateListeningField(arr.get(i).getAsJsonObject())) {
                    String listeningType = arr.get(i).getAsJsonObject().get("listeningType").getAsString();
                    String content = arr.get(i).getAsJsonObject().get("content").getAsString();
                    byte[] soundtrack = gson.fromJson(
                            arr.get(i).getAsJsonObject().get("contentFileStream"),
                            byte[].class);
                    if(listeningType.isEmpty())
                        listeningType = "-";
                    if(content.isEmpty())
                        content = "-";
                    if(soundtrack.length == 0)
                        soundtrack = new byte[0];
                    if(listeningType.equals("1")){
                        reibunContent = new ListeningContent(listeningType, content, soundtrack);

                    }else{
                        bunkeiContent = new ListeningContent(listeningType, content, soundtrack);
                    }
                }
            }
        }catch (Exception e){
            hasErrorOccured = true;
            System.out.println(e.getMessage());
        }
        return true;
    }

    public boolean validateListeningField(JsonObject input){
        boolean result = input.has("listeningType") & input.has("contentFileStream") & input.has("content");
        if(result){
            result &= (input.get("listeningType").getAsString().equals("1") ||
                    input.get("listeningType").getAsString().equals("2"));
            if(result)
                result &= (input.get("contentFileStream").toString().length() > 0) &
                        (input.get("content").toString().length() > 0);
        }
        return result;
    }

    public String getErrorMsg(){
        if(hasErrorOccured){
            if(content.isJsonObject() && content.getAsJsonObject().has("ERROR002")){
                return "ERROR002 : " + content.getAsJsonObject().get("ERROR002").getAsString();
            }else{
                return "ERROR000 : canot connect to server.";
            }
        }
        return "NO_ERROR";
    }

}
