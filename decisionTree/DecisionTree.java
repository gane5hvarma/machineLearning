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
        /*
        * Given a training example t, find out how the tree classifies the 
        Training data as.
        * We must pass DecisionTree tree as an attribute because it's going to 
        be a recursive implementation.
        * Iniitial call should be tree.getClassification(tree,t)
        */
        String classification = null;
        // System.out.println("here");
        if(tree.root.isleaf){
            // System.out.println("it's leaf"+tree.root.classification);
            return tree.root.classification;
        }
        if(tree.root.splitAttribute == null){
            // System.out.println("null "+tree.root.majorityClassification());
            return tree.root.majorityClassification();
        }
        else{
            int index = tree.root.splitAttribute.index;
            String value = t.attributes[index];
            for(DecisionTree child : tree.children){
                if(value.equalsIgnoreCase(child.root.splitValue)){
                    // System.out.println(index + " " + value + " " + child.root.splitValue);
                    if(child.root.data.get(0).attributes[index].equals(value)){
                        // System.out.println(child.root.data.get(0).attributes[index]);
                        // System.out.println(classification);
                        classification = getClassification(child, t);
                    }
                }
            }   
        }
        return classification;
    }

}