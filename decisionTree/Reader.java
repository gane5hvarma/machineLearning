package decision_tree;

import java.util.Scanner;
import java.io.*;
import java.util.*;


class Reader{
    public static ArrayList<TrainingData> read(String filename, int type) throws 
    FileNotFoundException {
        ArrayList<TrainingData> training = new ArrayList<TrainingData>();
        Scanner in = new Scanner(new FileReader(filename));
        while(in.hasNext()){ 
            String[] attributes = in.next().split(",");
            TrainingData train = new TrainingData(attributes);
            training.add(train);
        }
        return training;
    }
}
