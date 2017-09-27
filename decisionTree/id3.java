package decision_tree;

import java.util.*;
import java.io.*;

class id3{
    ArrayList<Attribute> attributes;
    ArrayList<TrainingData> data;
    id3(){
        TrainingData[] examples;
        try{
            examples = Reader.read("tempo.data");
        }catch(FileNotFoundException e){
            examples = null;
        }
        setAttributes();
        for (TrainingData example: examples) {
            this.data.add(example);
        }
    }
    void setAttributes(){
        Attribute attribute;
        for (int i = 0; i < 14; i++) {
            attribute = new Attribute(i);
            this.attributes.add(attribute);
        }
    }
    DecisionTree buildTree(ArrayList<TrainingData> examples,
                            ArrayList<Attribute> attributes){
        ArrayList<Attribute> considerAttributes = new ArrayList<Attribute>();
        for(Attribute attribute : attributes){
            considerAttributes.add(attribute);
        }
        TreeNode node = new TreeNode(examples);
        DecisionTree tree = new DecisionTree(node);
        if(node.isleaf){
            return tree;
        }else if(considerAttributes.size() == 0){
            tree.root.classification = node.majorityClassification();
            return tree;
        }else{
            double entropy = PreProcessor.getEntropy(examples);
            Attribute bestAttribute = PreProcessor.getBestAttribute(
                                         considerAttributes, examples, entropy);
            node.setSplitAttribute(bestAttribute);
            for (String val: TrainingData.getAcceptedValues(bestAttribute.index)){
                ArrayList<TrainingData> new_examples = new ArrayList<TrainingData>();
                for(TrainingData example: examples){
                    if(example.attributes[bestAttribute.index].equalsIgnoreCase(val)){
                        new_examples.add(example);
                    }
                }
                TreeNode new_child = new TreeNode(new_examples);
                new_child.setSplitValue(val);
                DecisionTree child_tree = new DecisionTree(new_child);
                if(!new_child.isleaf){
                    considerAttributes.remove(bestAttribute);
                    child_tree.addChild(buildTree(new_examples,considerAttributes));
                }else{
                    child_tree.root.setClassification();
                    tree.addChild(child_tree);
                }
            }
            return tree;
        }
    }
    public static void main(String[] args) {
        id3 id = new id3();
        
    }
}