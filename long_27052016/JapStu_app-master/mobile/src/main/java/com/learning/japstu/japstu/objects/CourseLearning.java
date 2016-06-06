package com.learning.japstu.japstu.objects;

import com.google.gson.JsonElement;

abstract class CourseLearning {
    // String type used for mapping
    //  1: Vocabulary
    //  2: Grammar
    //  3: Reading
    //  4: Listening
    //  5: Conversation
    //  6: Kanji
    public String sType = "";
    public long lCourseID = -1;
    public long lLessonID = -1;
    public JsonElement content;
    CourseLearning(){};
    CourseLearning(String type){sType = type;}
    CourseLearning(long lessonID){
        lLessonID = lessonID;
        queryContent();
    }

    abstract boolean queryContent();

    abstract boolean fillData();

}
