package candidate;

import java.util.Scanner;
import java.io.*;
import java.util.*;


class Reader{
    /*
    * An utility class to read data from specified by fileName and perform one
    * vs all strategy.
    */
    public static ArrayList<TrainingData> read(String filename, int type) throws 
    FileNotFoundException {
        /*
        * Reads data from filename, considers each line as a trainingExample and 
        * splits the values at "," with each value considered as an attribute.
        * Performs one vs all by setting classification zero for all TrainingData
        * whose classification != type and sets 1 otherwise
        */
        ArrayList<TrainingData> training = new ArrayList<TrainingData>();
        Scanner in = new Scanner(new FileReader(filename));
        while(in.hasNext()){
            String[] columns = in.next().split(",");
            int[] attributes = new int[17];
            int j = 0;
            for (int i=1;i<columns.length;i++ ) {
                attributes[j++] = Integer.parseInt(columns[i]);
            }
            TrainingData train = new TrainingData(attributes);            
            train.setName(columns[0]);
            train.oneVsall(type);
            training.add(train);
        }
        return training;
    }
}
