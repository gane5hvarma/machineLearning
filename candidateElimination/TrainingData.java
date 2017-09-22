package candidate;

import java.util.*;

class TrainingData {
    int[] attributes;
    String name;
    public static final int TYPE = 16;
    TrainingData(int[] attributes){
        this.attributes = attributes;
    }
    void setName(String name){
        this.name = name;
    }
    void oneVsall(int type){
        if (this.attributes[TYPE] == type) {
            this.attributes[TYPE] = 1;
        } else{
            this.attributes[TYPE] = 0;
        }
    }
}