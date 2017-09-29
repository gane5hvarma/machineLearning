package candidate;

import java.util.*;

class TrainingData {
    /*
    * Each object of TrainingData represents one training example. The attributes
    * property of this object corresponds to one line of zoo.data file.
    */
    int[] attributes;
    String name;
    TrainingData(int[] attributes){
        this.attributes = attributes;
    }
    void setName(String name){
        this.name = name;
    }
    void oneVsall(int type){
        /*
        * Performs one vs all.
        */
        if (this.attributes[this.attributes.length - 1] == type) {
            this.attributes[this.attributes.length - 1] = 1;
        } else{
            this.attributes[this.attributes.length - 1] = 0;
        }
    }
    int getClassNumber(){
        /*
        * returns which class the training Data belongs to.
        */
        return this.attributes[this.attributes.length - 1];
    }
    void printTrainingData(){
        /*
        * An utility function to print Training Data.
        */
        System.out.print("<");
        for (int i = 0; i < this.attributes.length; i++ ) {
            System.out.print(this.attributes[i]+", ");
        }
        System.out.println(">");
    }
}