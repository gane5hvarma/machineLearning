package decision_tree;

import java.util.*;
import java.lang.Math;

class RandomForest{
    DecisionTree[] randomForest = new DecisionTree[60];
    id3 runner = new id3();
    void buildRandomForest(){
        for(int i = 0; i < 60; i++){
            ArrayList<TrainingData> trainingData = getTrainingData();
            randomForest[i] = buildTree(trainingData, runner.attributes);
        }
    }
    ArrayList<TrainingData> getTrainingData(){
        ArrayList<TrainingData> data = new ArrayList<TrainingData>();
        for(int i = 0; i < 32561; i++){
            int rand = (int)(Math.random() * 32561);
            data.add(runner.data.get(rand));
        }
        return data;
    }
    ArrayList<Attribute> getAttributes(ArrayList<Attribute> oldAttrs){
        ArrayList<Attribute> newAttrs = new ArrayList<Attribute>();
        int len;
        if(oldAttrs.size() <= 4){
            len = oldAttrs.size();
        }else{
            len = 4;
        }
        for(int i = 0; i < len; i ++){
            int rand = (int)(Math.random() * len);
            newAttrs.add(oldAttrs.get(rand));
        }
        return newAttrs;
    }
    DecisionTree buildTree(ArrayList<TrainingData> examples, 
                           ArrayList<Attribute> attrs){
        ArrayList<Attribute> considerAttributes = getAttributes(attrs);
        TreeNode node = new TreeNode(examples);
        DecisionTree tree = new DecisionTree(node);
        if(node.isleaf){
            return tree;
        }else if(considerAttributes.size() == 0 || stopTreeBuilding(attrs)){
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
                ArrayList<TrainingData> nExamples = new ArrayList<TrainingData>();
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
                    final_child.root.classification = node.majorityClassification();
                    tree.addChild(final_child);
                }else{
                    TreeNode new_child = new TreeNode(nExamples);
                    new_child.setSplitValue(val);
                    DecisionTree child_tree = new DecisionTree(new_child);
                    if(!new_child.isleaf){
                        considerAttributes.remove(bestAttr);
                        child_tree.addChild(buildTree(nExamples, considerAttributes));
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
        int posExample = 0;
        int negExample = 0;
        String classification = null;
        for(int i = 0; i < 60; i ++){
            classification = randomForest[i].getClassification(randomForest[i], t);
            if(classification.equalsIgnoreCase("<=50K")){
                posExample++;
            }else{
                negExample++;
            }
        }
        if(posExample == negExample){
            return null;
        }
        else if(posExample > negExample){
            return "<=50K.";
        }else{
            return ">50K.";
        }
    }
    boolean stopTreeBuilding(ArrayList<Attribute> attrs){
        Attribute attr1 = attrs.get(0);
        for(Attribute attr : attrs){
            if(!attr.name.equalsIgnoreCase(attr1.name)){
                return false;
            }
        }
        return true;
    }
    double getAccuracy(TrainingData[] examples){
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
    public static void main(String[] args) {
        TrainingData[] test = null;
        try{
            test = Reader.read("modifiedTest.data");
        }catch(java.io.FileNotFoundException e){
            System.out.println("FileNotFoundException");
        }
        RandomForest randForest = new RandomForest();
        randForest.buildRandomForest();
        DecisionTree dt;
        for(int i = 0; i < 60; i++){
            dt = randForest.randomForest[i];
            System.out.println( i + " : " + id3.getAccuracy(dt, test));
        }
        System.out.println(randForest.getAccuracy(test));
    }
}