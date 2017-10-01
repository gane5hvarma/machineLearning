package decision_tree;

import java.util.*;

class Driver{
    public static void main(String[] args) {
        id3 id = new id3();
        double currTime = (double)System.currentTimeMillis();
        DecisionTree dt = id.buildTree(id.data, id.attributes , 0);
        double doneTime = (double)System.currentTimeMillis();
        System.out.println("Time taken to build the tree: " + 
                                                   (doneTime - currTime)/60000);
        TrainingData[] test = null;
        try{
            test = Reader.read("modifiedTest.data");
        }catch(java.io.FileNotFoundException e){
            System.out.println("FileNotFoundException");
        }
        System.out.println("determining accuracy....");
        double accuracy = id.getAccuracy(dt, test);
        System.out.println("accuracy before pruning: " + accuracy);
        Prune pruner = new Prune(dt, accuracy, test);
        DecisionTree prunedTree = pruner.prune();
        RandomForest randForest = new RandomForest();
        currTime = (double)System.currentTimeMillis();
        randForest.buildRandomForest();
        double endTime = (double)System.currentTimeMillis();
        System.out.println("Time Taken to build RandomForest: " + 
                                                    (endTime - currTime)/60000);
        System.out.println("accuracy with RandomForest is : " + 
                                                  randForest.getAccuracy(test));
    }
}