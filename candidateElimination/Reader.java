package candidate;

import java.util.Scanner;
import java.io.*;
import java.util.*;


class Reader{
    public static ArrayList<Hypothesis> read(String filename, int type) throws 
    FileNotFoundException {
        ArrayList<Hypothesis> hypothesis = new ArrayList<Hypothesis>();
        Scanner in = new Scanner(new FileReader(filename));
        while(in.hasNext()){
            String[] columns = in.next().split(",");
            int[] attributes = new int[17];
            int j = 0;
            for (int i=1;i<columns.length;i++ ) {
                attributes[j++] = Integer.parseInt(columns[i]);
            }
            Hypothesis hypo = new Hypothesis(attributes);            
            hypo.setName(columns[0]);
            hypo.oneVsall(type);
            hypothesis.add(hypo);
        }
        return hypothesis;
    }
    /*
    * Eventually move this main away from this class and put it in some other 
    * class that acts as entry point to this pacakge. This class should only 
    * deal with file reading.

    * Also note that to compile use: javac -d . filename.java
    * To run, use java candidate.ClassName
    */
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
    }
}
