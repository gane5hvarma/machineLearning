package decision_tree;

import java.util.Arrays;
import java.util.Comparator;
import java.util.*;
import java.io.*;
import java.lang.*;


class PreProcessor{
    /*
    * Except for the static methods in this class, we do not use majority of the
    * class any where in the actual construction of the tree. The sole purpose
    * of majority of methods in this class is merely to pre-process the data.
    * Pre Processing of the data includes:
    *    * Finding thresholds for all the continous valued attributes in the 
           given training data.
         * Modify the given training data / validation data/ test data by changing
           the values of continous valued attributes to either of "aT{Threshold}"
           or "bT{Threshold}" if they are above or below the attribute's 
           threshold respectively, and write the modified data to a new file.
    * This class also defines methods to calculate Entropy (Both static and non
    * static) and overloaded methods to calculate information gain.
    * Array of TrainingData examples represents the training data samples the 
    * preProcesor is supposed to work on.
    * The integer array continousValues has indices of all the continuous valued
    * attributes.
    * The double array thesholds is meant to hold values of thersholds of each
    * continuous valued attribute. For instance thresholds[i] has threshold for
    * the attribute with index continuousValues[i]
    * Entropy is entropy of the sample the PreProcessor is working on. 
    */
    TrainingData[] examples;
    int[] continuousValues = {0, 2, 4, 10, 11, 12};
    double[] thresholds = new double[6]; 
    double Entropy;

