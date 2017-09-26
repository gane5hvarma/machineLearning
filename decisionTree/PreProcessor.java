package decision_tree;

import java.util.Arrays;
import java.util.Comparator;
import java.util.*;
import java.io.*;
import java.lang.*;


class PreProcessor{
    TrainingData[] examples;
    int[] continuousValues = {0, 2, 4, 10, 11, 12};
    double[] thresholds = new double[6]; 
    double Entropy;

    PreProcessor(TrainingData[] examples){
        this.examples = examples;
        getEntropy();
    }
    void getEntropy(){
        double positiveExamples = 0;
        double negativeExamples = 0;
        for (TrainingData example : this.examples) {
            if(example.getValue().equalsIgnoreCase("<=50K")){
                positiveExamples++;
            }else{
                negativeExamples++;
            }
        }
        double posWeight = positiveExamples/this.examples.length;
        double negWeight = negativeExamples/this.examples.length;
        if(posWeight == 0){
            posWeight = 1;
        }
        if (negWeight == 0) {
            negWeight = 1;
        }
        double entropy = -posWeight*(Math.log(posWeight)/Math.log(2)) 
                         - negWeight*(Math.log(negWeight)/Math.log(2));
        this.Entropy = entropy;                         
    }
    static double getEntropy(ArrayList<TrainingData> examples){
        double positiveExamples = 0;
        double negativeExamples = 0;
        for (TrainingData example : examples) {
            if(example.getValue().equalsIgnoreCase("<=50K")){
                positiveExamples++;
            }else{
                negativeExamples++;
            }
        }
        double posWeight = positiveExamples/examples.size();
        double negWeight = negativeExamples/examples.size();
        if(posWeight == 0){
            posWeight = 1;
        }
        if (negWeight == 0) {
            negWeight = 1;
        }
        double entropy = -posWeight*(Math.log(posWeight)/Math.log(2)) 
                         - negWeight*(Math.log(negWeight)/Math.log(2));
        return entropy;   
    }
    void sortExamples(int i){
        Arrays.sort(examples, new Comparator<TrainingData>(){
            @Override
            public int compare(TrainingData t1, TrainingData t2){
                return t1.attributes[i].compareTo(t2.attributes[i]);
            }
        });
    }
    void fillThresholds(){
        for(int i = 0; i < this.continuousValues.length; i++){
            int index = this.continuousValues[i];
            sortExamples(index);
            String thresh = this.examples[0].attributes[index];
            double threshold = Double.parseDouble(thresh);
            double informationGain = getInformationGain(index, threshold);
            for(int j = 0; j < this.examples.length - 1; j++){
                String curr = this.examples[j].getValue();
                String next = this.examples[j+1].getValue();
                if(!curr.equalsIgnoreCase(next)){
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
    void makeDiscrete(){
        fillThresholds();
        for (TrainingData example: this.examples) {
            for(int j = 0; j < this.thresholds.length; j++){
                int index = this.continuousValues[j];
                double threshold = this.thresholds[j];
                double value = Double.parseDouble(example.attributes[index]);
                if(value > threshold){
                    example.attributes[index] = "aT" + String.valueOf(threshold);
                }else{
                    example.attributes[index] = "bT" + String.valueOf(threshold);                    
                }
            }
        }
    }
    double getInformationGain(int index){
        double currEntropy = 0;
        String[] values = TrainingData.getAcceptedValues(index);
        for(String value: values){
            double posExamples = 0;
            double negExamples = 0;
            double len = 0;
            for (TrainingData example : this.examples) {
                if(example.attributes[index].equalsIgnoreCase(value)){
                    len++;
                    if(example.getValue().equalsIgnoreCase("<=50K")){
                        posExamples++;  
                    }else{
                        negExamples++;
                    }
                }
            }
            double weight = -1 * len/this.examples.length;
            double posWeight = posExamples/len;
            double negWeight = negExamples/len;
            if(posWeight == 0 || Double.isNaN(posWeight)){
                posWeight = 1;
            }
            if (negWeight == 0 || Double.isNaN(negWeight)) {
                negWeight = 1;
            }
            // System.out.println(len + "\t" + weight + "\t" + posWeight + "\t" + negWeight);
            double entropy = posWeight*(Math.log(posWeight)/Math.log(2)) 
                             + negWeight*(Math.log(negWeight)/Math.log(2));

            currEntropy += (weight*entropy);
        }
        return this.Entropy - currEntropy;
    }
    static double getInformationGain(ArrayList<TrainingData> examples, 
                                    int index, double Entropy){
        double currEntropy = 0;
        String[] values = TrainingData.getAcceptedValues(index);
        for(String value: values){
            double posExamples = 0;
            double negExamples = 0;
            double len = 0;
            for (TrainingData example : examples) {
                if(example.attributes[index].equalsIgnoreCase(value)){
                    len++;
                    if(example.getValue().equalsIgnoreCase("<=50K")){
                        posExamples++;  
                    }else{
                        negExamples++;
                    }
                }
            }
            double weight = -1 * len/examples.size();
            double posWeight = posExamples/len;
            double negWeight = negExamples/len;
            if(posWeight == 0 || Double.isNaN(posWeight)){
                posWeight = 1;
            }
            if (negWeight == 0 || Double.isNaN(negWeight)) {
                negWeight = 1;
            }
            // System.out.println(len + "\t" + weight + "\t" + posWeight + "\t" + negWeight);
            double entropy = posWeight*(Math.log(posWeight)/Math.log(2)) 
                             + negWeight*(Math.log(negWeight)/Math.log(2));

            currEntropy += (weight*entropy);
        }
        return Entropy - currEntropy;
    }
    static Attribute getBestAttribute(ArrayList<Attribute> attributes, 
        ArrayList<TrainingData> examples, double Entropy){
        Attribute bestAttribute = attributes.get(0);
        double maxInformationGain = getInformationGain(examples, 0, Entropy);
        for(Attribute attribute : attributes){
            int index = attribute.index;
            double ig = getInformationGain(examples, index, Entropy);
            if(ig > maxInformationGain){
                maxInformationGain = ig;
                bestAttribute = attribute;
            }
        }
        return bestAttribute;
    }
    double getInformationGain(int index, double threshold){
        double currEntropy = 0;
        double posExamplesAboveThreshold = 0;
        double negExamplesAboveThreshold = 0;
        double posExamplesBelowThreshold = 0;
        double negExamplesBelowThreshold = 0;
        double lenAboveThreshold = 0;
        double lenBelowThreshold = 0;
        for (TrainingData example : this.examples) {
            if (Double.parseDouble(example.attributes[index]) > threshold){
                lenAboveThreshold++;
                if(example.getValue().equalsIgnoreCase("<=50K")){
                    posExamplesAboveThreshold++;
                }else{
                    negExamplesAboveThreshold++;
                }
            }else{
                lenBelowThreshold++;
                if(example.getValue().equalsIgnoreCase("<=50K")){
                    posExamplesBelowThreshold++;
                }else{
                    negExamplesBelowThreshold++;
                }
            }
        }
        double aboveThresholdWeight = -1 * lenAboveThreshold/examples.length;
        double posWeightAbove = posExamplesAboveThreshold/lenAboveThreshold;
        double negWeightAbove = negExamplesAboveThreshold/lenAboveThreshold;
        if(posWeightAbove == 0){
            posWeightAbove = 1;
        }
        if (negWeightAbove == 0) {
            negWeightAbove = 1;
        }
        double entropyAbove = posWeightAbove * (Math.log(posWeightAbove)/Math.log(2)) 
                         + negWeightAbove * (Math.log(negWeightAbove)/Math.log(2));
        double belowThresholdWeight = -1 * lenBelowThreshold/examples.length;
        double posWeightBelow = posExamplesBelowThreshold/lenBelowThreshold;
        double negWeightBelow = negExamplesBelowThreshold/lenBelowThreshold;
        if(posWeightBelow == 0){
            posWeightBelow = 1;
        }
        if (negWeightBelow == 0) {
            negWeightBelow = 1;
        }
        double entropyBelow = posWeightBelow * (Math.log(posWeightBelow)/Math.log(2)) 
                         + negWeightBelow * (Math.log(negWeightBelow)/Math.log(2));
        currEntropy = aboveThresholdWeight * entropyAbove
                     + belowThresholdWeight * entropyBelow;
        return this.Entropy - currEntropy;
    }
    void writeToFile(String fileName) throws IOException{
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        for (TrainingData example : this.examples){
            for(int k = 0; k < example.attributes.length -1; k++){
                out.write(example.attributes[k]);
                out.write(",") ;
            }
            out.write(example.getValue());
            out.newLine();
        }
        out.close();
    }
    public static void main(String[] args) {
        TrainingData[] examples;
        try{
            examples = Reader.read("tempo.data");
        }catch(FileNotFoundException e){
            examples = null;
        }
        PreProcessor pro = new PreProcessor(examples);
        // System.out.println(pro.getInformationGain(13));
        double ig = 0;
        double max = 0;
        int maxi = 0;
        for (int i = 0; i < pro.examples[0].attributes.length - 1; i++ ) {
            ig = pro.getInformationGain(i);
            if (ig > max) {
                max = ig;
                maxi = i;
            }
            System.out.println(i + " " + ig);
        }
        System.out.println(TrainingData.features.get(maxi)+":"+max);
    }
}