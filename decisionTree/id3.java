package decision_tree;

import java.util.*;
import java.io.*;

class id3{
    ArrayList<Attribute> attributes = new ArrayList<Attribute>();
    ArrayList<TrainingData> data = new ArrayList<TrainingData>();
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
                    String currValue = example.attributes[bestAttribute.index];
                    if(currValue == "?"){
                        currValue = getMostCommonValue(examples, bestAttribute.index);
                    }
                    if(currValue.equalsIgnoreCase(val)){
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
    String getMostCommonValue(ArrayList<TrainingData> examples, int index){
        Map<String, Integer> map = new HashMap<String, Integer>();
        String[] acceptedValues = TrainingData.getAcceptedValues(index);
        for(String value: acceptedValues){
            map.put(value, 0);
        }
        for(TrainingData example : examples){
            String key = example.attributes[index];
            if(key == "?"){
                continue;
            }
            map.put(key, map.get(key) + 1); 
        }
        String maxKey = null;
        int maxValue = 0;
        for(Map.Entry<String,Integer> entry : map.entrySet()) {
            if (maxKey == null || entry.getValue() > maxValue) {
                maxKey = entry.getKey();
                maxValue = entry.getValue();
            }
        }
        return maxKey;
    }
    public static void main(String[] args) {
        id3 id = new id3();
        DecisionTree dt = id.buildTree(id.data, id.attributes);
        System.out.println(dt.root.splitAttribute.name);
    }
}