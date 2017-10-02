package decision_tree;

import java.util.*;
import java.util.ArrayList;


class Driver{
    public static void main(String[] args) {
        id3 id = new id3();
        double currTime = (double)System.currentTimeMillis();
        DecisionTree dt = id.buildTree(id.data, id.attributes , 0);
        double doneTime = (double)System.currentTimeMillis();
        System.out.println("Time taken to build the tree: " + 
                                        (doneTime - currTime)/60000 + "Minutes");
        TrainingData[] test = null;
        try{
            test = Reader.read("modifiedTest.data");
        }catch(java.io.FileNotFoundException e){
            System.out.println("FileNotFoundException");
        }
        System.out.println("determining accuracy....");
        double accuracy = id.getAccuracy(dt, test);
        System.out.println("accuracy before pruning: " + accuracy);
        TrainingData[] validationData = 
                                     new TrainingData[id.validationData.size()]; 
        validationData = id.validationData.toArray(validationData);
        Prune pruner = new Prune(dt, accuracy, validationData);
        System.out.println("Pruning. This might take some time...");
        DecisionTree prunedTree = pruner.prune();
        RandomForest randForest = new RandomForest();
        currTime = (double)System.currentTimeMillis();
        randForest.buildRandomForest();
        double endTime = (double)System.currentTimeMillis();
        System.out.println("Time Taken to build RandomForest: " + 
                                    (endTime - currTime)/60000 + " Minutes");
        currTime = (double)System.currentTimeMillis();
        System.out.println("accuracy with RandomForest is : " + 
                                                  randForest.getAccuracy(test));
        endTime = (double)System.currentTimeMillis();
        System.out.println("Time Taken to find accuracy: " + 
                                    (endTime - currTime)/60000 + " Minutes");
    }
}