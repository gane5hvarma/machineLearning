package candidate;

import java.util.Scanner;
import java.io.*;
import java.util.*;


class Reader{
    public static ArrayList<TrainingData> read(String filename, int type) throws 
    FileNotFoundException {
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
    /*
    * Eventually move this main away from this class and put it in some other 
    * class that acts as entry point to this pacakge. This class should only 
    * deal with file reading.

    * Also note that to compile use: javac -d . filename.java
    * To run, use java candidate.ClassName

    public static void main(String[] args) {
        try{
            ArrayList<Hypothesis> hypothesis = Reader.read("zoo.data",1);
            for (int i = 0;i < hypothesis.size() ;i++ ) {
                System.out.println(hypothesis.get(i).attributes[Hypothesis.TYPE]);
            }
        }catch(FileNotFoundException e){
            System.out.println("File not found");
            return;
        }
    } */
}
