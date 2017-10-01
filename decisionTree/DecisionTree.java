package decision_tree;

import java.util.*;
import java.util.Collections;

class DecisionTree{
    TreeNode root;
    int id = 0;
    int level = 0;
    ArrayList<DecisionTree> children = new ArrayList<DecisionTree>();
    DecisionTree(TreeNode root){
        this.root = root;
    }
    DecisionTree(DecisionTree dt){
        this.root = new TreeNode(dt.root);
        for(DecisionTree child: dt.children){
            this.children.add(new DecisionTree(child));
        }
        this.id = dt.id;
        this.level = dt.level;
    }
    void addChild(DecisionTree child){
        this.children.add(child);
    }
    void prune(){
        this.root.isleaf = true;
        this.root.classification = this.root.majorityClassification();
        this.children.clear();
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
            System.out.println("conditional node: " + root.classification);
            for (DecisionTree child : this.children) {
                child.printTree();
            }
        }else{
            System.out.println("TreeNode: " + root.splitAttribute.name);
            for (DecisionTree child : this.children) {
                child.printTree();
            }
        }
    }
    int maxLevel(DecisionTree dt){
        if(dt.root.isleaf){
            return dt.level;
        }else if(dt.children.size() == 0){
            return dt.level;
        }
        else{
            ArrayList<Integer> levels = new ArrayList<Integer>();
            for(DecisionTree child: dt.children){
                int level = maxLevel(child);
                levels.add(level);
            }
            return Collections.max(levels);
        }
    }
    void getElementIds(DecisionTree dt, int level, ArrayList<Integer> ids){
        if(dt.level == level){
            ids.add(dt.id);
        }else if(dt.level > level){
            return;
        }else{
            for(DecisionTree child: dt.children){
                getElementIds(child, level, ids);
            }
        }
    }
    DecisionTree getElementById(DecisionTree dt, int id){
        DecisionTree req = null;
        if(dt.id == id){
            req = dt;
        }
        if((dt.root.isleaf || dt.root.data.size() == 0) && dt.id != id){
            req = null;
        }
        for(DecisionTree child : dt.children){
            DecisionTree temp = getElementById(child ,id);
            if(temp != null){
                req = temp;
            }
        }
        return req;
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
        if(tree.root.isleaf){
            return tree.root.classification;
        }
        if(tree.root.splitAttribute == null){
            if(tree.children.size() == 0){
                return tree.root.classification;
            }
            classification = getClassification(tree.children.get(0), t);
        }
        else{
            int index = tree.root.splitAttribute.index;
            String value = t.attributes[index];
            if(value.equals("?")){
                value = id3.getMostCommonValue(tree.root.data, index);
            }
            for(DecisionTree child : tree.children){
                if(value.equalsIgnoreCase(child.root.splitValue)){
                    if(child.root.data.size() == 0){
                        return child.root.classification;
                    }
                    if(child.root.data.get(0).attributes[index].equals(value)){
                        classification = getClassification(child, t);
                    }
                }
            }   
        }
        return classification;
    }
}