package candidate;

import java.util.*;

class Hypothesis{
    final int ALL = 999;
    final int NONE = 111;
    final int HAIR = 0;
    final int FEATHERS = 1;
    final int EGGS = 2;
    final int MILK = 3;
    final int AIRBORNE = 4;
    final int AQUATIC = 5;
    final int PREDATOR = 6;
    final int TOOTHED = 7;
    final int BACKBONE = 8;
    final int BREATHES = 9;
    final int VENOMOUS = 10;
    final int FINS = 11;
    final int TAIL = 12;
    final int DOMESTIC = 13;
    final int CATSIZE = 14;
    final int LEGS = 15;
    final int TYPE = 16;
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
    public void isMoreGeneral(Hypothesis h){

    }
}