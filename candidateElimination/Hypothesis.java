package candidate;

import java.util.*;

class Hypothesis{
    public static final int ALL = 999;
    public static final int NONE = 111;
    public static final int TYPE = 16;
    static int[] GENERAL = {ALL,ALL,ALL,ALL,ALL,ALL,ALL,ALL,ALL, ALL,ALL,ALL,
                            ALL,ALL,ALL,ALL};
    static int[] SPECIFIC = {NONE,NONE,NONE,NONE,NONE,NONE,NONE,NONE,NONE,NONE,
                             NONE,NONE,NONE,NONE,NONE,NONE};
    int[] attributes;
    Hypothesis(int[] attributes){
        this.attributes = attributes;
    }
    int[] acceptableValues(int i){
        /*
        * Returns all acceptable values that an attribute may have.
        */
        int[] accept;
        if(i < 0 || i > 16){
            accept = new int[]{};
        }
        else if(i == 13){
            accept = new int[]{0,2,4,5,6,8};
        }else{
            accept = new int[]{0,1};
        }
        return accept;
    }
    boolean isEqual(int attr1, int attr2){
        if((attr1 == NONE && attr2 != NONE)||(attr2 == NONE && attr1 !=NONE)){
            return false;
        }
        else if(attr1 == ALL || attr2 == ALL){
            return true;
        }
        else if(attr1 == attr2){
            return true;
        }
        else {
            return false;
        }
    } 
    boolean isConsistent(TrainingData t){
        /*
        * here t is the training data and we are checking if current hyopthesis
        * is consistent with training data.
        */
        int count = 0;
        for(int i = 0; i < this.attributes.length; i++){
            if(isEqual(this.attributes[i], t.attributes[i])){
                count++;
            }
        }
        if(count == this.attributes.length && t.getClassNumber() == 1){
            return true;
        }
        if(count != this.attributes.length && t.getClassNumber() == 0){
            return true;
        }
        return false;
    }
    boolean moreGeneralAttribute(int attr1, int attr2){
        /*
        * Check if attr1 is more general than attr2. More general means:
        * if attr2 is null | attr1 == attr2 | attr1 == ALL, attr1 is more
        * general
        */
        if( attr1 == attr2 || attr2 == NONE || attr1 == ALL){
            return true;
        }
        return false;
    }
    boolean isMoreGeneral(Hypothesis h){
        /*
        * Check if the current hypothesis is more general than hypothesis 'h'.
        * if in course of time it is found that we need a function to compare 
        * two different hypothesis, make this a static method with two arguments
        */
        int posCount = 0;
        for(int i = 0; i < this.attributes.length; i++){
            if(moreGeneralAttribute(this.attributes[i], h.attributes[i])){
                posCount++;
            }
        }
        if (posCount == h.attributes.length) {
            return true;
        }
        return false;
    }


    void printHypothesis(){
        System.out.print("<");
        for (int i = 0; i < this.attributes.length; i++ ) {
            System.out.print(this.attributes[i] +", ");
        }
        System.out.println(">");
    }
    // public static void main(String[] args) {
    //     int[] attr1 = {999, 0, 999, 0, 999, 999, 999, 999, 999, 999, 999, 0, 0, 999, 1, 999};
    //     int[] attr2 ={0, 0, 999, 0, 0, 999, 1, 1, 1, 999, 999, 0, 0, 1, 0, 0};
    //     Hypothesis h1 = new Hypothesis(attr1);
    //     Hypothesis h2 = new Hypothesis(attr2);
    //     System.out.println(h1.isMoreGeneral(h2));
    // }
}
