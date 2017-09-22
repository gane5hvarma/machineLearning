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
    public void setName(String name){
        this.name = name;
    }
    public void oneVsall(int type){
        if (this.attributes[TYPE] == type) {
            this.attributes[TYPE] = 1;
        } else{
            this.attributes[TYPE] = 0;
        }
    }
    public boolean isEqual(int attr1, int attr2){
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
    public boolean isConsistent(Hypothesis h){ // h is the training data
        int count = 0;
        for(int i=0;i<h.attributes.length;i++){
            if(isEqual(this.attributes[i], h.attributes[i])){
                count++;
            }
        }
        if(count == h.attributes.length){
            return true;
        }
        return false;
    }
    public boolean isMoreGeneral(Hypothesis h){
        /*
        Check if the current hypothesis is more general than hypothesis 'h'.
        */
        if(h.attributes[TYPE] == 1 && this.attributes[TYPE] == 0){
            return false;
        } else{
            // new logic
            return true;
        }
    }
}
