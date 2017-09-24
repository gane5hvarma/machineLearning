package decision_tree;

import java.util.Scanner;
import java.io.*;
import java.util.*;


class Reader{
    public static TrainingData[] read(String filename) throws 
    FileNotFoundException {
        ArrayList<TrainingData> training = new ArrayList<TrainingData>();
        Scanner in = new Scanner(new FileReader(filename));
        while(in.hasNext()){ 
            String[] attributes = in.nextLine().split(",");
            for (int i = 0; i < attributes.length; i++ ) {
                attributes[i] = attributes[i].trim();
            }
            TrainingData train = new TrainingData(attributes);
            training.add(train);
        }
        TrainingData[] examples = new TrainingData[training.size()];
        examples = training.toArray(examples);
        return examples;
    }
}
