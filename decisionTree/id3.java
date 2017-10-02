package decision_tree;

import java.util.*;
import java.io.*;
import java.lang.Math;

class id3{
    /*
    * This is the class that runs the id3 algorithm.
    * attributes : the list of attributes we are considering while building the
    * tree
    * data : the list of trainingData objects we are considering while building
    * the tree.
    * id : we increment this class level variable whenever we assign it to 
    * DecionTree as an id. This way every descisionTree gets a unique id without
    * having to use the uuid library.
    */
    ArrayList<Attribute> attributes = new ArrayList<Attribute>();
    ArrayList<TrainingData> data = new ArrayList<TrainingData>();
    ArrayList<TrainingData> validationData = new ArrayList<TrainingData>();
    int id = 0;
    id3(){
        TrainingData[] examples;
        try{
            examples = Reader.read("modifiedAdults.data");
        }catch(FileNotFoundException e){
            System.out.println("file not found");
            examples = null;
        }
        setAttributes();
        for (TrainingData example: examples) {
            this.data.add(example);
        }
        makeValidationData();
    }
    void makeValidationData(){
        if(this.data == null){
            return;
        }
        int validationSize = this.data.size()/3;
        for(int i = 0; i < validationSize; i++){
            int randIndex = (int)(Math.random() * validationSize);
            this.validationData.add(this.data.get(randIndex));
            this.data.remove(randIndex);
        }
    }
    void setAttributes(){
        // Just fill the attributes list.
        Attribute attribute;
        for (int i = 0; i < 14; i++) {
            attribute = new Attribute(i);
            this.attributes.add(attribute);
        }
    }
    DecisionTree buildTree(ArrayList<TrainingData> examples,
                            ArrayList<Attribute> attributes, int level){
        /*
        * This tree recursively builds the best DecisionTree that fits the 
        * training data. At every node, we choose a splitAttribute that gives us
        * the maximum information gain, and we add to the node as many children
        * as there are the possible values of the best attribute. For each child,
        * we assign a split value, and add to it all the trainingData objects
        * whose value of that attriubte = spliValue. Then we apply the same
        * algorithm recursively, until we reach leaf nodes, where we return.
        * @examples : list of training data we are considering while building 
        * the tree. Note that this doesn't remain constant, as each decisionTree
        * will have different training data set depending on their splitValues
        * @attributes : list of attributes we are consdering while finding the
        * best attribute. At each node, once we find best attribute, it is 
        * removed from the attributes list so that we don't build at the same
        * node again and again.
        * @level : at which level does the current decisionTree we are building
        * lie.
        * return_value : a decision tree which best fits the training data.
        */
        // increment id so that it'll be unique.
        this.id++;
        // make a copy of the list of @attributes list. we are going to remove
        // the bestAttribute once we find it.
        ArrayList<Attribute> considerAttributes = new ArrayList<Attribute>();
        for(Attribute attribute : attributes){
            considerAttributes.add(attribute);
        }
        // create a new DecisionTree and TreeNode with current examples, and a
        // assign them an id and level.
        TreeNode node = new TreeNode(examples);
        DecisionTree tree = new DecisionTree(node);
        tree.id = this.id++;
        // we are incrementing the level here because the next time we use this
        // variable again, we are goign to use it to build tree's children.
        tree.level = level++;
        if(node.isleaf){
            // node is leaf implies either there are no more examples or all the
            // examples have same classification. If that's the case, we are
            // done. Just return the current tree.
            return tree;
        }else if(considerAttributes.size() == 0){
            // if there are no attributes left to consider, assign to it the 
            // majority classification in the currentlist of examples and return
            // the tree.
            tree.root.classification = node.majorityClassification();
            return tree;
        }else{
            // if we reached here, it means there are examples and attributes
            // we need to consider while bulding the tree.
            // get Entropy. 
            double entropy = PreProcessor.getEntropy(examples);
            // bestAttribute is the attribute that gets us highest information
            // gain.
            Attribute bestAttribute = PreProcessor.getBestAttribute(
                                         considerAttributes, examples, entropy);
            // bestIndex is the index of the attribute that gives highest info
            // gain.
            int bestIndex = bestAttribute.index;
            // set splitAttribute as bestAttribute. Now this node has number of
            // children equal to number of possible values of the bestAttribute.
            // we have to appropriate build the tree for all those chidren.
            node.setSplitAttribute(bestAttribute);
            for (String val: TrainingData.getAcceptedValues(bestIndex)){
                // new_examples will contain all the TrainingData which have
                // same value for bestAttribute as val.
                ArrayList<TrainingData> new_examples = 
                                                  new ArrayList<TrainingData>();
                for(TrainingData example: examples){
                    String currValue = example.attributes[bestIndex];
                    if(currValue == "?"){
                        // "?" implies missing data. In that case, just get the
                        // most common value this attribute has in the examples
                        // at this node.
                        currValue = getMostCommonValue(examples, bestIndex);
                    }
                    if(currValue.equalsIgnoreCase(val)){
                        // it means this training example belongs in this path.
                        new_examples.add(example);
                    }
                }
                if (new_examples.size() == 0) {
                    // if there are no new_eeamples, it means we have reached 
                    // the end of this tree for this path. Make a new tree and 
                    // and set its isleaf = true. 
                    // it's classification will be equal to the majority 
                    // classification of its parent in this case. 
                    TreeNode final_leaf = new TreeNode(new_examples);
                    DecisionTree final_child = new DecisionTree(final_leaf);
                    final_child.level = level;
                    final_child.id = this.id++;
                    final_child.root.isleaf = true;
                    final_child.root.splitValue = val;
                    final_child.root.classification = 
                                                 node.majorityClassification();
                    // no need to recurse anymore. We have reacehd the end of 
                    // tree.
                    tree.addChild(final_child);
                }
                else{
                    // if we are here, it means either all examples have same
                    // classification, in which case it is leaf node, or we have
                    // to recurse further.

                    // create a new child and set its splitValue. This is the
                    // second kind of node (conditional node), that only has
                    // one child and represents a path.
                    TreeNode new_child = new TreeNode(new_examples);
                    new_child.setSplitValue(val);
                    DecisionTree child_tree = new DecisionTree(new_child);
                    child_tree.level = level;
                    child_tree.id = this.id++;
                    if(!new_child.isleaf){
                        // if the child is not a leaf, it means we have to bulid
                        // the tree from below it recursively.
                        // Remove the bestAttributes from considerAttributes and
                        // call this function again with an incremented level.
                        considerAttributes.remove(bestAttribute);
                        child_tree.addChild(buildTree(new_examples,
                                                  considerAttributes,level+1));
                        tree.addChild(child_tree);
                    }else{
                        // if its a leaf node, we are done. Just add the child 
                        // as a child to the current tree.
                        tree.addChild(child_tree);
                    }
                }
            }
            // we are done!
            return tree;
        }
    }
    static String getMostCommonValue(ArrayList<TrainingData> examples,
                                                                    int index){
        /*
        * A function that retuns the most common value that an attribute has 
        * in a list of given examples.
        * @examples : The list of trainingData objects in which we have to find
        * the most common value.
        * @index : Index of the attribute whose value we must consider.
        * return_value : String that is the most common value in examples.
        */
        // HashMap to store attribute value and number of times it appears.
        Map<String, Integer> map = new HashMap<String, Integer>();
        // Array of Values that this attribute may have.
        String[] acceptedValues = TrainingData.getAcceptedValues(index);
        // Set all acceptedValues to have zero occurances.
        for(String value: acceptedValues){
            map.put(value, 0);
        }
        for(TrainingData example : examples){
            String key = example.attributes[index];
            // we are here because we have to find the most common value in case
            // "?" appears. So all the examples that have "?" as their values 
            // are useless to us.
            if(key.equals("?")){
                continue;
            }
            // increment the number of occurances by 1.
            map.put(key, map.get(key) + 1); 
        }
        String maxKey = null;
        int maxValue = 0;
        // find key corresponding to maximum number of occurances. 
        for(Map.Entry<String,Integer> entry : map.entrySet()) {
            if (maxKey == null || entry.getValue() > maxValue) {
                maxKey = entry.getKey();
                maxValue = entry.getValue();
            }
        }
        return maxKey;
    }
    static double getAccuracy(DecisionTree dt, TrainingData[] examples){
        /*
        * A function to calculate accuracy of our decisionTree on an array of 
        * test data. We calculate accuracy by determining the proportion of 
        * test data that our decisoin tree correctly classifies.
        * @dt : The decisionTree we are checking the accuracy of.
        * @examples : the test data we use to determine accuracy.
        */
        // count keeps track of number of training examples that our tree has
        // correctly classified.
        double count = 0;
        for(TrainingData t : examples){
            // actualvalue is the classification of the training data and 
            // obtained value is the classification that our tree gave us.
            String actualValue = t.attributes[t.attributes.length - 1];
            String obtainedValue = dt.getClassification(dt,t);
            // adding "." because testData has a traling "." at the end.
            if(actualValue.charAt(actualValue.length() -1) == '.'){
                obtainedValue = obtainedValue + ".";
            }
            if(actualValue.equals(obtainedValue)){
                // increment count if our tree has classified the training data
                // correctly.
                count++;
            }
        }
        // return the accuracy
        return count/examples.length;
    }
}