package com.learning.japstu.japstu.objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.learning.japstu.japstu.services.APIService;

import java.util.ArrayList;


public class Vocabulary extends CourseLearning{
    // vocabulary attributes

    public class VocabularyContent{
        public String sVoc = "";
        public byte[] baSoundTrack;
        public String sMean = "";
        public String sKanji = "";
        public String sExample = "";

        VocabularyContent(){}
        VocabularyContent(String voc, String meaning, String kanji, String example, byte[] sountrack){
            sVoc = voc;
            sKanji = kanji;
            sMean = meaning;
            baSoundTrack = sountrack.clone();
            sExample = example;
        }
    }
    public ArrayList<VocabularyContent> listVocabulary;


    private boolean hasErrorOccured = false;
    private String err = "";
    private String errMsg = "";

    public Vocabulary(long lessonID){
        super(lessonID);
    }
    @Override

    public boolean queryContent(){
        content = APIService.searchVocabulary(lLessonID);
        if(content.isJsonArray()) {
            listVocabulary = new ArrayList<VocabularyContent>(0);
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
                if (validateVocabularyField(arr.get(i).getAsJsonObject())) {
                    String word = arr.get(i).getAsJsonObject().get("word").getAsString();
                    String meaning = arr.get(i).getAsJsonObject().get("meaning").getAsString();
                    String kanji = arr.get(i).getAsJsonObject().get("kanji").getAsString();
                    String explain = arr.get(i).getAsJsonObject().get("explain").getAsString();
                    if(word.isEmpty())
                        word = "-";
                    if(meaning.isEmpty())
                        meaning = "-";
                    if(kanji.isEmpty())
                        kanji = "-";
                    if(explain.isEmpty())
                        explain = "-";
                    byte[] sndtrack;
                    try {
//                        JsonObject obj = arr.get(i).getAsJsonObject();
                        Gson gson = new Gson();
                        sndtrack = gson.fromJson(
                                        arr.get(i).getAsJsonObject().get("pronunceFileStream"),
                                        byte[].class);
                        if (sndtrack.length <= 2)
                            sndtrack = new byte[0];

                    }catch (NullPointerException exc){
                        sndtrack = new byte[0];
                    }
                    listVocabulary.add(new VocabularyContent(word, meaning, kanji,
                            explain, sndtrack));
                }
            }
        }catch (Exception e){
            hasErrorOccured = true;
            System.out.println(e.getMessage());
        }
        return true;
    }

    public boolean validateVocabularyField(JsonObject input){
        return input.has("word") & input.has("meaning")
                & input.has("explain") & input.has("kanji");
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
