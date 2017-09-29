package decision_tree;

import java.util.*;

class Prune{
    DecisionTree bestTree;
    double bestAccuracy;
    TrainingData[] train;
    Prune(DecisionTree dt, double accuracy, TrainingData[] data){
        bestTree = dt;
        bestAccuracy = accuracy;
        train = data;
    }
    DecisionTree prune(){
        long startTime = System.currentTimeMillis();
        double newAccuracy = 0;
        DecisionTree copiedTree = new DecisionTree(bestTree);
        DecisionTree updatedTree = new DecisionTree(copiedTree);
        for(int i = 1; i < 27; i = i+2){
            ArrayList<Integer> ids = new ArrayList<Integer>();
            ArrayList<Integer> idss = new ArrayList<Integer>();
            updatedTree.getElementIds(updatedTree, i, ids);
            bestTree.getElementIds(bestTree, i, idss);
            System.out.println(ids.size() + " " + i + " " + idss.size());
            if(ids.size() == 0){
                break;
            }
            for(int id: ids){
                DecisionTree child = updatedTree.getElementById(updatedTree,id);
                child.prune();
                newAccuracy = id3.getAccuracy(updatedTree, train);
                if(newAccuracy >= bestAccuracy){
                    System.out.println("pruned to "+newAccuracy);
                    bestAccuracy = newAccuracy;
                    copiedTree = new DecisionTree(updatedTree); 
                }
                System.out.println("current accuracy" + bestAccuracy);
                updatedTree = new DecisionTree(copiedTree);
            }
        }
        long endTime = System.currentTimeMillis();   
        double time = (double)(endTime - startTime)/60000;
        System.out.println(time);
        System.out.println(bestAccuracy);
        return updatedTree;
    }
}