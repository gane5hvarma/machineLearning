package decision_tree;

import java.util.*;

class Attribute{
    /*
    * The class attribute represents each feature/attribute of the training 
    * data. There are 14 attributes, and each feature has an index and a name.
    * The name of each attribute and corresponding indices are given below.
     * age - 0
     * workclass - 1
     * fnlwgt - 2
     * education - 3
     * education-num - 4
     * marital-status - 5
     * occupation - 6
     * relationship - 7
     * race - 8
     * sex -9
     * capital-gain - 10
     * capital-loss - 11
     * hours-per-week - 12
     * native-country - 13
    */
    String name;
    int index;
    Attribute(int i){
        this.index = i;
        this.name = TrainingData.features.get(i);
    }
}