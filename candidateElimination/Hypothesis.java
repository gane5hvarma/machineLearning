package candidate;

import java.util.*;

class Hypothesis{
    public static final int ALL = 999;
    public static final int NONE = 111;
    public static final int HAIR = 0;
    public static final int FEATHERS = 1;
    public static final int EGGS = 2;
    public static final int MILK = 3;
    public static final int AIRBORNE = 4;
    public static final int AQUATIC = 5;
    public static final int PREDATOR = 6;
    public static final int TOOTHED = 7;
    public static final int BACKBONE = 8;
    public static final int BREATHES = 9;
    public static final int VENOMOUS = 10;
    public static final int FINS = 11;
    public static final int TAIL = 12;
    public static final int DOMESTIC = 13;
    public static final int CATSIZE = 14;
    public static final int LEGS = 15;
    public static final int TYPE = 16;
    String name;
    int hair, feathers, eggs, milk, airborne, aquatic, predator, toothed;
    int backbone, breathes, venomous, fins, tail, domestic, catsize;
    int legs, type;
    int[] attributes;
    Hypothesis(int[] attributes){
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
    boolean isConsistent(Hypothesis h){ // h is the training data
        int count = 0;
        for(int i = 0; i < h.attributes.length; i++){
            if(isEqual(this.attributes[i], h.attributes[i])){
                count++;
            }
        }
        if(count == h.attributes.length){
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
        for(int i = 0; i < this.attributes.length - 1; i++){
            if(!moreGeneralAttribute(this.attributes[i], h.attributes[i])){
                return false;
            }
        }
        return true;
    }
    Hypothesis minimalSpecialization(Hypothesis h){
        /*
        * In the case that the curreng hypothesis is not consistent with the
        * TRAINING DATA h, find a minimalSpecialization of it that does.
        * Note that, we need to call this in case of a negative encounter in CE
        */
        
    }
}
