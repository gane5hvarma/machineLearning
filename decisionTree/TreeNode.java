package decision_tree;

import java.util.*;

class TreeNode{
    Attribute splitAttribute;
    String splitValue;
    ArrayList<TrainingData> data;
    String classification;
    boolean isleaf = true;
    boolean delete = false;
    TreeNode(ArrayList<TrainingData> data){
        this.data = data;
        setClassification();
        if(this.data.size() == 0){
            this.isleaf = true;
            return;
        }else{
            String value = this.data.get(0).getValue();
            for(TrainingData t: this.data){
                if(!value.equalsIgnoreCase(t.getValue())){
                    this.isleaf= false;
                    return;
                }
            }
        }
    }
    TreeNode(TreeNode treeNode){
        this.data = treeNode.data;
        this.isleaf = treeNode.isleaf;
        this.classification = treeNode.classification;
        this.splitAttribute = treeNode.splitAttribute;
        this.splitValue = treeNode.splitValue;
    }
    void setSplitAttribute(Attribute splitAttribute){
        this.splitAttribute = splitAttribute;
    }
    void setSplitValue(String splitValue){
        this.splitValue = splitValue;
    }
    void setClassification(){
        /*
        * We need to set Classification in two cases. One, while pruning.
        Two, if it's a leafnode. 
        * There are two cases that it may be a leaf node, that either data is 
        empty or data is empty
        */
        if(isleaf && this.data.size() != 0){
            this.classification = data.get(0).getValue();
            return;
        }
        else if(data.size() == 0){
            // Figure something better out
            this.classification = majorityClassification();
            return; 
        }
        else{
            // This is when it's not a leafNode. That is useful while pruning. 
            // we need majority classification in this case.
            this.classification = majorityClassification();
        }
    }
    String majorityClassification(){
        int posExamples = 0;
        int negExamples = 0;
        for(TrainingData t: this.data){
            if(t.getValue().equalsIgnoreCase("<=50K")){
                posExamples++;
            }else{
                negExamples++;
            }
        }
        if (posExamples > negExamples){
            return "<=50K";
        }else{
            return ">50K";
        }
    }
}