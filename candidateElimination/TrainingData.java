package candidate;

import java.util.*;

class TrainingData {
    int[] attributes;
    String name;
    TrainingData(int[] attributes){
        this.attributes = attributes;
    }
    void setName(String name){
        this.name = name;
    }
    void oneVsall(int type){
        if (this.attributes[this.attributes.length - 1] == type) {
            this.attributes[this.attributes.length - 1] = 1;
        } else{
            this.attributes[this.attributes.length - 1] = 0;
        }
    }
    int getClassNumber(){
        return this.attributes[this.attributes.length - 1];
    }
}