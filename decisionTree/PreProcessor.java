package decision_tree;

import java.util.Arrays;
import java.util.Comparator;
import java.util.*;

class PreProcessor{
    TrainingData[] examples;
    int[] continuousValues = {0, 2, 4, 10, 11, 12};
    double[] thresholds = new double[6]; 
    double Entropy;

    PreProcessor(TrainingData[] examples){
        this.examples = examples;
        getEntropy();
    }
    void sortExamples(int i){
        Arrays.sort(examples, new Comparator<TrainingData>(){
            @Override
            public int compare(TrainingData t1, TrainingData t2){
                return t1.attributes[i].compareTo(t2.attributes[i]);
            }
        });
    }
    void makeDiscrete(){
        for(int i = 0; i < this.continuousValues.length; i++){
            int index = this.continuousValues[i];
            sortExamples(index);
            String thresh = this.examples[0].attributes[index];
            double threshold = Double.parseDouble(thresh);
            double informationGain = getInformationGain(index, threshold);
            for(int j = 0; j < this.examples.length - 1; j++){
                boolean curr = this.examples[j].getValue();
                boolean next = this.examples[j+1].getValue();
                if(curr != next){
                    double current = Double.parseDouble(examples[j].attributes[index]);
                    double after = Double.parseDouble(examples[j+1].attributes[index]);
                    double currIG = getInformationGain(index,(current+after)/2);
                    if(currIG > informationGain){
                        informationGain = currIG;
                        threshold = (current + after)/2;
                    }
                }
            }
            this.thresholds[i] = threshold;
        }
    }
    void getEntropy(){
        int positiveExamples = 0;
        int negativeExamples = 0;
        for (TrainingData example : this.examples) {
            if(example.getValue()){
                positiveExamples++;
            }
            negativeExamples++;
        }
        double posWeight = positiveExamples/this.examples.length;
        double negWeight = negativeExamples/this.examples.length;
        double entropy = -posWeight*(Math.log(posWeight)/Math.log(2)) 
                         - negWeight*(Math.log(negWeight)/Math.log(2));
        this.Entropy = entropy;                         
    }
    double getInformationGain(int index){
        double currEntropy = 0;
        String[] values = TrainingData.getAcceptedValues(index);
        for(String value: values){
            int posExamples = 0;
            int negExamples = 0;
            int len = 0;
            for (TrainingData example : this.examples) {
                if(example.attributes[index].equalsIgnoreCase(value)){
                    len++;
                    if(example.getValue()){
                        posExamples++;  
                    }else{
                        negExamples++;
                    }
                }
            }
            double weight = -1 * len/this.examples.length;
            double posWeight = posExamples/len;
            double negWeight = negExamples/len;
            double entropy = posWeight*(Math.log(posWeight)/Math.log(2)) 
                             + negWeight*(Math.log(negWeight)/Math.log(2));
            currEntropy += (weight*entropy);
        }
        return this.Entropy - currEntropy;
    }
    double getInformationGain(int index, double threshold){
        double currEntropy = 0;
        int posExamplesAboveThreshold = 0;
        int negExamplesAboveThreshold = 0;
        int posExamplesBelowThreshold = 0;
        int negExamplesBelowThreshold = 0;
        int lenAboveThreshold = 0;
        int lenBelowThreshold = 0;
        for (TrainingData example : this.examples) {
            if (Integer.parseInt(example.attributes[index]) > threshold){
                lenAboveThreshold++;
                if(example.getValue()){
                    posExamplesAboveThreshold++;
                }else{
                    negExamplesAboveThreshold++;
                }
            }else{
                lenBelowThreshold++;
                if(example.getValue()){
                    posExamplesBelowThreshold++;
                }else{
                    negExamplesBelowThreshold++;
                }
            }
        }
        double aboveThresholdWeight = -1 * lenAboveThreshold/examples.length;
        double posWeightAbove = posExamplesAboveThreshold/lenAboveThreshold;
        double negWeightAbove = negExamplesAboveThreshold/lenAboveThreshold;
        double entropyAbove = posWeightAbove * (Math.log(posWeightAbove)/Math.log(2)) 
                         + negWeightAbove * (Math.log(negWeightAbove)/Math.log(2));
        double belowThresholdWeight = -1 * lenBelowThreshold/examples.length;
        double posWeightBelow = posExamplesBelowThreshold/lenBelowThreshold;
        double negWeightBelow = negExamplesBelowThreshold/lenBelowThreshold;
        double entropyBelow = posWeightBelow * (Math.log(posWeightBelow)/Math.log(2)) 
                         + negWeightBelow * (Math.log(negWeightBelow)/Math.log(2));
        currEntropy = aboveThresholdWeight * entropyAbove
                     + belowThresholdWeight * entropyBelow;
        return this.Entropy - currEntropy;
    }
}