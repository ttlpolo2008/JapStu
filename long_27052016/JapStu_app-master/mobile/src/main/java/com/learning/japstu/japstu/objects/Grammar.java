package com.learning.japstu.japstu.objects;

import android.support.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.learning.japstu.japstu.services.APIService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Grammar extends CourseLearning{
    // grammar attributes
    public long lGrammarID = -1;
    public class GrammarContent{
        public String sTitle = "";
        public String sSyntax = "";
        public String sMean = "";
        public String sExample = "";

        GrammarContent(){}
        GrammarContent(String title, String syntax, String meaning, String example){
            sTitle = title;
            sSyntax = syntax;
            sMean = meaning;
            sExample = example;
        }
    }
    public ArrayList<GrammarContent> listGrammar;


    private boolean hasErrorOccured = false;
    private String err = "";
    private String errMsg = "";

    public Grammar(long lessonID){
        super(lessonID);
    }
    @Override

    public boolean queryContent(){
        content = APIService.searchGrammar(lLessonID);
        if(content.isJsonArray()) {
            listGrammar = new ArrayList<GrammarContent>(0);
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
        for(int i = 0; i < arr.size(); i++){
            if(validateGrammarField(arr.get(i).getAsJsonObject())){
                listGrammar.add(new GrammarContent("",
                        arr.get(i).getAsJsonObject().get("syntax").getAsString().replaceAll("\n", " \r\n "),
                        arr.get(i).getAsJsonObject().get("explain").getAsString().replaceAll("\n", " \r\n "),
                        arr.get(i).getAsJsonObject().get("example").getAsString().replaceAll("\n"," \r\n ")));
            }
        }
        return true;
    }

    public boolean validateGrammarField(JsonObject input){
        return input.has("syntax") & input.has("explain") & input.has("example");
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
