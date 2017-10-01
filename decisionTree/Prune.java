package decision_tree;

import java.util.*;

class Prune{
    /*
    * A class which defines the function to prune the decisionTree until maximum
    * accuracy is obtained.
    * @bestTree : The tree with highest accuracy. 
    * @bestAccuracy : Accuracy given by bestTree.
    * @train : validation set on which we are testing accuracy of the tree being
    * pruned
    */
    DecisionTree bestTree;
    double bestAccuracy;
    TrainingData[] train;
    Prune(DecisionTree dt, double accuracy, TrainingData[] data){
        bestTree = dt;
        bestAccuracy = accuracy;
        train = data;
    }
    DecisionTree prune(){
        /*
        * A method that iteratively prunes the bestTree in top-down fashion 
        * until we reach maximum accuracy.
        * Pruning is used to remove ill effects of overfitting. We go to each
        * node, turn it to a leaf node and make it's classification equal to
        * most common classification at that node and check for accuracy. If the
        * accuracy is higher or equal to previous accuracy, we make this tree
        * as the bestTree and continue the process. Otherwise we restore the 
        * node to how it was before pruning, and we continue the process.
        * This function uses the following algorithm to prune the tree:
        *       * Find the number of levels in the decisionTree
        *       * For each level, find ids of all the nodes in that level.
        *       * Prune each node at a level. If pruning the node does not lower
        *         the acuracy, keep it pruned. Otherwise revert it back to how
        *         it was before pruning.
        *       * stop once any level has zero nodes.
        * return_value : the best tree after pruning!
        */
        long startTime = System.currentTimeMillis();
        // To store the accuracy of the tree after it's pruned.
        double newAccuracy = 0;
        // copiedTree and updatedTree act as backups at different steps of 
        // pruning. Note that we cannot simply assign copiedTree = bestTree 
        // because in java object assignment takes place by reference. When
        // two objects have same reference, changing one changes another. Hence
        // we have to make sure that copied and updated trees do not share a
        // reference with bestTree.
        DecisionTree copiedTree = new DecisionTree(bestTree);
        DecisionTree updatedTree = new DecisionTree(copiedTree);
        // number of levels in the bestTree.
        int levels = bestTree.maxLevel(bestTree);
        for(int i = 1; i < levels; i = i+2){
            ArrayList<Integer> ids = new ArrayList<Integer>();
            // the arrayList ids contains the ids of all the nodes present at 
            // that level in the decisionTree.
            updatedTree.getElementIds(updatedTree, i, ids);
            if(ids.size() == 0){
                // A level can have zero nodes though the original tree didn't.
                // This is because we are pruning top down, there will be many
                // branches that are removed entirely because a node at the top
                // level was pruned.
                break;
            }
            for(int id: ids){
                // for each id in the ids list, get the corresponding node from
                // updatedTree. Note that, though updatedTree and bestTree had 
                // shared no reference, they are copied by values. Therefore, 
                // the ids of nodes reamin same in both the trees.
                DecisionTree child = updatedTree.getElementById(updatedTree,id);
                child.prune();
                // prune the child and get accuracy. Note that we are pruning
                // a node from updatedTree, therefore it doesn't affect bestTree
                // or copiedTree since they do not share any references.
                newAccuracy = id3.getAccuracy(updatedTree, train);
                if(newAccuracy >= bestAccuracy){
                    // we have to make sure that this tree reamins pruned. Hence
                    // update the copiedTree to currentTree.
                    // If the newAccuracy was lesser than bestAccuracy,
                    // copiedTree would have been equal to the updatedTree before
                    // it was pruned.
                    System.out.println("pruned to "+newAccuracy);
                    bestAccuracy = newAccuracy;
                    copiedTree = new DecisionTree(updatedTree); 
                }
                System.out.println("current accuracy" + bestAccuracy);
                // update the updated tree.
                updatedTree = new DecisionTree(copiedTree);
            }
        }
        long endTime = System.currentTimeMillis();   
        double time = (double)(endTime - startTime)/60000;
        // print time taken.
        System.out.println(time);
        // print accuracy.
        System.out.println(bestAccuracy);
        return updatedTree;
    }
}