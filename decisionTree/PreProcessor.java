package decision_tree;

import java.util.Arrays;
import java.util.Comparator;

class PreProcessor{
    TrainingData[] examples;
    PreProcessor(TrainingData[] examples){
        this.examples = examples;
    }
    void sortExamples(int i){
        Arrays.sort(examples, new Comparator<TrainingData>(){
            @Override
            public int compare(TrainingData t1, TrainingData t2){
                return t1.attributes[i].compareTo(t2.attributes[i]);
            }
        });
    }
}