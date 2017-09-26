package decision_tree;

import java.util.*;

class id3{
    ArrayList<Attribute> attributes;
    ArrayList<TrainingData> data;
    PreProcessor pre;
    id3(){
        TrainingData[] examples
        try{
            examples = Reader.read("tempo.data");
        }catch(FileNotFoundException e){
            examples = null;
        }
        pre = new PreProcessor(examples);
        setAttributes();
        this.data = pre.examples;
    }
    void setAttributes(){
        Attribute attribute
        for (int i = 0; i < 14; i++) {
            attribute = new attribute(i);
            this.attributes.add(attribute);
        }
    }
    DecisionTree buildTree(ArrayList<TrainingData> examples,
                            ArrayList<Attribute> attributes){
        ArrayList<Attribute> considerAttributes = new ArrayList<Attribute>();
        for(attribute : attributes){
            considerAttributes.add(attribute);
        }
        TreeNode node = new TreeNode(examples);
        DecisionTree tree = new DecisionTree(node);
        if(node.isleaf){
            return tree;
        }else if(considerAttributes.size() == 0){
            tree.root.classification = root.majorityClassification();
            return tree;
        }else{
            double entropy = PreProcessor.getEntropy(examples);
            
        }
    }
    
}