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
        if(count == this.attributes.length && t.attributes[TYPE] == 1){
            return true;
        }
        if(count != this.attributes.length && t.attributes[TYPE] == 0){
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
        if(h.attributes[TYPE] == 1 && this.attributes[TYPE] == 0){
            return false;
        }
        for(int i = 0; i < this.attributes.length; i++){
            if(!moreGeneralAttribute(this.attributes[i], h.attributes[i])){
                return false;
            }
        }
        return true;
    }
}
