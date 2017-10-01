package decision_tree;

import java.util.*;

class TreeNode{
    /*
    * A decision Tree is constructed as follows:
    * Every decision tree has nodes of the class DecisionTree. Each DecisionTree
    * has children that are DecisionTree objects themselves. Every DecisionTree
    * has a root of class TreeNode. 
    * This TreeNode class defines various methods and attributes that a root of
    * DecisionTree needs to have.
    * Every decision Treee has multiple types of nodes. They are :
    *   -> Nodes which represent an attribute
    *   -> Nodes which represent one value of attribute.
    *   -> leafNodes which just define the classification for that path.
    * Instead of making three different Nodes to represent each, we can use the
    * same node class for all of them by giving them various identifiers 
    * described below.
    *   -> splitAttribute : Attribute based on which splitting takes place. Set
    *                       this value to make it node of the first kind.
    *   -> splitValue : one value of the splitAttribute of the parent node. Set
    *                   this value to make it a node of the second kind.
    *   -> isleaf : a boolean which tells whether the current node is leafNode.
    * ArrayList<TrainingData> data is just a list of TrainingExamples present at
    * that node in the decision tree.
    * String Classification : in case it's a leafNode, this gives the tree's
    * classification of any example that traversed the tree to reach here.
    */
    Attribute splitAttribute;
    String splitValue;
    ArrayList<TrainingData> data;
    String classification;
    boolean isleaf = true;
    TreeNode(ArrayList<TrainingData> data){
        this.data = data;
        setClassification();
        // The following lines determine whether the givenNode isleaf or not.
        // There are two cases in which it can be considered a leafNode
        // one, when there are no examples associated with this. Two, when all
        // the examples associated with the node have the same classification.
        if(this.data.size() == 0){
            // first case.
            this.isleaf = true;
            return;
        }else{
            // for second case. Just see if all the classificatiosn are same, if
            // atleast one of them is different assign isleaf to false and 
            // return
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
        // This constructor is very much useful while pruning. When pruning is
        // done, we need to hold a copy of our bestDecisionTree to get back to
        // while we prune the originalTree. In java, objects are copied by 
        // reference and not by value. So when we prune the copy of original
        // Tree, the original Tree is also pruned. To preven that from happening
        // we specifically have to use a constructor like this.
        this.data = treeNode.data;
        this.isleaf = treeNode.isleaf;
        this.classification = treeNode.classification;
        this.splitAttribute = treeNode.splitAttribute;
        this.splitValue = treeNode.splitValue;
    }
    void setSplitAttribute(Attribute splitAttribute){
        /*
        * while constructing decisionTree, we need to set either splitAttribute
        * or splitValue based on what kind of node it is. Therefore this and the
        * following setSplitValue let us set splitAttribute and splitValue
        * @splitAttribute : an Attribute object which has highest information
        * gain at that node.
        */
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
            // if a node is leaf, but has non empty data set, it means all 
            // examples have same classfication. Therefore just get the 
            // classification of first training data example.
            this.classification = data.get(0).getValue();
            return;
        }
        else if(data.size() == 0){
            // This is only a place holder. In case there are no training data 
            // examples at a node, we have to get majority classification of the
            // parentNode. We take care of it while constructing the tree itself.
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
        /*
        * A simple method to get Majory Classification at that node. Majority
        * classification simply means the classification which majoirty of 
        * training data have at that node.
        * This method is very useful while constructing decision tree and while
        * pruning. 
        * return_value : striing representing majority classification.
        */
        // posExamples keep track of number trainingdata examples whose
        // classification is <=50K
        int posExamples = 0;
        int negExamples = 0;
        // negExamples keep track of number trainingdata examples whose
        // classification is >50K
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