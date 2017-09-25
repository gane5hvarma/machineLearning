package decision_tree;

import java.util.*;

class Attribute{
    String name;
    String[] acceptableValues;
    int index;
    Attribute(int i){
        this.index = i;
        this.name = TrainingData.features.get(i);
        this.acceptableValues = TrainingData.getAcceptedValues(i);
    }
    boolean equalTo(Attribute a){
        if(a.name.equalsIgnoreCase(this.name) && 
            a.acceptableValues.equals(this.acceptableValues)){
            return true;
        }
        return false;
    }
    boolean isAcceptableValue(String value){
        for (String val: this.acceptableValues ) {
            if(val.equalsIgnoreCase(value)){
                return true;
            }
        }
        return false;
    }
}