    PreProcessor(TrainingData[] examples){
        this.examples = examples;
        getEntropy();
    }
    PreProcessor(TrainingData[] examples, double[] thresholds){
        this.examples = examples;
        this.thresholds = thresholds;
    }
    void getEntropy(){
        /*
        * A simple method to calculate Entropy of the traingData the preProcessor
        * is currently working on. 
        * Entropy is calculated as summation of p(i)*log(p(i)) where p(i) is the
        * proportion of elments belonging to classification 'i'. we have only 2
        * classifications >=50K and <=50K, so calculation of entropy is very
        * simple here.
        */
        double positiveExamples = 0;
        double negativeExamples = 0;
        // here positiveExamples keeps track of number of examples with class
        // <=50K and negativeExamples keeps track of number of examples with
        // class >50K
        for (TrainingData example : this.examples) {
            if(example.getValue().equalsIgnoreCase("<=50K")){
                positiveExamples++;
            }else{
                negativeExamples++;
            }
        }
        double posWeight = positiveExamples/this.examples.length;
        double negWeight = negativeExamples/this.examples.length;
        // posWeight and negWeight are the p(i) in the formula mentioned above.
        // if either posWeight or negWeight is zero, the entropy is NaN because
        // log(0) is NaN. If either of them is zero, it implies that our sample
        // contains only one classification and the zero weight can be ignored.
        // To ignore the "zero" weight classification in the entropy calculation
        // we give it a weight of 1 because log(1) = 0
        if(posWeight == 0){
            posWeight = 1;
        }
        if (negWeight == 0) {
            negWeight = 1;
        }
        double entropy = -posWeight*(Math.log(posWeight)/Math.log(2)) 
                         - negWeight*(Math.log(negWeight)/Math.log(2));
        this.Entropy = entropy;                         
    }
    static double getEntropy(ArrayList<TrainingData> examples){
        /*
        * Same method as above, except sine this is static, it can be used by 
        * other classes without creating a PreProcessor object.
        * ArrayList<TrainingData> examples is the entropy to which we have to 
        * calculate the entropy of (In the previous method, examples = 
        * this.examples)
        * Returns Entropy (In the previous method, it just fills this.Entropy)
        */
        double positiveExamples = 0;
        double negativeExamples = 0;
        for (TrainingData example : examples) {
            if(example.getValue().equalsIgnoreCase("<=50K")){
                positiveExamples++;
            }else{
                negativeExamples++;
            }
        }
        double posWeight = positiveExamples/examples.size();
        double negWeight = negativeExamples/examples.size();
        if(posWeight == 0){
            posWeight = 1;
        }
        if (negWeight == 0) {
            negWeight = 1;
        }
        double entropy = -posWeight*(Math.log(posWeight)/Math.log(2)) 
                         - negWeight*(Math.log(negWeight)/Math.log(2));
        return entropy;   
    }
    void sortExamples(int i){
        /*
        * A simple sort method to sort our trainingData based on the index 'i'
        * We override the Arrays.Sort method to compare the required attribute
        * of two trainingData objects.
        */
        Arrays.sort(examples, new Comparator<TrainingData>(){
            @Override
            public int compare(TrainingData t1, TrainingData t2){
                return t1.attributes[i].compareTo(t2.attributes[i]);
            }
        });
    }
    void fillThresholds(){
        /*
        * A method used to fill the this.thresholds array. Each element in this
        * array gives the value at which a continous valued attribute should be
        * broken to make it discrete. This threshold is calculated in such a way 
        * that informationGain obtained is maximum when continous valued attr
        * is broken at this point. The formal algorith is as follows.
        *   * Sort the samples based on the continous valued attribute we have
        *     to find the threshold of.
        *   * On this sorted list, find every pair of consecutive points where
        *     classification changes. Call the midpoint of these two consecutive
        *     as an inflection point.
        *   * Of all the inflection points obtained that way, find the point 
        *     that gives highest information gain. It is the threshold.
        */
        for(int i = 0; i < this.continuousValues.length; i++){
            // We loop through the continuousValues array. continuousValues[i]
            // is the index of a continuous valued attribute. 
            int index = this.continuousValues[i];
            // Sort the trainingData bsed on the index.
            sortExamples(index);
            // initialize the threshold to the first value of the attribute
            String thresh = this.examples[0].attributes[index];
            double threshold = Double.parseDouble(thresh);
            // initalize maximum informationGain as informatin gain obtained at
            // the current threshold.
            double informationGain = getInformationGain(index, threshold);
            for(int j = 0; j < this.examples.length - 1; j++){
                // loop through all trainingData samples and find the attribute
                // values in current and the consecutive examples.
                String curr = this.examples[j].getValue();
                String next = this.examples[j+1].getValue();
                if(!curr.equalsIgnoreCase(next)){
                    // if current != next, it means the midpoint of current and 
                    // next is an inflection point. Cast it to double and find 
                    // the midPoint, and get Information gain obtained when we
                    // split the continuous valued attribute at that point.
                    double current = Double.parseDouble(
                                                examples[j].attributes[index]);
                    double after = Double.parseDouble(
                                              examples[j+1].attributes[index]);
                    // getInformationGain for continousValued attributes is a
                    // bit different from that for discreteValued attributes.
                    // Please look at the function definition for more info.
                    double currIG = getInformationGain(index,(current+after)/2);
                    if(currIG > informationGain){
                        // update the threshold and max information gain
                        informationGain = currIG;
                        threshold = (current + after)/2;
                    }
                }
            }
            // fill the thresholds array. thresholds[i] gives thre threshold for
            // continous valued attribute of index continuousValued[i]
            this.thresholds[i] = threshold;
        }
    }
    void makeDiscrete(boolean a){
        /*
        * This is the function that does the preProcessing. It modifies the 
        * training data the PreProcessor object is working on by changing the
        * values of each continuous valued attribute to either "aT{threshold}" /
        * "bT{threshold}" where :
        *               * aT means above Threshold
        *               * bT means below Threshold
        *               * {threshold} is the Threshold of the current attribute
        * It then writes the modified attributes to the specified file, given by
        * "fileName", so that they can be used in construction of tree.
        */
        if(a){
            // The boolean a here represents whether we are working on Training
            // Data or testData. We only need to calculate thresholds for the
            // Training Data and then use those thresholds to make both test
            // and training data attributes discrete.
            // a = True => working on TrainingData
            // a = False => working on TestData
            fillThresholds();
        }
        for (TrainingData example: this.examples) {
            // consider each example in the trainingData and modify them.
            for(int j = 0; j < this.thresholds.length; j++){
                // consider each threshold in the thresholds array and get the 
                // index corresponding to the threshold from continuousValues
                // array.
                int index = this.continuousValues[j];
                double threshold = this.thresholds[j];
                double value = Double.parseDouble(example.attributes[index]);
                if(value > threshold){
                    // aT{Threshold} if the value is above threshold.
                    example.attributes[index] = "aT" + 
                                                      String.valueOf(threshold);
                }else{
                    // bT{Threshold} if the value is below threshold 
                    example.attributes[index] = "bT" +
                                                      String.valueOf(threshold);                    
                }
            }
        }
        try{
            writeToFile("modifiedTest.data");
        }catch(IOException e){
            System.out.println("Something went wrong");
        }
    }
    double getInformationGain(int index){
        /*
        * A function to calculate infromation gain corresponding to the attr 
        * correponding to the given index.
        * Entropy is defined as number of bits needed to encode the data, and
        * InformationGain of an attribute is defined as number of bits that can
        * be saved while encoding the data if the value of the attribute is 
        * known.
        * To caclulate information gain:
        *       * consider each value the attribute can take and make a subset 
        *         of sample and calculate entropy for each such subset similar
                  to how entropy was calculated above. Sum all such entropies to
                  calculate Gain.
        *       * InformationGain = Entropy - Gain
        * this function and algorithm are used to calculate information gain for
        * discrete valued attributes. Though the basic logic is same even for
        * continuous valued attributes, the implementation is a little bit
        * different. Please refer to that function for more information.
        */
        double currEntropy = 0;
        // currEntropy represents Gain.
        String[] values = TrainingData.getAcceptedValues(index);
        // Take each value in the acceptableValues of the attribute and find
        // Entropy for each subset of the TrainingData sample that has value of
        // attribute == value.
        for(String value: values){
            double posExamples = 0;
            double negExamples = 0;
            double len = 0;
            // We don't actually need to make a subset every time. We just
            // ignore any traningData whose value of the current attribute is 
            // not equal to value.
            // len keeps track of number of trainingData examples with value of
            // current attribute == value and for all such trainingData 
            // posExamples keeps track of number of trainingData with class
            // <=50K and negExamples keeps track of trainingData with class
            // >50K
            for (TrainingData example : this.examples) {
                if(example.attributes[index].equalsIgnoreCase(value)){
                    // Ignore every TrainingData whose value of the current attr
                    // is not equal to value.
                    len++;
                    if(example.getValue().equalsIgnoreCase("<=50K")){
                        posExamples++;  
                    }else{
                        negExamples++;
                    }
                }
            }
            double weight = -1 * len/this.examples.length;
            // to facilitate ease of calculation.
            double posWeight = posExamples/len;
            double negWeight = negExamples/len;
            // posWeight and negWeight represent proportion similar to their
            // counter parts in getEntropy() function.
            if(posWeight == 0 || Double.isNaN(posWeight)){
                posWeight = 1;
            }
            // the posWeight == 0 and negWeight == 0 are similar to those in 
            // getEntropy() function. But, in this case, we also have to consier
            // the case that there might be no TrainingData in our sample whose
            // value is equal to value. In that case since weight = 0, it gives
            // 0 contribution to infromation gain. But since division by zero
            // gives NaN, we have to take care of it before we apply log.
            if (negWeight == 0 || Double.isNaN(negWeight)) {
                negWeight = 1;
            }
            double entropy = posWeight*(Math.log(posWeight)/Math.log(2)) 
                             + negWeight*(Math.log(negWeight)/Math.log(2));
            // add the current gain to currEntropy
            currEntropy += (weight*entropy);
        }
        // InformationGain = Entropy - Gain
        return this.Entropy - currEntropy;
    }
    static double getInformationGain(ArrayList<TrainingData> examples, 
                                    int index, double Entropy){
        /*
        * Same method as above, but this is static so that other classes can
        * use this function to calculate information gain. This method is useful
        * to determine best attribute to use as splitAttr while
        * builiding the tree.
        * @examples : TrainingData sample on which information gain is to be 
        * calcualted.
        * @Entropy : Total entropy of @examples
        * @index : index of the attribute for which information gain is to be
        * cacluated
        * return_value: information gain
        */
        double currEntropy = 0;
        String[] values = TrainingData.getAcceptedValues(index);
        for(String value: values){
            double posExamples = 0;
            double negExamples = 0;
            double len = 0;
            for (TrainingData example : examples) {
                if(example.attributes[index].equalsIgnoreCase(value)){
                    len++;
                    if(example.getValue().equalsIgnoreCase("<=50K")){
                        posExamples++;  
                    }else{
                        negExamples++;
                    }
                }
            }
            double weight = -1 * len/examples.size();
            double posWeight = posExamples/len;
            double negWeight = negExamples/len;
            if(posWeight == 0 || Double.isNaN(posWeight)){
                posWeight = 1;
            }
            if (negWeight == 0 || Double.isNaN(negWeight)) {
                negWeight = 1;
            }
            double entropy = posWeight*(Math.log(posWeight)/Math.log(2)) 
                             + negWeight*(Math.log(negWeight)/Math.log(2));

            currEntropy += (weight*entropy);
        }
        // Note the difference between the method above and this one. In the
        // method above Entropy = this.Entropy i.e Entropy of the sample
        // PreProcessor object is working on. Where as here, Entropy is the 
        // value given to the method. Therefore for proper functioning of this
        // method, we have to make sure that entropy is calculated before hand.
        return Entropy - currEntropy;
    }
    static Attribute getBestAttribute(ArrayList<Attribute> attributes, 
        ArrayList<TrainingData> examples, double Entropy){
        /*
        * While building the decision Tree, we decide which attribute to split
        * the examples based on information gain. For the set of examples at a
        * node, the splitAttribute is the attribute which gives the most 
        * information gain among the attributes available at that node.
        * @Attributes -> list of attributes to be considered while deciding best
        * Attribute
        * @examples -> TrainingData sample on which information gain must be 
        * calculated
        * @Entropy -> Entropy of @exampes
        * return_vale -> bestAttribute with maximum information gain
        */
        // initialize bestAttribute to first attribute and maxInfomrationGain to
        // information gain obtaied by the current bestAttribute.
        Attribute bestAttribute = attributes.get(0);
        double maxInformationGain = getInformationGain(examples, 0, Entropy);
        for(Attribute attribute : attributes){
            int index = attribute.index;
            // getInformationGain for each attribute and update 
            // maxInformationGain and bestAttribute accordingly.
            double ig = getInformationGain(examples, index, Entropy);
            if(ig > maxInformationGain){
                maxInformationGain = ig;
                bestAttribute = attribute;
            }
        }
        return bestAttribute;
    }
    double getInformationGain(int index, double threshold){
        /*
        * This is very similar to the getInformationGain(int index) method used
        * for calculating informationGain of discrete values. But unlike for
        * discrete values, there are only two possible values a continous valued
        * attribute can take : aboveThreshold and belowThreshold.
        * Since makeDicrete() uses this method to calculate information gain at
        * each threshold, we should not modify the data itself. Instead, keep 
        * track of the number of examples above and below threshold, and number
        * of postive and negative examples in each case and then find Total Gain.
        * @index : index of continuousValued attribute
        * @threshold : the point wrt which information gain should be calculated
        * return_vale : informationGain
        */
        // CurrEntropy keeps track gain
        double currEntropy = 0;
        // posExamplesAboveThreshold keeps track of all examples above 
        // Threshold whose classification is <=50K
        double posExamplesAboveThreshold = 0;
        // negExamplesAboveThreshold keeps track of all examples above 
        // Threshold whose classification is >50K
        double negExamplesAboveThreshold = 0;
        // posExamplesBelowThreshold keeps track of all examples Below 
        // Threshold whose classification is <=50K
        double posExamplesBelowThreshold = 0;
        // netExamplesBelowThreshold keeps track of all examples Below 
        // Threshold whose classification is >50K
        double negExamplesBelowThreshold = 0;
        // lenAboveThreshold of number of training examples above threshold
        double lenAboveThreshold = 0;
        // lenBelowThreshold of number of training examples Below threshold
        double lenBelowThreshold = 0;
        for (TrainingData example : this.examples) {
            // Loop through all the examples in given sample and fill the above
            // variables accordingly.
            if (Double.parseDouble(example.attributes[index]) > threshold){
                lenAboveThreshold++;
                if(example.getValue().equalsIgnoreCase("<=50K")){
                    posExamplesAboveThreshold++;
                }else{
                    negExamplesAboveThreshold++;
                }
            }else{
                lenBelowThreshold++;
                if(example.getValue().equalsIgnoreCase("<=50K")){
                    posExamplesBelowThreshold++;
                }else{
                    negExamplesBelowThreshold++;
                }
            }
        }
        // similar to getInformationGain() for discrete valued functins consider
        // aboveThreshold and belowThreshold as the two classes. Since there are
        // only two classes we can calculate entropy for each class directly.
        // The following logic is same as the logic used in getInformationGain()
        // for discrete variables.
        double aboveThresholdWeight = -1 * lenAboveThreshold/examples.length;
        double posWeightAbove = posExamplesAboveThreshold/lenAboveThreshold;
        double negWeightAbove = negExamplesAboveThreshold/lenAboveThreshold;
        if(posWeightAbove == 0){
            posWeightAbove = 1;
        }
        if (negWeightAbove == 0) {
            negWeightAbove = 1;
        }
        double entropyAbove = posWeightAbove * (Math.log(posWeightAbove)/Math.log(2)) 
                         + negWeightAbove * (Math.log(negWeightAbove)/Math.log(2));
        double belowThresholdWeight = -1 * lenBelowThreshold/examples.length;
        double posWeightBelow = posExamplesBelowThreshold/lenBelowThreshold;
        double negWeightBelow = negExamplesBelowThreshold/lenBelowThreshold;
        if(posWeightBelow == 0){
            posWeightBelow = 1;
        }
        if (negWeightBelow == 0) {
            negWeightBelow = 1;
        }
        double entropyBelow = posWeightBelow * (Math.log(posWeightBelow)/Math.log(2)) 
                         + negWeightBelow * (Math.log(negWeightBelow)/Math.log(2));
        currEntropy = aboveThresholdWeight * entropyAbove
                     + belowThresholdWeight * entropyBelow;
        return this.Entropy - currEntropy;
    }
    void writeToFile(String fileName) throws IOException{
        /*
        * Just a utility function to write the modified trainingData sample
        * (modified) by makeDicrete() into a file specified fileName
        */
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        for (TrainingData example : this.examples){
            for(int k = 0; k < example.attributes.length -1; k++){
                out.write(example.attributes[k]);
                out.write(",") ;
            }
            out.write(example.getValue());
            out.newLine();
        }
        out.close();
    }
    /*
    * How to use this class?
    * Given Training Data, make a preProcessor object with that data and 
    * makeDicrete with a = True,
    * Now make another Preprocessor object using the second constructor, with 
    * examples = TrainingData and thresholds = obj1.thersholds.
    * makeDiscrete with a = False.
    */
}