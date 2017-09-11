package candidate;

import java.util.Scanner;
import java.io.*;
import java.util.*;


class Reader{
    public static ArrayList<Hypothesis> read(String filename) throws 
    FileNotFoundException {
        ArrayList<Hypothesis> hypothesis = new ArrayList<Hypothesis>();
        Scanner in = new Scanner(new FileReader(filename));
        while(in.hasNext()){
            String[] columns = in.next().split(",");
            int[] attributes = {0};
            int j = 0;
            for (int i=1;i<columns.length;i++ ) {
                attributes[j++] = Integer.parseInt(columns[i]);
            }
            Hypothesis hypo = new Hypothesis(attributes);            
            hypo.setName(columns[0]);
            hypothesis.add(hypo);
        }
        return hypothesis;
    }
}