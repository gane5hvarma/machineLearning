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
        /*
        * Returns if attr1 can be considered as equal with attr2 while 
        * calculatig consistency.
        */
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
        * is consistent with training data. For the hypothesis to be consistent
        * there are two cases.
        * Case 1 : Training Data t is a negative example. Then, there should be
        * at least one attribute in hypothesis that is NOT EQUAL to the attr in
        * training data.
        * Case 2 : Training Data t is a positive example. Then, ALL the attrs in
        * hypothesis should be equal to the attrs in t.
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
        * general We use this when we are checking to see if an hypothesis is
        * more general than another hypothesis.
        */
        if( attr1 == attr2 || attr2 == NONE || attr1 == ALL){
            return true;
        }
        return false;
    }
    boolean isMoreGeneral(Hypothesis h){
        /*
        * Check if the current hypothesis is more general than hypothesis 'h'.
        * For one hypothesis to be more  general than another hypothesis, all of
        * its attributes should be more general than attributes of the other
        * hypothesis.
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
    boolean isMoreSpecific(Hypothesis h){
        int posCount = 0;
        for(int i = 0; i < this.attributes.length; i++){
            int attr1 = this.attributes[i];
            int attr2 = h.attributes[i];
            if(attr1 == attr2){
                continue;
            }
            if(attr1 == NONE || attr2 == ALL){
                posCount++;
                continue;
            }
            if(attr1 == ALL || attr2 == NONE){
                return false;
            }else{
                return false;
            }
        }
        if(posCount > 0){
            return true;
        }else{
            return false;
        }
    }
    void printHypothesis(){
        /*
        * An utility function to pring the hypothesis
        */
        System.out.print("<");
        for (int i = 0; i < this.attributes.length; i++ ) {
            int attr = this.attributes[i];
            if(attr == Hypothesis.ALL){
                System.out.print("ALL" +", ");    
            }else{
            System.out.print(this.attributes[i] +", ");
            }
        }
        System.out.println(">");
    }
}
