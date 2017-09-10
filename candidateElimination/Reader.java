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
            Hypothesis hypo = new Hypothesis();
            hypo.setName(columns[0]);
            hypo.setHair(Integer.parseInt(columns[1]));
            hypo.setFeathers(Integer.parseInt(columns[2]));
            hypo.setEggs(Integer.parseInt(columns[3]));
            hypo.setMilk(Integer.parseInt(columns[4]));
            hypo.setAirborne(Integer.parseInt(columns[5]));
            hypo.setAquatic(Integer.parseInt(columns[6]));
            hypo.setPredator(Integer.parseInt(columns[7]));
            hypo.setToothed(Integer.parseInt(columns[8]));
            hypo.setBackbone(Integer.parseInt(columns[9]));
            hypo.setBreathes(Integer.parseInt(columns[10]));
            hypo.setVenomous(Integer.parseInt(columns[11]));
            hypo.setFins(Integer.parseInt(columns[12]));
            hypo.setLegs(Integer.parseInt(columns[13]));
            hypo.setTail(Integer.parseInt(columns[14]));
            hypo.setDomestic(Integer.parseInt(columns[15]));
            hypo.setCatsize(Integer.parseInt(columns[16]));
            hypo.setType(Integer.parseInt(columns[17]));
            hypothesis.add(hypo);
        }
        return hypothesis;
    }
}