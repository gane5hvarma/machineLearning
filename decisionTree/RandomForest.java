package decision_tree;

import java.util.*;
import java.lang.Math;

class RandomForest{
    /*
    * A class to perform Decision Tree learning via RandomForest.
    * RandomForest is a collectin of DecisionTrees. These DecisonTrees are built
    * on randomly sampeld data from the actual data. Random sampling, with
    * replacement would give us roughly 2/3rd unique values and 1/3rd values 
    * that are repeated.
    * More over these decision trees are built in a slightly different way, best
    * attribute for splitting is obtained from randomly choosen attributes 
    *(number of randomly chosen attrs is usually 1/3rd the actual number of 
    * attributes) instead of from all the available attributes.
    * @randomForest : an array of decision trees
    * @examples : training data on which the decision trees are built.
    */
    DecisionTree[] randomForest = new DecisionTree[29];
    TrainingData[] examples = null;
    id3 runner = new id3();
    void buildRandomForest(){
        try{
            examples = Reader.read("modifiedAdults.data");
        }catch(java.io.FileNotFoundException e){
            System.out.println("file not found");
            examples = null;
        }
        for(int i = 0; i < 29; i++){
            ArrayList<TrainingData> trainingData = getTrainingData();
            randomForest[i] = buildTree(trainingData, runner.attributes);
        }
    }
    ArrayList<TrainingData> getTrainingData(){
        /*
        * for each decision tree, we need to create a new dataset from the 
        * original by randomly choosing , with replacement, from the original.
        * This is known as bagging. This method returns one such data set.
        * return_value : an arraylist of randomly chosen TrainingData
        */
        // data is used to store TrainingData objects obtained due to bagging.
        ArrayList<TrainingData> data = new ArrayList<TrainingData>();
        for(int i = 0; i < 32561; i++){
            // randomly generate an index between 0 and 32561. 32561 is the 
            // number of TrainingData objects.
            int rand = (int)(Math.random() * 32561);
            data.add(this.examples[rand]);
        }
        return data;
    }
    ArrayList<Attribute> getAttributes(ArrayList<Attribute> oldAttrs){
        /*
        * At every node of the first kind, while building the decision tree, we
        * need to randomly choose 4 (because 1/3rd of total number of attrs) 
        * attributes. From among these attributes, we consider the attribute 
        * with highest information gain as best attribute for splitting at that
        * node.
        * @oldAttrs : attributes from which random attributes have to be choosen
        * via bagging.
        * return_value : arrayList of randomly chosen attributes. 
        */
        // newAttrs will hold randomly chosen attributes.
        ArrayList<Attribute> newAttrs = new ArrayList<Attribute>();
        int len;
        // During tree construction we will evntually reach a point where the
        // number of attributes left to be used in the tree will be less than 4.
        // It is useless to expand the attribute size to 4 in cases like this.
        if(oldAttrs.size() <= 4){
            len = oldAttrs.size();
        }else{
            len = 4;
        }
        // This is similar to how random TrainingData objects were selected.
        for(int i = 0; i < len; i ++){
            int rand = (int)(Math.random() * len);
            newAttrs.add(oldAttrs.get(rand));
        }
        return newAttrs;
    }
    DecisionTree buildTree(ArrayList<TrainingData> examples, 
                           ArrayList<Attribute> attrs){
        /*
        * This method is very similar to id.buildTree() method. The differences
        * is how we decide best attribute for splitting. The alogorithm to 
        * find best Attribute is as follows:
        *   * from list of attributes not yet used in building the tree, choose
        *     randomly 4 attributes ( if there are more than 4 atts that haven't
        *     been chosen yet) or randomly choose same number of attributes 
        *     that is the length of available attributes (it should be choosen
        *     randomly because same attribute may be selcted more than once)
        * Everything else is the same. 
        */
        ArrayList<Attribute> considerAttributes = getAttributes(attrs);
        TreeNode node = new TreeNode(examples);
        DecisionTree tree = new DecisionTree(node);
        if(node.isleaf){
            return tree;
        }else if(considerAttributes.size() == 0){
            tree.root.classification = node.majorityClassification();
            return tree;
        }
        else{
            double entropy = PreProcessor.getEntropy(examples);
            Attribute bestAttr = PreProcessor.getBestAttribute(
                                        considerAttributes, examples, entropy);
            node.setSplitAttribute(bestAttr);
            int index = bestAttr.index;
            for(String val : TrainingData.getAcceptedValues(index)){
                ArrayList<TrainingData> nExamples =
                                                 new ArrayList<TrainingData>();
                for(TrainingData example : examples){
                    String currValue = example.attributes[index];
                    if(currValue == "?"){
                        currValue = id3.getMostCommonValue(examples, index);
                    }
                    if(currValue.equalsIgnoreCase(val)){
                        nExamples.add(example);
                    }
                }
                if(nExamples.size() == 0){
                    TreeNode final_leaf = new TreeNode(nExamples);
                    DecisionTree final_child = new DecisionTree(final_leaf);
                    final_child.root.isleaf = true;
                    final_child.root.splitValue = val;
                    final_child.root.classification =
                                                  node.majorityClassification();
                    tree.addChild(final_child);
                }else{
                    TreeNode new_child = new TreeNode(nExamples);
                    new_child.setSplitValue(val);
                    DecisionTree child_tree = new DecisionTree(new_child);
                    if(!new_child.isleaf){
                        considerAttributes.remove(bestAttr);
                        child_tree.addChild(buildTree(nExamples,
                                                           considerAttributes));
                        tree.addChild(child_tree);
                    }else{
                        tree.addChild(child_tree);
                    }
                }
            }
            return tree;
        }
    }
    String getClassification(TrainingData t){
        /*
        * In case of randomForest, classification of a TrainingData example is
        * the majority classification, i.e, the classifcation which majority of
        * decisionTrees of the forest return.
        * @t : TrainingData object that needs to be classified.
        */
        // posExample stoes number of DecisionTrees that clssify the example as
        // <=50K
        int posExample = 0;
        // netExample stoes number of DecisionTrees that clssify the example as
        // >50K
        int negExample = 0;
        String classification = null;
        for(int i = 0; i < 29; i ++){
            // iterate over all the decisionTrees and find classification given
            // by each tree.
            classification =
                          randomForest[i].getClassification(randomForest[i], t);
            if(classification.equalsIgnoreCase("<=50K")){
                posExample++;
            }else{
                negExample++;
            }
        }
        // if posExamples are same as negExamples, then randomForest can't 
        // classify the example. We can avoid this case by simply creating
        // a forest with odd number of trees.
        if(posExample == negExample){
            return null;
        }
        // Return appropriate classifcation
        else if(posExample > negExample){
            return "<=50K.";
        }else{
            return ">50K.";
        }
    }
    double getAccuracy(TrainingData[] examples){
        /*
        * Get accuracy of the randomForest on training data. This is very 
        * similar to id3.getAccuracy() method.
        */
        double count = 0;
        for(TrainingData t : examples){
            String actualValue = t.attributes[t.attributes.length - 1];
            String obtainedValue = getClassification(t);
            if(actualValue.equalsIgnoreCase(obtainedValue)){
                count++;
            }
        }
        return count/examples.length;
    }
    void getRecall(TrainingData[] examples){
        double posCount = 0;
        double negCount = 0;
        double posLength = 0;
        double negLength = 0;
        for(TrainingData t : examples){
            String actualValue = t.attributes[t.attributes.length - 1];
            String obtainedValue = getClassification(t);
            if(obtainedValue.equalsIgnoreCase("<=50K.")){
                posLength++;
                if(actualValue.equalsIgnoreCase(obtainedValue)){
                    posCount++;
                }
            }
            if(obtainedValue.equalsIgnoreCase(">50K.")){
                negLength++;
                if(actualValue.equalsIgnoreCase(obtainedValue)){
                    negCount++;
                }
            }
        }
        // return the accuracy
        System.out.println("recall for <=50K class is " + posCount/posLength);
        System.out.println("recall for >50K class is " + negCount/negLength);
    }
}