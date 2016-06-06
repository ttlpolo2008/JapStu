package com.learning.japstu.japstu.objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.learning.japstu.japstu.services.APIService;

import java.util.ArrayList;


public class Kanji extends CourseLearning {
    // vocabulary attributes

    public class KanjiContent{
        public String sWord = "";
        public String sOnYomi = "";
        public String skunYomi = "";
        public String sMean = "";
        public String sExample = "";

        KanjiContent(){}
        KanjiContent(String word, String meaning, String onYomi, String kunYomi, String example){
            sWord = word;
            sOnYomi = onYomi;
            skunYomi = kunYomi;
            sMean = meaning;
            sExample = example;
        }
    }

    private boolean hasErrorOccured = false;
    private String err = "";
    private String errMsg = "";

    public ArrayList<KanjiContent> listKanji;
    public Kanji(long lessonID){super(lessonID);}


    @Override

    public boolean queryContent(){
        content = APIService.searchKanji(lLessonID);
        if(content.isJsonArray()) {
            listKanji = new ArrayList<KanjiContent>(0);
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
            for (int i = 0; i < arr.size(); i++) {
                if (validateKanjiField(arr.get(i).getAsJsonObject())) {
                    String word = arr.get(i).getAsJsonObject().get("word").getAsString();
                    String meaning = arr.get(i).getAsJsonObject().get("meaning").getAsString();
                    String onYomi = arr.get(i).getAsJsonObject().get("onYomi").getAsString();
                    String kunYomi = arr.get(i).getAsJsonObject().get("kunYomi").getAsString();
                    String explain = arr.get(i).getAsJsonObject().get("explain").getAsString();
                    if(word.isEmpty())
                        word = "-";
                    if(meaning.isEmpty())
                        meaning = "-";
                    if(onYomi.isEmpty())
                        onYomi = "-";
                    if(kunYomi.isEmpty())
                        kunYomi = "-";
                    if(explain.isEmpty())
                        explain = "-";
                    listKanji.add(new KanjiContent(word, meaning, onYomi, kunYomi, explain));
                }
            }
        }catch (Exception e){
            hasErrorOccured = true;
            System.out.println(e.getMessage());
        }
        return true;
    }

    public boolean validateKanjiField(JsonObject input){
        return input.has("word") & input.has("meaning") & input.has("kunYomi")
                & input.has("explain") & input.has("onYomi");
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
