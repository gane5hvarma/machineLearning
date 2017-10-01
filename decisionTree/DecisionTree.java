package decision_tree;

import java.util.*;
import java.util.Collections;

class DecisionTree{
    /*
    * DecisionTree represents the actual Node of the DecisionTree. The objects
    * of DecisionTree are building blocks of the tree.
    * Each DecisionTree has a root (of TreeNode class) that determines which of
    * the three kinds of nodes it is. Each DecisionTree also has an id and level
    * id -> unique identitifaction number for each decisionTree. We can extract
    * a node based on it's id.
    * level -> represents the level at which the decisionTree is present in the 
    * over all tree. The root of the overall tree is at level 1.
    * Each DecisonTree has children, represented by ArrayList of DecisionTree
    * objects.
    */
    TreeNode root;
    int id = 0;
    int level = 0;
    ArrayList<DecisionTree> children = new ArrayList<DecisionTree>();
    DecisionTree(TreeNode root){
        this.root = root;
    }
    DecisionTree(DecisionTree dt){
        /*
        * Just as told in TreeNode class, we need an explicit constructor so 
        * that when we are pruning, we can make a decisionTree copied by value
        * instead of by reference.
        */
        this.root = new TreeNode(dt.root);
        for(DecisionTree child: dt.children){
            this.children.add(new DecisionTree(child));
        }
        this.id = dt.id;
        this.level = dt.level;
    }
    void addChild(DecisionTree child){
        /*
        * A utility function to add a child to the DecisionTree object.
        */
        this.children.add(child);
    }
    void prune(){
        /*
        * This function prunes the current tree.
        * pruning essentially means:
        *   1) Make it a leaf node.
        *   2) Set classification equal to majority classification.
        * Apart from doing the above two, this method also clears away all the 
        * children of the DecisionTree being pruned becasue, a leaf node isn't
        * supposed to have any children.
        */
        this.root.isleaf = true;
        this.root.classification = this.root.majorityClassification();
        this.children.clear();
    }
    void printTree(){
        /*
        * A utility funtion to printTree.
        * It is a recursive function that prints all children of the currentTree
        * that are leaf nodes. For any child that is not a leaf node, it calls
        * printTree() function for that child.
        */
        if(root.isleaf){
            // if the root is leaf, just print the tree and return.
            System.out.println("leaf node: " + root.classification);
            return;
        }else if(root.splitAttribute == null){
            // splitAttribute is null and it's not a leaf node => it is a 
            // conditional node and has a splitValue associated with it.
            System.out.println("conditional node: " + root.splitValue);
            for (DecisionTree child : this.children) {
                // Conditional nodes only have one child. Just call the same
                // printTree() function for that child.
                child.printTree();
            }
        }else{
            // Doesn't satisfy above two conditons => it is a node that has a 
            // splitAttribute. 
            System.out.println("TreeNode: " + root.splitAttribute.name);
            for (DecisionTree child : this.children) {
                child.printTree();
            }
        }
    }
    int maxLevel(DecisionTree dt){
        /*
        * Utility function to find maxLevel of a decisionTree. maxlevel is
        * essentially the leafNode with highest level attribute. This funcion
        * recusively finds all the leaf nodes of a decisionTree and find the 
        * maximum level amongst them all.
        * @dt : decision tree whose lowest leaf i.e highest level needs to be
        * found
        * return_value : max level of all leaf nodes.
        */
        if(dt.root.isleaf){
            // if the root is leaf, we are done. Just return the level of this
            // root.
            return dt.level;
        }else if(dt.children.size() == 0){
            // if it has no children, but for some reason it is not labeled as 
            // a leaf, we are done. Just return the level of this root.
            return dt.level;
        }
        else{
            // if it reached here, it means it has children. Call this funciton 
            // on all the children, and return maximum of all the levels.
            ArrayList<Integer> levels = new ArrayList<Integer>();
            for(DecisionTree child: dt.children){
                int level = maxLevel(child);
                levels.add(level);
            }
            return Collections.max(levels);
        }
    }
    void getElementIds(DecisionTree dt, int level, ArrayList<Integer> ids){
        /*
        * A recursive function to get all the nodes of a given level in a given
        * tree.
        * @dt : the decisionTree in which we have to find nodes of a given
        * level
        * @level : we have to find nodes at level given by "level"
        * @ids : we fill this array with all nodes of a given level.
        * return_value : none
        */
        if(dt.level == level){
            // if level of the current node is required level, add it to the
            // arrayList
            ids.add(dt.id);
        }else if(dt.level > level){
            // if the level of this node is greator than required, traversing
            // further is useless. Just return.
            return;
        }else{
            // if it reached here, it means required nodes are present below its
            // children. Call this function again for all of its children.
            for(DecisionTree child: dt.children){
                getElementIds(child, level, ids);
            }
        }
    }
    DecisionTree getElementById(DecisionTree dt, int id){
        /*
        * A function that returns a DecisionTree object whose id  = id. Very 
        * useful while pruning.
        * @dt : a tree root that has DecisioonTree node with id = id
        * @id : unique identification for a tree node.
        * return_value : A decisionTree whose id = id
        */
        // req stores the required decision Tree
        DecisionTree req = null;
        if(dt.id == id){
            // if the current decisionTree has required id, we are done. It is 
            // the required decision tree.
            req = dt;
            return req;
        }
        if((dt.root.isleaf || dt.root.data.size() == 0) && dt.id != id){
            // if the dt.id != id and it's not leaf or has children, then we 
            // can't do anything else. 
            req = null;
        }
        for(DecisionTree child : dt.children){
            // If we reached here, it means the children of the current decision
            // tree will have the required node. Call the same function again
            // on each child.
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
            // if we reached the root while traversing the tree, we are done. 
            // just return the classification here.
            return tree.root.classification;
        }
        if(tree.root.splitAttribute == null){
            // if splitAttribute == null, it means either it is conditional node
            // or it is a leaf node which hasn't been assigned as leaf.
            if(tree.children.size() == 0){
                // it's a leaf node. Return the classifcation.
                return tree.root.classification;
            }
            // if its' a conditional node, it has only one child. Call the same
            // function on its only child.
            classification = getClassification(tree.children.get(0), t);
        }
        else{
            // If it reached here, it means we are on a node that has splitattr
            // Any node that has a split attribute will have number of paths 
            // equal to number of possible values it can take. Therefore we have
            // to choose which path to follow.
            // index : indexo f the splitAttribute
            int index = tree.root.splitAttribute.index;
            // value : value that the training Data has of the splitAttribute
            String value = t.attributes[index];
            if(value.equals("?")){
                // "?" represents missing value. In case of a missing value, we 
                // just take the most common value for that attribute at that 
                // node
                value = id3.getMostCommonValue(tree.root.data, index);
            }
            for(DecisionTree child : tree.children){
                // now that we have the value, we have to check which path to 
                // follow in the tree. We have to follow a path whose splitValue
                // is same as our current value.
                if(value.equalsIgnoreCase(child.root.splitValue)){
                    if(child.root.data.size() == 0){
                        // we've reached the child node. Just return the 
                        // classification.
                        return child.root.classification;
                    }
                    if(child.root.data.get(0).attributes[index].equals(value)){
                        // there are still nodes to traverse. Call the same 
                        // function again.
                        classification = getClassification(child, t);
                    }
                }
            }   
        }
        return classification;
    }
}