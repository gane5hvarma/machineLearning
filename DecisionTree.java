package decision_tree;

import java.util.*;

class DecisionTree{
    TreeNode root;
    ArrayList<DecisionTree> children = new ArrayList<DecisionTree>();
    DecisionTree(TreeNode root){
        this.root = root;
    }
    void addChild(DecisionTree child){
        this.children.add(child);
    }
    int totalNumberofNodes(){
        int numberOfNodes = 0;
        for (DecisionTree tree : this.children) {
            for (DecisionTree child : tree.children) {
                if(!child.root.isleaf){
                    numberOfNodes++;
                }
            }
        }
        return numberOfNodes;
    }
    void printTree(){
        if(root.isleaf){
            System.out.println("leaf node: " + root.classification);
        }else if(root.splitAttribute == null){
            System.out.println("leaf node: " + root.classification);
        }else{
            System.out.println("TreeNode: " + root.splitAttribute.name);
            for (DecisionTree child : this.children) {
                child.printTree();
            }
        }
    }
    String getClassification(DecisionTree tree, TrainingData t){
        
    }
}