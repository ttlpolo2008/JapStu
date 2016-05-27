package com.learning.japstu.japstu.objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.learning.japstu.japstu.services.APIService;

import java.util.ArrayList;


public class Reading extends CourseLearning {

    // reading attributes
    public String sTitle = "";
    public String sContent = "";
    public String sContentPron = "";
    public String scontentTranslate = "";
    public String[] arrContent;
    public String[] arrContentTrans;
    // error for logging
    private boolean hasErrorOccured = false;
    private String err = "";
    private String errMsg = "";

    public Reading(long lessonID){super(lessonID);}

    @Override

    public boolean queryContent(){
        content = APIService.searchReading(lLessonID);
        if(content.isJsonObject() || content.isJsonArray()) {
            if( fillData()) {
                hasErrorOccured = false;
                return true;
            }
        }
        hasErrorOccured = true;
        return false;
    }

    @Override
    public boolean fillData(){
        JsonObject data;
        if(content.isJsonArray())
            data = content.getAsJsonArray().get(0).getAsJsonObject();
        else {
            if (content.isJsonObject())
                data = content.getAsJsonObject();
            else {
                data = new JsonObject();
                data.addProperty("title", "No Reading");
                data.addProperty("content", "");
                data.addProperty("contentPron", "");
                data.addProperty("contentTranslate", "");
            }
        }
        try {
            if (validateReadingField(data)) {
                if(data.has("title")){
                    this.sTitle = data.get("title").toString();
                }
                this.sContent = data.get("content").toString();
                this.sContentPron = data.get("contentPron").toString();
                this.scontentTranslate = data.get("contentTranslate").toString();
                if(this.sContent.isEmpty())
                    this.sContent = "-";
//                if(sContentPron.isEmpty())
//                    sContentPron = "-";
                if(this.scontentTranslate.isEmpty())
                    scontentTranslate = "-";

                if(this.sContent.split("\n").length > 0){
                    this.arrContent = this.sContent.split("\n").clone();
                }
                if(this.scontentTranslate.split("\n").length > 0){
                    this.arrContentTrans = this.scontentTranslate.split("\n").clone();
                }
            }
        }catch (Exception e){
            hasErrorOccured = true;
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean validateReadingField(JsonObject input){
        boolean result = input.has("content") & input.has("contentPron") & input.has("contentTranslate");
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
