package decision_tree;

import java.util.*;
import java.util.Arrays;

class TrainingData implements Comparable<TrainingData>{
    String[] attributes;
    TrainingData(String[] attributes){
        this.attributes = attributes;
    }
    @Override
    public int compareTo(TrainingData t){
        return 0;
    }
}